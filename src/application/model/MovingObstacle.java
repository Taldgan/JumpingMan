package application.model;

import javafx.scene.paint.Color;

public class MovingObstacle extends Obstacle {

	private double dx, dy, moveX, endX, startX, moved;
	private boolean moving = true;
	private boolean right = true;

	public MovingObstacle(int w, int h, Color c, int moveX) {
		super(w, h, c);
		this.moveX = moveX;
		this.moved = 0;
	}

	public MovingObstacle(int w, int h, Color c, Color cTop, int moveX) {
		super(w, h, c, cTop);
	}
	
	public double getdx() {
		return dx;
	}
	
	public void moveLeft() {
		setdx(-1);
	}

	public void moveRight() {
		setdx(1);
	}
	
	public void moveHoriz() {
		if(right) {
			setdx(1);
		}
		else {
			setdx(-1);
		}
	}
	
	public void moveUp() {
		setdy(-1);
	}

	public void moveDown() {
		setdy(1);
	}

	public void move() {
		moveHoriz();
		if(moved != endX-startX && moved != startX-endX) {
			moved += getdx();
			setX(getX() + getdx());
			setY(getY() + getdy());
		}
		else {
			moved = 0;
			swapDirection();
		}
	}
	
	public void swapDirection() {
		if(right)
			right = false;
		else
			right = true;
	}

	public void setdx(double dx) {
		this.dx = dx;
	}
	public double getdy() {
		return dy;
	}
	public void setdy(double dy) {
		this.dy = dy;
	}
	public boolean isMoving() {
		return this.moving;
	}
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	public void setStartEndX(int x) {
		this.startX = x;
		this.endX = startX+moveX;
	}
	public boolean movingRight() {
		return this.right;
	}

}
