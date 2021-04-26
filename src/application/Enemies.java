package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;
public class Enemies extends Character implements Collidable
{
	Circle enemy = new Circle(x, y, size, color);
	private double initialY;
	private boolean dir;
	private boolean isCollidable;
	private Color initColor;
	private double initialX;
	public Enemies(double x, double y, double size, Color color) 
	{
		super(x, y, size, color);
		initialY = y;
		initialX = x;
		dir = true;
		isCollidable = true;
		initColor = color;
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
		if(gety() > 300)
		{
			setJumping(false);
			setdy(0);
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
	
	public boolean collide(double x, double y, double w, double h)
	{
		if((getx() - radius <= x && getx()+radius >= x) && (gety()+radius >= y && gety()-radius <= y))
			return true;
		else
			return false;
	}
	public void setCollidable(boolean isCollidable)
	{
		this.isCollidable = isCollidable;
	}
	public boolean isCollidable()
	{
		return true;
	}
	public Color getColor()
	{
		return color;
	}
	public double getInitialX()
	{
		return initialX;
	}
	public void setInitialX(double x)
	{
		initialX = x;
	}

}
