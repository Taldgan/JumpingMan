package application;

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PointBox extends Obstacle {

	private int pointsLeft;
	private Image fullBox, emptyBox;
	private ImageView imageViewer;
	private Group displayImage;

	public PointBox(int w, int h, Color c) {
		super(w, h, c);
		Random pointNum = new Random();
		fullBox = new Image("/application/images/pointBoxSprite.png");
		emptyBox = new Image("/application/images/pointBoxSpriteEmpty.png");
		imageViewer = new ImageView();
		imageViewer.setImage(fullBox);
		displayImage = new Group(imageViewer);
		pointsLeft = pointNum.nextInt(4)+1;
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
	
	public void getHit() {
		if(this.pointsLeft >= 1) {
			Sounds.sPlayer.playSFX(2);
			LevelManager.mainGuy.finalScore += 100;
			System.out.println("Score without time bonus: " + LevelManager.mainGuy.finalScore);
			this.pointsLeft--;
			if(this.pointsLeft == 0) 
				this.imageViewer.setImage(emptyBox);
		}
	}
}
