package application;

import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PointBox extends Obstacle {

	private int pointsLeft;
	private Image fullBox, emptyBox;
	private ImageView imageViewer;
	private Group displayImage;
	private ArrayList<FloatLabel> scoreLabels;
	private Random pointNum;

	public PointBox(int w, int h, Color c) {
		super(w, h, c);
		pointNum = new Random();
		fullBox = new Image("/application/images/pointBoxSprite.png");
		emptyBox = new Image("/application/images/pointBoxSpriteEmpty.png");
		imageViewer = new ImageView();
		imageViewer.setImage(fullBox);
		displayImage = new Group(imageViewer);
		pointsLeft = pointNum.nextInt(4)+1;
		scoreLabels = new ArrayList<FloatLabel>();
	}
	
	public void setX(double x) {
		super.setX(x);
		imageViewer.setX(x);
	}

	public void setY(double y) {
		super.setY(y);
		imageViewer.setY(y);
	}

	public ImageView getImageViewer() {
		return this.imageViewer;
	}

	public Group getImageGroup() {
		return this.displayImage;
	}
	
	public int getPointsLeft() {
		return this.pointsLeft;
	}
	
	public void floatLabels() {
		for(FloatLabel scoreLabel : scoreLabels) {
			scoreLabel.move();
			if(scoreLabel.needToRemove()) {
				this.getImageGroup().getChildren().remove(scoreLabel);
			}
		}
	}

	public void getHit() {
		if(this.pointsLeft >= 1) {
			Sounds.sPlayer.playSFX(2);
			LevelManager.mainGuy.finalScore += 100;
			System.out.println("Score without time bonus: " + LevelManager.mainGuy.finalScore);
			FloatLabel scoreLabel = new FloatLabel("+100", 15, -20);
			scoreLabel.setFont(new Font("Blocky Font", 30));
			scoreLabel.setTranslateX(this.getX() + this.pointNum.nextInt(60)-30);
			scoreLabel.setTranslateY(this.getY() + this.pointNum.nextInt(30)-60);
			scoreLabel.setStartEndXY(scoreLabel.getTranslateX(), scoreLabel.getTranslateY());
			scoreLabels.add(scoreLabel);
			getImageGroup().getChildren().add(scoreLabels.get(scoreLabels.size()-1));
			this.pointsLeft--;
			if(this.pointsLeft == 0) 
				this.imageViewer.setImage(emptyBox);
		}
	}
	
	class FloatLabel extends Label {

		private double moveX, moveY, startX, endX, startY, endY, movedX, movedY;
		private boolean remove = false;

		public FloatLabel(String text, double moveX, double moveY) {
			super(text);
			this.moveX = moveX;
			this.moveY = moveY;
		}
		
		public void setStartEndXY(double startX, double startY) {
			this.startX = startX;
			this.endX = this.startX+this.moveX;
			this.startY = startY;
			this.endY = this.startY+this.moveY;
		}

		public void move() {
			if(movedX != endX-startX && movedY != endY-startY) {
				this.setTranslateX(this.getTranslateX() + 1);
				movedX++;
				this.setTranslateY(this.getTranslateY() - 1);
				movedY--;
			}
			else
				this.remove = true;
		}
		
		public boolean needToRemove() {
			return this.remove;
		}
	}
}
