package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Character {

	Boolean jumping, walking;
	double dx, dy;
	double x, y, minY = 280;
	double size;
	Color color;
	Circle character = new Circle(x, y, size, color);
	
	public Character(double x, double y, double size, Color color) {
		jumping = false;
		walking = false;
		setx(x);
		sety(y);
		setSize(size);
		setColor(color);
		setCharacter(x, y, size, color);
	}
	
	public void move() {
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
	public boolean getWalking(boolean b) {
		return this.walking;
	}

	public double getMinY() {
		return this.minY;
	}
}
