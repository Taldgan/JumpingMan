package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class LevelManager {

	//Level/Object Size integers
	public static int groundLevel = 700;
	public static int tileWidth = 125;
	private static int obstacleWidth = 50;
	private static int obstacleHeight = 50;

	//Level Strings for Obstacle x/y locations in level
	static String groundString, lowerPlatString, upperPlatString, 
	obstacleString, enemyString; 

	//Group vars, for altering level object positions
	static Group ground, lowerPlatforms, upperPlatforms, level, obstacles, enemies;
	
	//List vars, lists of objects containing location data
	static ArrayList<Obstacle> groundList;
	static ArrayList<Integer> groundOffsets;
	static ArrayList<Obstacle> lowerPlatList;
	static ArrayList<Obstacle> upperPlatList;
	static ArrayList<Obstacle> obstacleList;
	static ArrayList<Obstacle> allObjects;
	static ArrayList<Enemies> enemyList;
	
	//Void and Background
	static Rectangle background;

	//Main character variables
	private static int spawnX = 250, spawnY = LevelManager.groundLevel-25;
	static Character mainGuy;
	
	//Labels
	static Label pauseLabel = new Label("PAUSED\n(Q)UIT");
	static Label livesRemaining = new Label("Lives ");


	public static void loadLevel() {
		//Read in level data to strings
		System.out.println(StateManager.currentLevel.ordinal());
		try {
			BufferedReader levelReader = new BufferedReader(new FileReader("src/application/levels/level" + StateManager.currentLevel.ordinal() + ".lvl"));
			groundString = levelReader.readLine();
			lowerPlatString = levelReader.readLine();
			upperPlatString = levelReader.readLine();
			obstacleString = levelReader.readLine();
			enemyString = levelReader.readLine();
			levelReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Level Load failed");
			System.exit(1);
		}
		//Initialize lists, main character
		groundList = new ArrayList<Obstacle>();
		groundOffsets = new ArrayList<Integer>();
		lowerPlatList = new ArrayList<Obstacle>();
		upperPlatList = new ArrayList<Obstacle>();
		obstacleList = new ArrayList<Obstacle>();
		allObjects = new ArrayList<Obstacle>();
		enemyList = new ArrayList<Enemies>();

		mainGuy = new Character(spawnX, spawnY, 20, Color.RED);


		//Assign groups using spawn methods
		background = new Rectangle(groundString.length()*tileWidth, 5000, Color.LIGHTSKYBLUE);
		ground = spawnGround(groundString, 0, 0,  Color.SADDLEBROWN, Color.GREEN, groundList);
		lowerPlatforms = spawnPlatforms(lowerPlatString,0,0,Color.SADDLEBROWN, Color.GREEN, lowerPlatList);
		upperPlatforms = spawnPlatforms(upperPlatString,0,0,Color.SADDLEBROWN, Color.GREEN, lowerPlatList);
		obstacles = spawnObstacles(obstacleString, obstacleWidth, obstacleHeight, Color.DARKGREEN, obstacleList);
		enemies = spawnEnemies(enemyString, groundString);
		level = new Group(background, ground, lowerPlatforms, upperPlatforms, obstacles, enemies, mainGuy.getCharacter());
		level.setManaged(false);

		//Add all object lists to allObjects for easier collision
		allObjects.addAll(groundList);
		allObjects.addAll(lowerPlatList);
		allObjects.addAll(upperPlatList);
		allObjects.addAll(obstacleList);
		
		//Lastly, set labels
		pauseLabel.setTranslateY(LevelManager.groundLevel-400);
		pauseLabel.setFont(new Font("Blocky Font", 50));
		livesRemaining.setTranslateY(LevelManager.groundLevel - 700);
		livesRemaining.setFont(new Font("Blocky Font", 40));

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
