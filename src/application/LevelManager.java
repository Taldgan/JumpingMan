package application;

import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.paint.Color;

public class LevelManager {

	//Level/Object Size integers
	private static int groundLevel = 700;
	private static int tileWidth = 125;
	private static int obstacleWidth = 50;
	private static int obstacleHeight = 50;

	//Level Strings for Obstacle x/y locations in level
	private static String groundString, lowerPlatString, upperPlatString, 
	obstacleString, enemyString; 

	//Group vars, for altering level object positions
	static Group ground, lowerPlatforms, upperPlatforms, level, obstacles, enemies;
	
	//List vars, lists of objects containing location data
	static ArrayList<Obstacle> groundList = new ArrayList<Obstacle>();
	static ArrayList<Integer> groundOffsets;
	static ArrayList<Obstacle> lowerPlatList = new ArrayList<Obstacle>();
	static ArrayList<Obstacle> upperPlatList = new ArrayList<Obstacle>();
	static ArrayList<Obstacle> obstacleList = new ArrayList<Obstacle>();
	static ArrayList<Enemies> enemyList = new ArrayList<Enemies>();

	
	public static void loadLevel() {
		Scanner levelReader = new Scanner("levels/" + StateManager.currLevel + ".lvl");
		groundString = levelReader.nextLine();
		lowerPlatString = levelReader.nextLine();
		upperPlatString = levelReader.nextLine();
		obstacleString = levelReader.nextLine();
		enemyString = levelReader.nextLine();
		levelReader.close();
		ground = spawnGround(groundString, 0, 0,  Color.SADDLEBROWN, Color.GREEN, groundList);
		lowerPlatforms = spawnPlatforms(lowerPlatString,0,0,Color.SADDLEBROWN, Color.GREEN, lowerPlatList);
		upperPlatforms = spawnPlatforms(upperPlatString,0,0,Color.SADDLEBROWN, Color.GREEN, lowerPlatList);
		obstacles = spawnObstacles(obstacleString, obstacleWidth, obstacleHeight, Color.DARKGREEN, obstacleList);
		enemies = spawnEnemies(enemyString, groundString);
		level = new Group(ground, lowerPlatforms, upperPlatforms, obstacles, enemies);
	}

	private static Group spawnEnemies(String eSet, String groundSet)
	{
		//1 is a normal enemy
		//2 is a jumping enemy
		//3 Is a normal enemy on a platform
		Group enemyGroup = new Group();
		int groundOffset;
		int spawnOffset = 10;
		for(int x = 0; x < eSet.length(); x++)
		{
			groundOffset = groundOffsets.get(x);
			if(eSet.charAt(x) == '1')
			{
				enemyList.add(new Enemies(x*tileWidth+spawnOffset,groundLevel-groundOffset-20,20,Color.MAGENTA, groundSet.charAt(x),groundLevel));
			}
			else if(eSet.charAt(x) == '2')
			{
				enemyList.add(new Enemies(x*tileWidth+spawnOffset,groundLevel-groundOffset-20,20,Color.BLUE, groundSet.charAt(x),groundLevel));
			}
			else if(eSet.charAt(x) >= '3' && eSet.charAt(x) <= '9') {
				enemyList.add(new Enemies((x+1)*tileWidth,
						groundLevel-groundOffset-20-45-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA,
						groundSet.charAt(x),groundLevel));
			}

			//Starting at 3, spawn enemy on platforms. To match the platform height, multiply the string value-2 by 45 and subtract that by
			//The ground level, groundLevel, groundLevel offset groundOffsets.get(x). 
			//Finally, substract in an offset of 20 to account for the circle's bottom.
			//enemyList.add(new Enemies((1+x)*tileWidth,
			//groundLevel-groundOffset-(45+20)-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA));
			//}
		}
		for(int x = 0; x < enemyList.size(); x++)
		{
			enemyGroup.getChildren().add(enemyList.get(x).getCharacter());
		}

		return enemyGroup;
	}

	private static Group spawnGround(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> gList) 
	{
		int gOffset;
		Group groundG = new Group();
		for(int i = 0; i < lvl.length(); i++) {
			gOffset = 0;
			if(lvl.charAt(i) != '0') {
				Obstacle g = new Obstacle(tileWidth, groundLevel-50, c, cTop);
				gList.add(g);
				groundG.getChildren().add(g.getPlat());
				groundG.getChildren().add(g.getPlatTop());

				g.setX(tileWidth*i);
				int offsetVal = Integer.parseInt(String.valueOf(lvl.charAt(i)));
				if(offsetVal > 1) {
					gOffset = 60*Integer.parseInt(String.valueOf(lvl.charAt(i)));
					g.setY(groundLevel-gOffset);
				}
				else
					g.setY(groundLevel);
			}
			groundOffsets.add(gOffset);
		}
		return groundG;

	}

	private static Group spawnPlatforms(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> pList) 
	{
		//lvl string: 
		//0's mean no platform, and then the varying numbers mean a platform will spawn at that level of height.
		//Their position in the string will correlate to how far into the level they spawn. See below for the formula I used.
		//Feel free to change whatever.
		Group platG = new Group();
		double groundLvlOffset = groundLevel-45;
		for(int x = 0; x < lvl.length();x++)
		{
			if(lvl.charAt(x) != '0' && lvl.charAt(x) != '9') //If the current char is not 0, create a platform in that spot.
			{
				//Spawn platform based off of char's location in string
				//Each char will be 90 pixels of space, and will spawn at a height of 265-(y*45)

				Obstacle r = new Obstacle((int) (tileWidth),25,c, cTop); //Platforms are 90x25
				pList.add(r);
				platG.getChildren().add(r.getPlat());
				platG.getChildren().add(r.getPlatTop());

				r.setX(tileWidth*x+offsetX);
				r.setY(groundLvlOffset-groundOffsets.get(x)+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1-offsetY);
			}
			else if(lvl.charAt(x) == '9') { //For use for the victory platform
				int width = tileWidth;
				int height = 20;
				Obstacle r = new Obstacle(width, height, Color.DARKSLATEGRAY, cTop);
				pList.add(r);
				platG.getChildren().add(r.getPlat());

				r.setX(tileWidth*x);
				r.setY(groundLvlOffset-groundOffsets.get(x)-45);
			}
		}
		return platG;
	}

	private static Group spawnObstacles(String lvl, int sizeX, int sizeY, Color c, ArrayList<Obstacle> oList)
	{
		Group obsGroup = new Group();
		for(int x = 0; x < lvl. length(); x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				Obstacle o = new Obstacle(sizeX,sizeY, c);
				o.setX(tileWidth*x);
				o.setY(groundLevel-groundOffsets.get(x)-(Integer.parseInt(String.valueOf(lvl.charAt(x))))*sizeY);
				oList.add(o);
				obsGroup.getChildren().add(o.getPlat());
			}
		}
		return obsGroup;
	}
}
