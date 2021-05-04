package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class LevelManager {

	//Level/Object Size integers
	public static int groundLevel = 700;
	public static int tileWidth = 125;
	private static int pointBoxWidth = 40;
	private static int pointBoxHeight = 40;

	//Level Strings for Obstacle x/y locations in level
	static String groundString, lowerPlatString, upperPlatString, 
	movingPlatString, pointBoxString, enemyString; 

	//Group vars, for altering level object positions
	static Group ground, lowerPlatforms, upperPlatforms, movingPlatforms,
	level, pointBoxes, enemies;
	
	//List vars, lists of objects containing location data
	static ArrayList<Obstacle> groundList;
	static ArrayList<Integer> groundOffsets;
	static ArrayList<Obstacle> lowerPlatList;
	static ArrayList<Obstacle> upperPlatList;
	static ArrayList<PointBox> pointBoxList;
	static ArrayList<MovingObstacle> movingPlatList;
	static ArrayList<Obstacle> allStaticObjects;
	static ArrayList<MovingObstacle> allMovingObjects;
	static ArrayList<Enemies> enemyList;
	
	//Background
	static Rectangle background;

	//Main character variables
	private static int spawnX = 250, spawnY = LevelManager.groundLevel-25;
	static Character mainGuy;
	
	//Labels/Data
	static Label pauseLabel = new Label("PAUSED\n(Q)UIT");
	static Group lifeCounter;
	static Label infoLabel = new Label("Level: \nLives: ");
	
	//Colors
	private static Color bgColor, groundColor, grassColor, platColor, cloudColor;
	


	public static void loadLevel() {
		//Read in level data to strings
		System.out.println(StateManager.currentLevel.ordinal());
		try {
			//BufferedReader levelReader = new BufferedReader(new FileReader("src/application/levels/level" + StateManager.currentLevel.ordinal() + ".lvl"));
			BufferedReader levelReader = new BufferedReader(new FileReader("src/application/levels/test.lvl"));
			//Colors
			groundColor = Color.web(levelReader.readLine());
			grassColor = Color.web(levelReader.readLine());
			bgColor = Color.web(levelReader.readLine());
			cloudColor = Color.web(levelReader.readLine());
			platColor = Color.web(levelReader.readLine());
			//Level strings
			groundString = levelReader.readLine();
			lowerPlatString = levelReader.readLine();
			upperPlatString = levelReader.readLine();
			movingPlatString = levelReader.readLine();
			pointBoxString = levelReader.readLine();
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
		movingPlatList = new ArrayList<MovingObstacle>();
		pointBoxList = new ArrayList<PointBox>();
		allStaticObjects = new ArrayList<Obstacle>();
		allMovingObjects = new ArrayList<MovingObstacle>();
		enemyList = new ArrayList<Enemies>();
		

		mainGuy = new Character(spawnX, spawnY, 20, Color.RED);


		//Assign groups using spawn methods
		background = new Rectangle(groundString.length()*tileWidth, 5000, bgColor);
		ground = spawnGround(groundString, 0, 0, groundColor, grassColor, groundList);
		lowerPlatforms = spawnPlatforms(lowerPlatString,0,0, platColor, grassColor, lowerPlatList);
		upperPlatforms = spawnPlatforms(upperPlatString,0,0, platColor, grassColor, upperPlatList);
		movingPlatforms = spawnMovingPlatforms(movingPlatString, 0, 50, grassColor, movingPlatList);
		pointBoxes = spawnPointBoxes(pointBoxString, pointBoxWidth, pointBoxHeight, Color.web("0xF5E101"), pointBoxList);		
		enemies = spawnEnemies(enemyString, groundString);
		level = new Group(background, ground, lowerPlatforms, upperPlatforms, movingPlatforms, pointBoxes, enemies, mainGuy.getCharacter());
		level.setManaged(false);

		//Add all static object lists to allStaticObjects for easier collision
		allStaticObjects.addAll(groundList);
		allStaticObjects.addAll(lowerPlatList);
		allStaticObjects.addAll(upperPlatList);
		allStaticObjects.addAll(pointBoxList);

		//Add all moving object lists to allMovingObjects for moving collision
		allMovingObjects.addAll(movingPlatList);

		//Lastly, set labels
		pauseLabel.setTranslateY(LevelManager.groundLevel-400);
		pauseLabel.setFont(new Font("Blocky Font", 50));
		infoLabel.setTranslateY(LevelManager.groundLevel - 655);
		infoLabel.setFont(new Font("Blocky Font", 40));
		lifeCounter = new Group();
		for(int l = 0; l < mainGuy.getLives(); l++) {
			lifeCounter.getChildren().add(new Circle((l*(mainGuy.getRadius()*2))+15, 0, (mainGuy.getRadius()*2)/3, mainGuy.getColor()));
		}
		drawClouds();

	}
	
	private static void drawClouds() {
		Random gen = new Random();
		int cloudX = 200;
		int cloudY;
		Group cloud;
		Ellipse puff;
		for(int i = 0; i < groundString.length(); i+=3) {
			cloudY = 400 - gen.nextInt(200)-100;
			cloud = new Group();
			for(int j = 0; j < gen.nextInt(5)+4; j++) {
				puff = new Ellipse();
				puff.setCenterX(cloudX + (i*tileWidth) + (j*12) + 10 + gen.nextInt(10));
				puff.setCenterY(cloudY+ (7*(gen.nextInt(6)-3)));
				puff.setRadiusX(30);
				puff.setRadiusY(25);
				puff.setFill(cloudColor);
				cloud.getChildren().add(puff);
			}
			level.getChildren().add(cloud);	
		}
		
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
	
	private static Group spawnMovingPlatforms(String lvl, int offsetX, int offsetY, Color c, ArrayList<MovingObstacle> pList) {
		Group platG = new Group();
		double groundLvlOffset = groundLevel - 45;
		for(int x = 0; x < lvl.length()-1;x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				//Spawn platform based off of char's location in string
				//Each char will be 90 pixels of space, and will spawn at a height of 265-(y*45)
				//charAt(x) determines offset of platform, charAt(x+1) determines how far to move before returning 

				MovingObstacle r = new MovingObstacle((int) (tileWidth),25,c, Integer.parseInt(String.valueOf(lvl.charAt((x+1))))*tileWidth); //Platforms are 90x25
				pList.add(r);
				platG.getChildren().add(r.getPlat());
				platG.getChildren().add(r.getPlatTop());

				r.setX(tileWidth*x+offsetX);
				r.setStartEndX(tileWidth*x+offsetX);
				r.setY(groundLvlOffset-groundOffsets.get(x)+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1-offsetY);
				x++;
			}
		}
		return platG;
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

	private static Group spawnPointBoxes(String lvl, int sizeX, int sizeY, Color c, ArrayList<PointBox> oList)
	{
		Group pBoxGroup = new Group();
		for(int x = 0; x < lvl. length(); x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				PointBox pBox = new PointBox(sizeX,sizeY, c);
				pBox.setX(tileWidth*x);
				pBox.setY(groundLevel-groundOffsets.get(x)-(Integer.parseInt(String.valueOf(lvl.charAt(x))))*sizeY);
				oList.add(pBox);
				pBoxGroup.getChildren().add(pBox.getPlat());
				pBoxGroup.getChildren().add(pBox.getImageGroup());
			}
		}
		return pBoxGroup;
	}
}
