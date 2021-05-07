package application.model;

import java.util.Random;
import javafx.scene.text.Font;

import javafx.scene.control.Label;

public class FloatLabel extends Label {

	private double moveX, moveY, startX, endX, startY, endY, movedX, movedY;
	private boolean remove = false;
	private Random randomXY;

	public FloatLabel(String text, double moveX, double moveY, double translateX, double translateY) {
		super(text);
		randomXY = new Random();
		this.moveX = moveX;
		this.moveY = moveY;
		this.setFont(new Font("Blocky Font", 30));
		this.setTranslateX(translateX + randomXY.nextInt(60)-30);
		this.setTranslateY(translateY + randomXY.nextInt(60)-30);
	}
	
	public FloatLabel(String text, double moveX, double moveY, double translateX, double translateY, int fontSize) {
		super(text);
		randomXY = new Random();
		this.moveX = moveX;
		this.moveY = moveY;
		this.setFont(new Font("Blocky Font", fontSize));
		this.setTranslateX(translateX + randomXY.nextInt(60)-30);
		this.setTranslateY(translateY + randomXY.nextInt(60)-30);
	}


	public void setStartEndXY(double startX, double startY) {
		this.startX = startX;
		this.endX = this.startX+this.moveX;
		this.startY = startY;
		this.endY = this.startY+this.moveY;
	}

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

	public boolean needToRemove() {
		return this.remove;
	}
}

