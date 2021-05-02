package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Character {

	int lives;
	static Boolean dead;
	Boolean jumping, walking;
	double dx, dy;
	double x, y, minY = 280;
	double prevX, prevY, prevTranslateX, prevTranslateY;
	double size;
	Color color;
	Circle character = new Circle(x, y, size, color);
	boolean collide, collideRight, collideLeft;
	double groundLvl;
	double radius;
	private boolean dir;
	
	public Character(double x, double y, double size, Color color) {
		lives = 3;
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
	
	public void dead() {
		
		if (gety() > 800 || dead){
			System.out.println("dead called");
			setDead(true);
			setLives(getLives() - 1);
			if (getLives() < 0) {
				StateManager.gameState = State.GAMEOVER;
			} else {
				StateManager.gameState = State.YOUDIED;
			}
		}
	}
	
	public void move() {
		prevX = this.x;
		prevY = this.y;
		prevTranslateX = this.getCharacter().getTranslateX();
		prevTranslateY = this.getCharacter().getTranslateY();
		this.character.setTranslateX(this.character.getTranslateX() + getdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());
		// the background game objects are scrolled accordingly in the GameObject class
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
		//Set speed cap to 5 or -5
		if(dx > 5)
			dx = 5;
		else if(dx < -5)
			dx = -5;
		this.dx = dx;
	}
	public void setGroundLvl(double y)
	{
		groundLvl = y;
	}
	public double getdy() {
		return this.dy;
	}
	public void setdy(double dy) {
		this.dy = dy;
	}
	public void setJumping(boolean b){
		this.jumping = b;
	}
	public boolean getJumping(){
		return this.jumping;
	}
	public void setWalking(boolean b) {
		this.walking = b;
	}
	public boolean getWalking() {
		return this.walking;
	}

	public boolean getCollide()
	{
		return collide;
	}
	public void setCollide(boolean collide)
	{
		this.collide = collide;
	}
	public boolean getCollideLeft()
	{
		return collideLeft;
	}
	public void setCollideLeft(boolean collide)
	{
		this.collideLeft = collide;
	}
	public boolean getCollideRight()
	{
		return collideRight;
	}
	public void setCollideRight(boolean collide)
	{
		this.collideRight = collide;
	}
	public double getGroundLvl()
	{
		return groundLvl;
	}
	public double getMinY() {
		return this.minY;
	}
	public double getRadius()
	{
		return radius;
	}
	public void setRadius(double r)
	{
		radius = r;
	}
	public void swapDir()
	{
		if(dir)
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
		Character.dead = dead;
	}
	public Boolean getDead() {
		return Character.dead;
	}
}