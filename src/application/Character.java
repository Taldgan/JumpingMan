package application;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Character {

	int lives;
	static Boolean dead;
	boolean winning = false;
	Boolean jumping, walking;
	double dx, dy, platdx = 0, platdy = 0;
	double predeathy, predeathx, predeathTranslateX, predeathTranslateY;
	double safeY, safeTranslateY;
	boolean onMovingPlat = false;
	double x, y, minY = 280;
	double prevX, prevY, prevTranslateX, prevTranslateY;
	double size;
	Color color;
	Circle character = new Circle(x, y, size, color);
	boolean collide, collideRight, collideLeft, collideTop, collideBottom;
	double groundLvl;
	double radius;
	private boolean dir;
	private boolean animating = false;
	int winPlatX;
	private boolean animatingWinG = false;
	private int deathAnimationCount = 150;
	private int winAnimationCount = 0;
	double collisionTimeDelta = 0, startTime = 0;
	
	public Character(double x, double y, double size, Color color) {
		dead = false;
		jumping = false;
		walking = false;
		collide = false;
		collideLeft = false;
		collideRight = false;
		radius = size;
		groundLvl = 280;
		dir = true;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
	}

	public Character(double x, double y, double size, Color color, int lives) {
		this.lives = lives;
		dead = false;
		jumping = false;
		walking = false;
		collide = false;
		collideLeft = false;
		collideRight = false;
		radius = size;
		groundLvl = 280;
		dir = true;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
	}

	public void deathByEnemy() {
		predeathx = getx();
		predeathy = gety();
		predeathTranslateX = character.getTranslateX();
		predeathTranslateY = character.getTranslateY();
		System.out.println("Death by enemy");
		if (dead) {
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			LevelManager.lifeCounter.getChildren().remove(getLives());
			StateManager.gameState = State.DYING;
		}
	}
	public void deathByHole(Group group, int respawnX) {
		int oldX = (int) getx();
		setx(respawnX);
		int newX = (int) getx();
		this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() - (oldX-newX));
		character.setTranslateY(LevelManager.groundList.get(respawnX/LevelManager.tileWidth).getY()-LevelManager.groundLevel);
		group.setTranslateX(group.getTranslateX() + (oldX-newX));
		predeathx = respawnX;
		if (dead) {
			setdy(0);
			setdx(0);
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			LevelManager.lifeCounter.getChildren().remove(getLives());
			if(getLives() <= 0)
				StateManager.gameState = State.GAMEOVER;
			else
				StateManager.gameState = State.YOUDIED;
		}
	}

	public void move() {
		prevX = this.x;
		prevY = this.y;
		prevTranslateX = this.getCharacter().getTranslateX();
		prevTranslateY = this.getCharacter().getTranslateY();
		this.character.setTranslateX(this.character.getTranslateX() + getdx() + getPlatdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy() + getPlatdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());
	}

	public Circle getCharacter() {
		return this.character;
	}

	public void setCharacter(double x, double y, double size, Color color) {
		this.character.setCenterX(x);
		this.character.setCenterY(y);
		this.character.setRadius(size);
		this.character.setFill(color);
	}

	public void setWinACount(int count) {
		this.winAnimationCount = count;
	}
	public void animateWin() {
		setdx(0);
		System.out.println("animating: " + winAnimationCount);
		if(winAnimationCount > 0) {
			if(winAnimationCount == 0) {
				character.setTranslateX(winPlatX);
			}
			setdy(0);
			if(winAnimationCount % 15 == 0) {
				swapDir();
			}
			if(gety()+character.getRadius() < LevelManager.groundLevel && !animatingWinG) {
				if(dir) 
					character.setTranslateX(character.getTranslateX() + 3);
				else
					character.setTranslateX(character.getTranslateX() - 3);
				character.setTranslateY(character.getTranslateY() + 2.5);
			}
			else {
				if(winAnimationCount == 40) {
					animatingWinG = true;
					Sounds.sPlayer.playSFX(0);
				}
				else if(winAnimationCount < 20) {
					character.setTranslateY(character.getTranslateY() + 2.5);
				}
				else if(winAnimationCount < 40) {
					character.setTranslateY(character.getTranslateY() - 2.5);
				}
			}
			winAnimationCount--;
		}
		else {
			StateManager.gameState = State.NEXTLEVEL;
			LevelManager.levelOver();
		}
	}

	public void animateDeath() {
		System.out.println("animating: " + deathAnimationCount);
		if(deathAnimationCount >= 120) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()-0.5);
			deathAnimationCount--;
		}
		else if(deathAnimationCount > 0) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()+8);
			deathAnimationCount--;
		}
		else {
			this.deathAnimationCount = 150;
			this.animating = false;
			if(!animating) {
				setdx(0);
				setdy(0);
				this.getCharacter().setTranslateX(predeathTranslateX);
				this.getCharacter().setTranslateY(predeathTranslateY);
				sety(predeathy);
				setx(predeathx);
				if(getLives() != 0) 
					StateManager.gameState = State.YOUDIED;
				else
					StateManager.gameState = State.GAMEOVER;
			}
		}
	}

	public double getx() {
		return this.x;
	}

	public void setx(double x) {
		this.x = x;
	}

	public double gety() {
		return this.y;
	}

	public void sety(double y) {
		this.y = y;
	}

	public void setMinY(double y) {
		this.minY = y;
	}

	public double getSize() {
		return this.size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public double getdx() {
		return this.dx;
	}

	public void setdx(double dx) {
		// Set speed cap to 5 or -5
		if (dx > 5)
			dx = 5;
		else if (dx < -5)
			dx = -5;
		this.dx = dx;
	}

	public void setGroundLvl(double y) {
		groundLvl = y;
	}

	public double getdy() {
		return this.dy;
	}

	public void setdy(double dy) {
		this.dy = dy;
	}

	public void setJumping(boolean b) {
		this.jumping = b;
	}

	public boolean getJumping() {
		return this.jumping;
	}

	public void setWalking(boolean b) {
		this.walking = b;
	}

	public boolean getWalking() {
		return this.walking;
	}

	public boolean getCollide() {
		return collide;
	}

	public void setCollide(boolean collide) {
		if(collide) {
			startTime = System.currentTimeMillis();
			safeY = gety();
			safeTranslateY = character.getTranslateY();
		}
		else {
			collisionTimeDelta = System.currentTimeMillis() - startTime;
		}
		this.collide = collide;
	}

	public double getCollisionDelta() {
		return this.collisionTimeDelta;
	}

	public boolean getCollideLeft() {
		return collideLeft;
	}

	public void setCollideLeft(boolean collide) {
		this.collideLeft = collide;
	}

	public boolean getCollideRight() {
		return collideRight;
	}

	public void setCollideRight(boolean collide) {
		this.collideRight = collide;
	}

	public void setCollideTop(boolean b) {
		this.collideTop = b;
	}
	public boolean getCollideTop() {
		return this.collideTop;
	}

	public void setCollideBottom(boolean b) {
		this.collideBottom = b;
	}
	public boolean getCollideBottom() {
		return this.collideBottom;
	}

	public double getGroundLvl() {
		return groundLvl;
	}

	public double getMinY() {
		return this.minY;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double r) {
		radius = r;
	}

	public void swapDir() {
		if (dir)
			dir = false;
		else
			dir = true;
	}

	public double getPrevX() {
		return prevX;
	}

	public void setPrevX(double prevX) {
		this.prevX = prevX;
	}

	public double getPrevY() {
		return prevY;
	}

	public void setPrevY(double prevY) {
		this.prevY = prevY;
	}

	public double getPrevTranslateX() {
		return prevTranslateX;
	}

	public void setPrevTranslateX(double prevTranslateX) {
		this.prevTranslateX = prevTranslateX;
	}

	public double getPrevTranslateY() {
		return prevTranslateY;
	}

	public void setPrevTranslateY(double prevTranslateY) {
		this.prevTranslateY = prevTranslateY;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {
		return this.lives;
	}

	public void setDead(Boolean dead) {
		this.dead = dead;
	}

	public Boolean getDead() {
		return this.dead;
	}

	public double getPlatdx() {
		return this.platdx;
	}
	public double getPlatdy() {
		return this.platdy;
	}
	public void setPlatdx(double platdx) {
		this.platdx = platdx;
	}
	public void setPlatdy(double platdy) {
		this.platdy = platdy;
	}

	public void setOnMovingPlat(boolean b) {
		this.onMovingPlat = b;
	}

	public boolean getOnMovingPlat() {
		return this.onMovingPlat;
	}

	public boolean isAnimating() {
		return animating;
	}

	public void setAnimating(boolean animating) {
		this.animating = animating;
	}

	public void setWinning(boolean winning) {
		this.winning = winning;
	}

	public boolean isWinning() {
		return this.winning;
	}

}
