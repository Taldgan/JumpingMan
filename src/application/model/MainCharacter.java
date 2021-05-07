package application.model;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


public class MainCharacter extends Character {

	private boolean animatingWinG = false;
	private int deathAnimationCount = 150, winAnimationCount = 0;
	private Group hat;
	private boolean animating = false;
	private int winPlatX;
	private double predeathX, predeathY, predeathTranslateX, predeathTranslateY;
	private int lives;

	double hatPrevTranslateX, hatPrevTranslateY;

	public MainCharacter(double x, double y, double size, Color color, int lives) {
		super(x, y, size, color);
		this.lives = lives;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
		Ellipse hatBase = new Ellipse();
		Ellipse hatFront = new Ellipse();
		hatBase.setRadiusX(size);
		hatBase.setRadiusY(size/2);
		hatBase.setTranslateX(x+4);
		hatBase.setTranslateY(y-14);
		hatBase.setFill(Color.BLACK);
		hatFront.setRadiusX(size/1.2);
		hatFront.setRadiusY(size/2.5);
		hatFront.setTranslateX(x+9);
		hatFront.setTranslateY(y-7);
		hatFront.setFill(Color.web("0x33232D"));
		Label j = new Label("J");
		j.setTextFill(Color.WHITE);
		j.setTranslateX(hatFront.getTranslateX());
		j.setTranslateY(hatFront.getTranslateY()-9);
		hat = new Group(hatBase, hatFront, j);
	}

	public void deathByEnemy() {
		predeathX = getx();
		predeathY = gety();
		predeathTranslateX = character.getTranslateX();
		predeathTranslateY = character.getTranslateY();
		if (isDead()) {
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			if(getLives() <= 0)
				LevelManager.lifeCounter.getChildren().remove(getLives());
			StateManager.gameState = State.DYING;
		}
	}

	public void deathByHole(Group group, int respawnX) {
		int oldX = (int) getx();
		setx(respawnX);
		int newX = (int) getx();
		this.getCharacter().setTranslateX(this.getCharacter().getTranslateX() - (oldX-newX));
		this.getHat().setTranslateX(this.getHat().getTranslateX() - (oldX-newX));
		character.setTranslateY(LevelManager.groundList.get((respawnX-1)/LevelManager.tileWidth).getY()-(LevelManager.groundOffsets.get((respawnX-1)/LevelManager.tileWidth))-LevelManager.groundLevel);
		hat.setTranslateY(LevelManager.groundList.get(respawnX/LevelManager.tileWidth).getY()-(LevelManager.groundOffsets.get((respawnX-1)/LevelManager.tileWidth))-LevelManager.groundLevel);
		group.setTranslateX(group.getTranslateX() + (oldX-newX));
		predeathX = respawnX;
		if (isDead()) {
			setdy(0);
			setdx(0);
			setLives(getLives() - 1);
			LevelManager.lifeCount--;
			if(getLives() != -1)
				LevelManager.lifeCounter.getChildren().remove(getLives());
			if(getLives() <= 0)
				StateManager.gameState = State.GAMEOVER;
			else
				StateManager.gameState = State.YOUDIED;
		}
	}

	public void setWinACount(int count) {
		this.winAnimationCount = count;
	}

	public void animateWin() {
		setdx(0);
		if(winAnimationCount > 0) {
			if(winAnimationCount >= 509) {
				character.setTranslateX(winPlatX-225);
				hat.setTranslateX(winPlatX-225);
			}
			setdy(0);
			if(winAnimationCount % 15 == 0) {
				System.out.println(" should swap");
				swapDir();
			}
			if(gety()+character.getRadius() < LevelManager.groundLevel && !animatingWinG) {
				if(movingRight()) {
					character.setTranslateX(character.getTranslateX() + 3);
					hat.setTranslateX(hat.getTranslateX() + 3);
				}
				else {
					character.setTranslateX(character.getTranslateX() - 3);
					hat.setTranslateX(hat.getTranslateX() - 3);
				}
				character.setTranslateY(character.getTranslateY() + 2.5);
				hat.setTranslateY(hat.getTranslateY() + 2.5);
			}
			else {
				if(winAnimationCount == 40) {
					animatingWinG = true;
					Sounds.sPlayer.playSFX(0);
				}
				else if(winAnimationCount < 20) {
					character.setTranslateY(character.getTranslateY() + 2.5);
					hat.setTranslateY(hat.getTranslateY() + 2.5);
				}
				else if(winAnimationCount < 40) {
					character.setTranslateY(character.getTranslateY() - 2.5);
					hat.setTranslateY(hat.getTranslateY() - 2.5);
				}
			}
			winAnimationCount--;
		}
		else {
			if(StateManager.currentLevel == Level.END) {
				LevelManager.lifeCount = 3;
				StateManager.gameState = State.YOUWON;
				StateManager.currentLevel = Level.LEVEL1;
				LevelManager.levelOver();
			}
			else {
				StateManager.gameState = State.NEXTLEVEL;
				LevelManager.levelOver();
			}
		}
	}

	public void animateDeath() {
		if(deathAnimationCount >= 120) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()-0.5);
			this.getHat().setTranslateY(this.getHat().getTranslateY()-0.5);
			deathAnimationCount--;
		}
		else if(deathAnimationCount > 110) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()+8);
			deathAnimationCount--;
		}
		else if(deathAnimationCount > 0) {
			this.getCharacter().setTranslateY(this.getCharacter().getTranslateY()+8);
			this.getHat().setTranslateY(this.getHat().getTranslateY()+8);
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
				setx(predeathX);
				sety(predeathY);
				if(getLives() > 0) 
					StateManager.gameState = State.YOUDIED;
				else
					StateManager.gameState = State.GAMEOVER;
			}
		}
	}

	public void move() {
		super.move();
		hatPrevTranslateX = this.getHat().getTranslateX();
		hatPrevTranslateY = this.getHat().getTranslateY();
		hat.setTranslateX(this.hat.getTranslateX() + getdx() + getPlatdx());
		hat.setTranslateY(this.hat.getTranslateY() + getdy() + getPlatdy());
	}

	public Group getHat() {
		return this.hat;
	}

	public double getHatPrevTranslateX() {
		return hatPrevTranslateX;
	}

	public double getPrevHatTranslateY() {
		return hatPrevTranslateY;
	}

	public boolean isAnimating() {
		return animating;
	}

	public void setAnimating(boolean animating) {
		this.animating = animating;
	}

	public int getWinPlatX() {
		return this.winPlatX;
	}

	public void setWinPlatX(int x) {
		this.winPlatX = x;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getLives() {
		return this.lives;
	}

}
