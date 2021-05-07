package application.model;

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class PointBox extends Obstacle {

	private int pointsLeft;
	private Image fullBox, emptyBox;
	private ImageView imageViewer;
	private Group displayImage;
	private Random pointNum;
	private boolean animating = false;
	private int animationCount = 0;

	public PointBox(int w, int h, Color c) {
		super(w, h, c);
		pointNum = new Random();
		fullBox = new Image("/resources/images/pointBoxSprite.png");
		emptyBox = new Image("/resources/images/pointBoxSpriteEmpty.png");
		imageViewer = new ImageView();
		imageViewer.setImage(fullBox);
		displayImage = new Group(imageViewer);
		pointsLeft = pointNum.nextInt(4)+1;
	}

	public void animateCube() {
		if(animationCount == 0) {
			this.getImageGroup().setTranslateY(this.getImageGroup().getTranslateY() + 5);
			animating = false;
		}
		else {
			animationCount--;
		}
	}

	public void getHit() {
		if(this.pointsLeft >= 1) {
			Sounds.sPlayer.playSFX(2);
			Score.finalScore += 100;
			System.out.println("Score without time bonus: " + Score.finalScore);
			//create floatLabel and add it
			FloatLabel scoreLabel = new FloatLabel("+100", 20, -20, this.getX(), this.getY());
			scoreLabel.setStartEndXY(scoreLabel.getTranslateX(), scoreLabel.getTranslateY());
			LevelManager.scoreLabels.add(scoreLabel);
			LevelManager.level.getChildren().add(LevelManager.scoreLabels.get(LevelManager.scoreLabels.size()-1));
			if(!animating){
				this.animating = true;
				this.getImageGroup().setTranslateY(this.getImageGroup().getTranslateY() - 5);
				animationCount = 10;
			}

			this.pointsLeft--;

			if(this.pointsLeft == 0) 
				this.imageViewer.setImage(emptyBox);
		}
	}

	public int getACount() {
		return this.animationCount;
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

	public boolean isAnimating() {
		return this.animating;
	}

	
}
