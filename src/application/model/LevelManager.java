package application.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * Statically manages all of the level-related objects for the game. This includes lists of all game obstacles and characters, 
 * the String data for levels, and all of their Groups. Also includes methods for reading in level data and spawning the 
 * associated level objects from the files.
 * 
 * @author Thomas White, Caleb Kopecky, Gabriel Pastelero
 */
public class LevelManager {
	
	//Score counting object
	static Score score = new Score();

	//Level/Object Size integers
	public static int groundLevel = 700;
	public static int tileWidth = 125;
	private static int pointBoxWidth = 40;
	private static int pointBoxHeight = 40;

	//Level Strings for Obstacle x/y locations in level
	public static String groundString;
	public static String lowerPlatString;
	public static String upperPlatString;
	public static String movingPlatString;
	public static String pointBoxString;
	public static String lowerEnemyString;
	public static String upperEnemyString; 

	//Group vars, for altering level object positions
	static Group ground, lowerPlatforms, upperPlatforms, movingPlatforms;

	public static Group level;

	static Group pointBoxes;

	static Group enemies;
	
	//List vars, lists of objects containing location data
	public static ArrayList<Obstacle> groundList;
	public static ArrayList<Integer> groundOffsets;
	public static ArrayList<Obstacle> lowerPlatList;
	public static ArrayList<Obstacle> upperPlatList;
	public static ArrayList<PointBox> pointBoxList;
	public static ArrayList<MovingObstacle> movingPlatList;
	public static ArrayList<Obstacle> allStaticObjects;
	public static ArrayList<MovingObstacle> allMovingObjects;
	public static ArrayList<Enemies> enemyList;
	public static ArrayList<FloatLabel> scoreLabels;
	
	//Background
	static Rectangle background;
	static Rectangle theVoid;

	//Main character variables
	private static int spawnX = 250, spawnY = LevelManager.groundLevel-20;
	public static MainCharacter mainGuy;
	
	//Labels/Data
	public static Label pauseLabel = new Label("PAUSED\n(Q)UIT");
	public static Group lifeCounter;
	public static Label infoLabel = new Label("Level: \nLives: \nScore: ");
	public static int lifeCount = 3;
	
	//Colors
	private static Color bgColor, groundColor, grassColor, platColor, cloudColor;
	
	/**
	 * Move all FloatLabels, and remove them if they are marked for removal
	 */
	public static void floatLabels() {
		ArrayList<FloatLabel> iterList = new ArrayList<FloatLabel>();
		iterList.addAll(scoreLabels);
		for(FloatLabel scoreLabel : iterList) {
			scoreLabel.move();
			if(scoreLabel.needToRemove()) {
				level.getChildren().remove(scoreLabel);
				scoreLabels.remove(scoreLabel);
			}
		}
	}

	/**
	 * Calculate final score for the level
	 */
	public static void levelOver() {
		Score.stop();
		Score.finalScore += score.calculateTimeScore();
	}

	/**
	 * Reset final score to 0 for losing game
	 */
	public static void gameOver() {
		loadLevel();
		Score.finalScore = 0;
	}
	
	/**
	 * Re-initializes all relevant level data. Level data is read in from the level files (determining file based off of the StateManager's declared 'currentLevel'.
	 * The level strings are then read in, and platforms/enemies/pointboxes etc. are spawned in based off of those strings. The main character and other items are 
	 * also spawned. 
	 */
	public static void loadLevel() {
		
		Score.start();		
		//Read in level data to strings
		try {
			URL levelsURL = LevelManager.class.getClass().getResource("/resources/levels/level" + StateManager.currentLevel.ordinal() + ".lvl");
			BufferedReader levelReader = new BufferedReader(new FileReader(new File(levelsURL.getPath())));
			//Colors
			//Read comment in
			levelReader.readLine();
			groundColor = Color.web(levelReader.readLine());
			grassColor = Color.web(levelReader.readLine());
			bgColor = Color.web(levelReader.readLine());
			cloudColor = Color.web(levelReader.readLine());
			platColor = Color.web(levelReader.readLine());
			//Level strings
			//Read 2 comments in
			levelReader.readLine();
			levelReader.readLine();
			groundString = levelReader.readLine();
			lowerPlatString = levelReader.readLine();
			upperPlatString = levelReader.readLine();
			movingPlatString = levelReader.readLine();
			pointBoxString = levelReader.readLine();
			lowerEnemyString = levelReader.readLine();
			upperEnemyString = levelReader.readLine();
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
		scoreLabels = new ArrayList<FloatLabel>();
		int upperOffset = 10;

		//Spawn main character
		mainGuy = new MainCharacter(spawnX, spawnY, 20, Color.RED, lifeCount);


		//Assign groups using spawn methods
		background = new Rectangle(groundString.length()*tileWidth, 5000, bgColor);
		theVoid = new Rectangle(groundString.length()*tileWidth*2, 8000, groundColor);
		theVoid.setTranslateX(theVoid.getTranslateX()-2000);
		ground = spawnGround(groundString, 0, 0, groundColor, grassColor, groundList);
		lowerPlatforms = spawnPlatforms(lowerPlatString,0,0, platColor, grassColor, lowerPlatList);
		upperPlatforms = spawnPlatforms(upperPlatString,0, upperOffset, platColor, grassColor, upperPlatList);
		movingPlatforms = spawnMovingPlatforms(movingPlatString, 0, 50, grassColor, movingPlatList);
		pointBoxes = spawnPointBoxes(pointBoxString, pointBoxWidth, pointBoxHeight, Color.web("0xF5E101"), pointBoxList);		
		enemies = spawnEnemies(lowerEnemyString, groundString, 0);
		enemies.getChildren().add(spawnEnemies(upperEnemyString, groundString, upperOffset));
		level = new Group(theVoid, background, drawClouds(), ground, lowerPlatforms, upperPlatforms, movingPlatforms, pointBoxes, enemies, mainGuy.getCharacter(), mainGuy.getHat());
		level.setManaged(false);

		//Add all static object lists to allStaticObjects for easier collision
		allStaticObjects.addAll(groundList);
		allStaticObjects.addAll(lowerPlatList);
		allStaticObjects.addAll(upperPlatList);
		allStaticObjects.addAll(pointBoxList);

		//Add all moving object lists to allMovingObjects for moving collision
		allMovingObjects.addAll(movingPlatList);

		//Set labels
		pauseLabel.setTranslateY(LevelManager.groundLevel-400);
		pauseLabel.setFont(new Font("Blocky Font", 50));
		infoLabel.setTranslateY(LevelManager.groundLevel - 655);
		infoLabel.setFont(new Font("Blocky Font", 40));

		//Lastly, re-create the graphical life counter
		lifeCounter = new Group();
		for(int l = 0; l < mainGuy.getLives(); l++) {
			lifeCounter.getChildren().add(new Circle((l*(mainGuy.getRadius()*2))+15, 0, (mainGuy.getRadius()*2)/3, mainGuy.getColor()));
		}
	}
	
	/**
	 * Dynamically generates a group of clouds. Shape and y location of the clouds are randomized, and the group is returned.
	 * @return Group - the Group object containing all of the clouds
	 */
	private static Group drawClouds() {
		Random gen = new Random();
		int cloudX = 200;
		int cloudY;
		Group cloud, clouds = new Group();;
		Ellipse puff;
		//For loop to create a cloud and add it to 'clouds' group
		for(int i = 0; i < groundString.length(); i+=3) {
			cloudY = 400 - gen.nextInt(200)-100;
			cloud = new Group();
			//For loop to randomly assign puffs and assign them to a cloud
			for(int j = 0; j < gen.nextInt(5)+4; j++) {
				puff = new Ellipse();
				puff.setCenterX(cloudX + (i*tileWidth) + (j*12) + 10 + gen.nextInt(10));
				puff.setCenterY(cloudY+ (7*(gen.nextInt(6)-3)));
				puff.setRadiusX(30);
				puff.setRadiusY(25);
				puff.setFill(cloudColor);
				cloud.getChildren().add(puff);
			}
			clouds.getChildren().add(cloud);	
		}
		return clouds;
		
	}

	/**
	 * 
	 * @param eSet - the String of enemy data, determines type and location of enemy spawn
	 * @param groundSet - the String of ground data, determines zone code of enemy
	 * @param upperOffset - upper platform offset, for enemies spawning on upper platforms
	 * @return Group - returns the newly created Group of enemies
	 */
	private static Group spawnEnemies(String eSet, String groundSet, int upperOffset)
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
				enemyList.add(new Enemies(x*tileWidth+spawnOffset,groundLevel-groundOffset-20-upperOffset,20,Color.MAGENTA, groundSet.charAt(x),groundLevel));
			}
			else if(eSet.charAt(x) == '2')
			{
				enemyList.add(new Enemies(x*tileWidth+spawnOffset,groundLevel-groundOffset-20-upperOffset,20,Color.BLUE, groundSet.charAt(x),groundLevel));
			}
			else if(eSet.charAt(x) >= '3' && eSet.charAt(x) <= '9') {
				enemyList.add(new Enemies((x+1)*tileWidth,
						groundLevel-groundOffset-20-upperOffset-45-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA,
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

	/**
	 *  Spawns ground obstacles, and fill ground/ground offset ArrayLists. Returns the group of ground objects.
	 * @param lvl - the level String determining where and at what height to spawn the ground
	 * @param offsetX - x offset for ground
	 * @param offsetY - y offset for ground
	 * @param c - 'ground' color of the ground
	 * @param cTop - 'grass' color of the ground
	 * @param gList - the list of Obstacles to add the ground to
	 * @return Group - returns the Group of newly created ground obstacles
	 */
	private static Group spawnGround(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> gList) 
	{
		int gOffset;
		Group groundG = new Group();
		for(int i = 0; i < lvl.length(); i++) {
			gOffset = 0;
			//0's mean a hole in the ground
			if(lvl.charAt(i) != '0') {
				Obstacle g = new Obstacle(tileWidth, groundLevel-50, c, cTop);
				gList.add(g);
				groundG.getChildren().add(g.getPlat());
				groundG.getChildren().add(g.getPlatTop());

				g.setX(tileWidth*i);
				int offsetVal = Integer.parseInt(String.valueOf(lvl.charAt(i)));
				//Higher than 1 means an raised height/offset of 60*character value
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
	
	/**
	 *  Spawns moving platform obstacles, and fills the moving platforms ArrayList. Returns the group of newly created moving platforms.
	 * @param lvl - the data String determining where, what height, and how far the moving platform will move
	 * @param offsetX - x offset for the moving platforms
	 * @param offsetY - y offset for the moving platforms
	 * @param c - color of the moving platforms
	 * @param pList - the list of MovingObstacles to add the moving platforms to
	 * @return Group - returns the Group of newly created moving platforms
	 */ 
	private static Group spawnMovingPlatforms(String lvl, int offsetX, int offsetY, Color c, ArrayList<MovingObstacle> pList) {
		Group platG = new Group();
		double groundLvlOffset = groundLevel - 45; //base offset for platforms
		for(int x = 0; x < lvl.length()-1;x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				//Spawn platform based off of char's location in string
				//Each char will be 90 pixels of space, and will spawn at a height of 265-(y*45)
				//charAt(x) determines offset of platform, charAt(x+1) determines how far to move the platform before returning 

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

	/**
	 *  Spawns platform obstacles, and fills the platforms ArrayList. Returns the group of newly created platforms.
	 * @param lvl - the data String determining where and what height the platform will spawn at
	 * @param offsetX - x offset for the platforms
	 * @param offsetY - y offset for the platforms
	 * @param c - 'ground' color of the platforms
	 * @param cTop - 'grass' color of the platforms
	 * @param pList - the list of Obstacles to add the platforms to
	 * @return Group - returns the Group of newly created platforms
	 */ 	
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
				int width = 25;
				int height = 800-25-175;
				Obstacle r = new Obstacle(width, height, Color.WHITESMOKE, cTop);
				pList.add(r);
				platG.getChildren().add(r.getPlat());

				r.setX(tileWidth*x);
				r.setY(100);
			}
		}
		return platG;
	}

	/**
	 *  Spawns PointBox obstacles, and fills the PointBox ArrayList. Returns the group of newly created PointBoxes.
	 * @param lvl - level String determining position and height to spawn the PointBoxes at
	 * @param sizeX - width of box
	 * @param sizeY - height of box
	 * @param c - color of box, should image loading fail
	 * @param oList - list of PointBoxes to add the new PointBoxes to
	 * @return Group - returns the Group of newly created PointBoxes
	 */
	private static Group spawnPointBoxes(String lvl, int sizeX, int sizeY, Color c, ArrayList<PointBox> oList)
	{
		Group pBoxGroup = new Group();
		double groundLvlOffset = groundLevel-45;
		for(int x = 0; x < lvl. length(); x++)
		{
			if(lvl.charAt(x) != '0') //No pointBoxes at '0' chars
			{
				PointBox pBox = new PointBox(sizeX,sizeY, c);
				pBox.setX(tileWidth*x);
				pBox.setY(groundLvlOffset-groundOffsets.get(x)+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1);
				oList.add(pBox);
				pBoxGroup.getChildren().add(pBox.getImageGroup());
			}
		}
		return pBoxGroup;
	}
}
