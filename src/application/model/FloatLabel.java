package application.model;

import java.util.Random;
import javafx.scene.text.Font;
import javafx.scene.control.Label;

/**
 * Creates a text Label that moves in a direction, and marks itself for removal once it has traveled a set distance.
 * @author Thomas White
 *
 */
public class FloatLabel extends Label {

	//Position variables
	private double moveX, moveY, startX, endX, startY, endY, movedX, movedY;
	private boolean remove = false;
	//Some spice for initial position
	private Random randomXY;

	/**
	 * Constructor for FloatLabel, initializes text, x/y distances to move, and initial x/y position
	 * @param text - text the FloatLabel contains
	 * @param moveX - the distance for the FloatLabel to move in the x direction
	 * @param moveY - the distance for the FloatLabel to move in the y direction
	 * @param translateX - initial translate x position
	 * @param translateY - initial translate y position
	 */
	public FloatLabel(String text, double moveX, double moveY, double translateX, double translateY) {
		super(text);
		randomXY = new Random();
		this.moveX = moveX;
		this.moveY = moveY;
		this.setFont(new Font("Blocky Font", 30));
		this.setTranslateX(translateX + randomXY.nextInt(60)-30);
		this.setTranslateY(translateY + randomXY.nextInt(60)-30);
	}
	
	/**
	 * Constructor for FloatLabel, initializes text, x/y distances to move, initial x/y position, and font size of the text
	 * @param text - text the FloatLabel contains
	 * @param moveX - the distance for the FloatLabel to move in the x direction
	 * @param moveY - the distance for the FloatLabel to move in the y direction
	 * @param translateX - initial translate x position
	 * @param translateY - initial translate y position
	 * @param fontSize - the font size for the Label's text
	 */
	public FloatLabel(String text, double moveX, double moveY, double translateX, double translateY, int fontSize) {
		super(text);
		randomXY = new Random();
		this.moveX = moveX;
		this.moveY = moveY;
		this.setFont(new Font("Blocky Font", fontSize));
		this.setTranslateX(translateX + randomXY.nextInt(60)-30);
		this.setTranslateY(translateY + randomXY.nextInt(60)-30);
	}

	/**
	 * Sets initial and ending x and y positions for this FloatLabel
	 * @param startX
	 * @param startY
	 */
	public void setStartEndXY(double startX, double startY) {
		this.startX = startX;
		this.endX = this.startX+this.moveX;
		this.startY = startY;
		this.endY = this.startY+this.moveY;
	}

	/**
	 * Moves the float label in the x and y directions while they haven't reached their endpoint. Marks for removal if both x & y are reached.
	 */
	public void move() {
		if(movedX != endX-startX) {
			this.setTranslateX(this.getTranslateX() + 1);
			movedX++;
		}
		if(movedY != endY-startY) {
			this.setTranslateY(this.getTranslateY() - 1);
			movedY--;
		}
		if(movedX == endX-startX && movedY == endY-startY)
			this.remove = true;
	}

	/**
	 * Returns true if this FloatLabel is marked for removal
	 * @return boolean - true if this FloatLabel is marked for removal
	 */
	public boolean needToRemove() {
		return this.remove;
	}
}

