package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Enemies extends Character
{
	Circle enemy = new Circle(x, y, size, color);
	
	public Enemies(double x, double y, double size, Color color) 
	{
		super(x, y, size, color);
		// TODO Auto-generated constructor stub
	}
	
	public void enemyMove()
	{
		/*this.character.setTranslateX(this.character.getTranslateX() + getdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());*/
		
		//Move left
		super.setdx(-1);
		super.move();
		//If collision with something, or I guess it reached the end of a platform, move to the right.
		
	}

}
