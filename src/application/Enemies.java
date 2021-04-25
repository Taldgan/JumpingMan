package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;
public class Enemies extends Character
{
	Circle enemy = new Circle(x, y, size, color);
	private double initialY;
	private boolean dir;
	public Enemies(double x, double y, double size, Color color) 
	{
		super(x, y, size, color);
		initialY = y;
		dir = true;
	}

	public double getInitY()
	{
		return initialY;
	}
	
	public void enemyMove()
	{
		/*this.character.setTranslateX(this.character.getTranslateX() + getdx());
		setx(this.character.getCenterX() + this.character.getTranslateX());
		this.character.setTranslateY(this.character.getTranslateY() + getdy());
		sety(this.character.getCenterY() + this.character.getTranslateY());*/
		
		//Move left if direction is true
		if(dir)
		{
			super.setdx(-.5);
			super.move();
		}
		else
		{
			setdx(.5);
			move();
		}
		//If collision with something, or I guess it reached the end of a platform, move to the right.
		
	}
	public void enemyJump()
	{
		if(!getJumping())
		{
			//System.out.println("Enemy jumping");
			setJumping(true);
			setdy(-3); //Up
		}
		
		/*else if(getJumping() && gety() > initialY)
		{
			//System.out.println("Enemy not jumping");
			setdy(0); //down
			setJumping(false);
		}*/
		//setJumping(false);
		
	}
	public int getRNG(int max)
	{
		Random rng = new Random();
		return rng.nextInt(max);
	}
	
	public void swapDir()
	{
		if(dir)
			dir = false;
		else
			dir = true;
	}

}
