package application;

import javafx.scene.paint.Color;

public class MovingObstacle extends Obstacle {

	private double dx, dy;

	public MovingObstacle(int w, int h, Color c) {
		super(w, h, c);

	}

	public MovingObstacle(int w, int h, Color c, Color cTop) {
		super(w, h, c, cTop);
	}
	
	public double getdx() {
		return dx;
	}
	
	public void moveLeft() {
		setdx(-2);
	}

	public void moveRight() {
		setdx(2);
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
	
		
	

}
