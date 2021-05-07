package application.model;

import java.util.Random;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * PointBox obstacle class, an obstacle that gives the player points when they jump into the bottom of it. 
 * Contains getters/setters for location data, a method for animating it when hit, and a method for performing
 * actions when hit.
 * 
 * @author Thomas White
 *
 */
public class PointBox extends Obstacle {

	//Image and GUI variables
	private Image fullBox, emptyBox;
	private ImageView imageViewer;
	private Group displayImage;

	//Random generator for # of hits in the box
	private Random pointNum;

	//Animation variables
	private boolean animating = false;
	private int animationCount = 0;

	//Number of points in the box
	private int pointsLeft;

	/**
	 * Constructor for the PointBox, allocates width/height/color/image of the box, as well as number of hits left in the box (1-5)
	 *  
	 * @param w - width of the PointBox
	 * @param h - height of the PointBox
	 * @param c - color of the point box, should image loading fail
	 */
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

	/**
	 * Moves the back down once animation period is over.
	 */
	public void animateCube() {
		if(animationCount == 0) {
			this.getImageGroup().setTranslateY(this.getImageGroup().getTranslateY() + 5);
			animating = false;
		}
		else {
			animationCount--;
		}
	}

	/**
	 * Called when a player hits a PointBox, performing everything needed when a PointBox is hit. This includes
	 * playing a sound, animating the box, creating a float label, AND incrementing the players score while hits are left.
	 * When out of hits, the box turns gray and does nothing.
	 */
	public void getHit() {
		//While box has points
		if(this.pointsLeft >= 1) {
			//Play sound and increment player score
			Sounds.sPlayer.playSFX(2);
			Score.finalScore += 100;
			//Create a FloatLabel for the box
			FloatLabel scoreLabel = new FloatLabel("+100", 20, -20, this.getX(), this.getY());
			scoreLabel.setStartEndXY(scoreLabel.getTranslateX(), scoreLabel.getTranslateY());
			LevelManager.scoreLabels.add(scoreLabel);
			LevelManager.level.getChildren().add(LevelManager.scoreLabels.get(LevelManager.scoreLabels.size()-1));
			//If the box isn't currently animating, animate it (move it up, then down shortly after)
			if(!animating){
				this.animating = true;
				this.getImageGroup().setTranslateY(this.getImageGroup().getTranslateY() - 5);
				animationCount = 10;
			}
			this.pointsLeft--;
			//Set to gray when out of points
			if(this.pointsLeft == 0) 
				this.imageViewer.setImage(emptyBox);
		}
	}

	/**
	 * Return number of ticks for the box animation left
	 * @return int - number of ticks for the box animation left 
	 */
	public int getACount() {
		return this.animationCount;
	}

	/**
	 * Sets the x location of the PointBox
	 */
	public void setX(double x) {
		super.setX(x);
		imageViewer.setX(x);
	}

	/**
	 * Sets the y location of the PointBox
	 */
	public void setY(double y) {
		super.setY(y);
		imageViewer.setY(y);
	}

	/**
	 * Returns the ImageView of this PointBox
	 * @return - the ImageView of this PointBox
	 */
	public ImageView getImageViewer() {
		return this.imageViewer;
	}

	/**
	 * returns the Group containing the image of this PointBox
	 * @return - the Group containing the image of this PointBox
	 */
	public Group getImageGroup() {
		return this.displayImage;
	}

	/**
	 * Returns the number of hits left in this box
	 * @return - the number of hits left in this box
	 */
	public int getPointsLeft() {
		return this.pointsLeft;
	}

	/**
	 * Returns true if this box is currently animating
	 * @return - true if this box is currently animating 
	 */
	public boolean isAnimating() {
		return this.animating;
	}
	
}
