package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Obstacle implements Collidable {
	
	private boolean collidable = true;
	private Rectangle platform, platTop;
	private double width, height, x, y, dx, dy;
	private boolean collided;
	private Color initColor;
	
	public Obstacle(int w, int h, Color c) {
		this.platform = new Rectangle(w, h, c);
		this.platTop = new Rectangle(w, h*0.2, c);
		this.width = w;
		this.height = h;
	}

	//2nd constructor for adding topPlat color
	public Obstacle(int w, int h, Color c, Color cTop) {
		this.platform = new Rectangle(w, h, c);
		this.platTop = new Rectangle(w, 5, cTop);
		this.width = w;
		this.height = h;
		this.initColor = c;
	}
	
	public boolean collide(double x, double y, double w, double h) {

		if(!collidable) 
			return false;
			
		if((x+w>= this.x && x-w <= this.x+width) && (y+h >= this.y && y-h <= this.y+height))
			return true;
		else 
			return false;
	}

	public void setCollidable(boolean isCollidable) {
		this.collidable = isCollidable;
	}
	
	public boolean hasCollided() {
		return collided;
	}
	
	public void setColliding() {
		this.collided = true;
	}

	public boolean isCollidable() {
		return collidable;
	}
	
	//Getters/Setters...
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;	
	}
	
	public Rectangle getPlat() {
		return this.platform;
	}
	

	public Rectangle getPlatTop() {
		return this.platTop;
	}
	
	public void setWidth(double w) {
		this.width = w;
	}

	public void setHeight(double h) {
		this.height = h;
	}

	public void setX(double x) {
		this.x = x;
		this.platform.setX(x);
		this.platTop.setX(x);
	}

	public void setY(double y) {
		this.y = y;
		this.platform.setY(y);
		this.platTop.setY(y);
	}
	public Color getColor() {
		return initColor;
	}
	public double getdx() {
		return this.dx;
	}
	public double getdy() {
		return this.dy;
	}
	public void setdx(double dx) {
		this.dx = dx;
	}
	public void setdy(double dy) {
		this.dy = dy;
	}




}
