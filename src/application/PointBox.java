package application;

import java.io.File;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class PointBox extends Obstacle {

	private int pointsLeft;
	private Image image;
	private ImageView imageViewer;
	private Group displayImage;

	public PointBox(int w, int h, Color c) {
		super(w, h, c);
		File test = new File("src/applications/images/pointBoxSprite.png");
		image = new Image(test.toURI().toString());
		imageViewer = new ImageView();
		imageViewer.setImage(image);
		displayImage = new Group(imageViewer);
	}
	
	public void setX(double x) {
		super.setX(x);
		imageViewer.setX(x);
	}

	public void setY(double y) {
		super.setY(y);
		imageViewer.setY(y);
	}

	public Image getImage() {
		return this.image;
	}

	public ImageView getImageViewer() {
		return this.imageViewer;
	}

	public Group getImageGroup() {
		return this.displayImage;
	}
}
