package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameObject extends InputFunctions{
	
	//Ground Level
	int groundLevel = 700;
	int tileWidth = 125;
	//String locations/types
	String lvl1Set1 =  "0000012030000000000000000090000000000000000";
	String lvl1Set2 =  "0000000000000001055500000000000000000000000";
	String lvl1GSet1 = "9110110000223234000033111111111111111111111";
	String lvl1ESet =  "0000010050000100080800000000000000000000000"; //Enemies
	String lvl1OSet =  "0000000000000000000000000000000000000000000"; //Obstacles

	Rectangle theVoid = new Rectangle(5000, 5000, Color.BLACK);
	Rectangle background = new Rectangle(lvl1GSet1.length()*tileWidth, 1200, Color.LIGHTSKYBLUE);


	//Ground Vars
	ArrayList<Obstacle> gList = new ArrayList<Obstacle>();
	ArrayList<Integer> gListOffsets = new ArrayList<Integer>();

	//Platform vars
	ArrayList<Obstacle> pList1 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList2 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList3 = new ArrayList<Obstacle>();

	Group groundSet1 = spawnGround(lvl1GSet1, 0, 0,  Color.SADDLEBROWN, Color.GREEN, gList);
	Group platformSet1 = spawnPlatforms(lvl1Set1,0,0,Color.SADDLEBROWN, Color.GREEN, pList1);
	Group platformSet2 = spawnPlatforms(lvl1Set2,-50,50, Color.SADDLEBROWN, Color.GREEN, pList2);

	//Enemy vars
	ArrayList<Enemies> eList = new ArrayList<Enemies>();
	Group e1 = spawnEnemies(lvl1ESet);

	//Obstacle vars
	ArrayList<Obstacle> oList1 = new ArrayList<Obstacle>();

	Group obstacleSet1 = spawnObstacles(lvl1OSet, 50, 50, Color.DARKGREEN, oList1); //50x50 squares as obstacles, feel free to change the numbers

	//List of All Collidable Objects
	ArrayList<Obstacle> allObs = new ArrayList<Obstacle>();


	//Scenes
	BorderPane root;
	Scene menuScene;
	Scene gameScene;
	Scene deadScene;
	Scene gameOverScene;

	//Etc
	int spawnX = 250, spawnY = groundLevel-25;
	Character mainGuy = new Character(spawnX, spawnY, 20, Color.RED);
	Group group = new Group(theVoid, background, groundSet1, mainGuy.getCharacter(), e1, platformSet1, platformSet2, obstacleSet1);

	//Labels
	Label pauseLabel = new Label("PAUSED\n(Q)UIT");
	Label livesRemaining = new Label("Lives " + mainGuy.getLives());

	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;

	public GameObject() {

		StateManager.gameState = State.MAINMENU;

		//Labels
		pauseLabel.setTranslateY(groundLevel-400);
		pauseLabel.setFont(new Font("Blocky Font", 50));
		
		livesRemaining.setTranslateY(groundLevel - 700);
		livesRemaining.setFont(new Font("Blocky Font", 40));
		

		group.setManaged(false);

		allObs.addAll(gList);
		allObs.addAll(pList1);
		allObs.addAll(pList2);
		allObs.addAll(pList3);
		allObs.addAll(oList1);

		theVoid.setY(-2500);
		theVoid.setX(-2500);
	}

	public void processInput() {

		this.gameScene.setOnKeyPressed(e ->{
			keyPressed(e, mainGuy);
		});

		this.gameScene.setOnKeyReleased(e ->{
			keyReleased(e, mainGuy);
		});

	}

	public void update() {

		mainGuy.dead();

		//If mainGuy is not touching top of platform, he must be jumping/falling
		if(!mainGuy.getCollide())
			mainGuy.setJumping(true);

		//If he is jumping or walking, update his movement to match
		if (mainGuy.walking || mainGuy.jumping) {
			mainGuy.move();
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			//group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			if (mainGuy.jumping) {
				mainGuy.setdy(mainGuy.getdy() + (gravity*calculate()));
			}
		}

		//Prevent mainGuy from moving faster than 5 units left/right
		if (mainGuy.getdx() > 5)
			mainGuy.setdx(5);
		if (mainGuy.getdx() < -5)
			mainGuy.setdx(-5);

		//???
		if (mainGuy.getdx() != 0 && !mainGuy.walking) {
			if (mainGuy.getdx() > 0)
				mainGuy.setdx(mainGuy.getdx()-0.25);
			if (mainGuy.getdx() < 0)
				mainGuy.setdx(mainGuy.getdx()+0.25);
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			//group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			mainGuy.move();
		}

		//=====================================================
		//Update enemies
		for(int x = 0; x < eList.size();x++)
		{
			//Blue enemies jump
			if(eList.get(x).getColor() == Color.BLUE)
			{
				if(eList.get(x).getJumping())
					eList.get(x).setdy(gravity*calculate()+eList.get(x).getdy());

				if(eList.get(x).getJumping() && eList.get(x).gety() > eList.get(x).getInitY())
				{
					//System.out.println("Enemy not jumping");
					eList.get(x).setdy(0); //down
					eList.get(x).setJumping(false);
				}
				int ran = eList.get(x).getRNG(1000);
				if(ran >= 0 && ran < 15) //Random chance (15/1000) that an enemy will jump. I think this is per frame, so it's still quite a lot.
				{
					eList.get(x).enemyJump();
				}

			}
			//Dark Magenta enemies on platforms
			if(eList.get(x).getColor() == Color.DARKMAGENTA)
			{
				//Swap directions if they're about to move off of their platform. Platform size is 90 rn, so they move 70 pixels left or right
				//then swap.
				if(eList.get(x).getx() >= eList.get(x).getInitialX()+tileWidth-20 || eList.get(x).getx() <= eList.get(x).getInitialX()-tileWidth)
				{
					eList.get(x).swapDir();
					eList.get(x).setInitialX(eList.get(x).getx());
				}
			}
			//Chance for an enemy to swap directions (5/1000 chance) per frame refresh.
			//Perhaps we can have this be a new enemy type?
			/*int ran = eList.get(x).getRNG(1000);
			if(ran >= 0 && ran <= 5)
				eList.get(x).swapDir();*/
			eList.get(x).enemyMove();
			//Check collision with the player
			if(eList.get(x).collide(mainGuy.getx(),mainGuy.gety(),mainGuy.getRadius(),mainGuy.getRadius()))
			{
				//Player got hit, go to game over screen or whatever. For now, change the enemy's color.
				eList.get(x).getCharacter().setFill(Color.YELLOW);
				mainGuy.setDead(true);
				eList.get(x).getCharacter().setCenterY(-1000);
				eList.remove(x);
				mainGuy.setdx(0);
			}
			else
				eList.get(x).getCharacter().setFill(eList.get(x).getColor());

			//Check collision with obstacles/platforms
			checkCollision(eList.get(x)); 
			groundCheck(eList.get(x),lvl1GSet1); //Swap enemy direction when close to a hole.

		}

		//=====================================================
		//Update platforms, testing collision. Moved down to a method at the bottom so
		//That enemies can also collide with objects.
		checkCollision(mainGuy);

		livesRemaining.setText("Lives " + mainGuy.getLives());
		livesRemaining.setTranslateX(mainGuy.getCharacter().getTranslateX()+20);
	}

	@FXML
	public void mainMenu(ActionEvent e) {
		StateManager.gameState = State.MAINMENU;
	}

	@FXML 
	public void newGame(ActionEvent event) {
		mainGuy.setDead(false);
		StateManager.gameState = State.LEVEL1;
		StateManager.currLevel = State.LEVEL1;
		/*try {
			render((Stage) ((Node) event.getSource()).getScene().getWindow());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		 */
		//mainGuy.startTime();
	}

	@FXML
	public void playAgain(ActionEvent e) {
		StateManager.gameState = State.LEVEL1;
		StateManager.currLevel = State.LEVEL1;
		mainGuy.setDead(false);
		//mainGuy.startTime();
	}

	@FXML
	public void exitGame() {
		System.exit(0);
	}

	public void render(Stage primaryStage) throws IOException {
		Parent view;
		switch(StateManager.gameState) {
		case MAINMENU:
			view = FXMLLoader.load(getClass().getResource("/application/MainMenu.fxml"));
			this.menuScene = new Scene(view);
			primaryStage.setScene(this.menuScene);
			Sounds.sPlayer.stopSong();
			break;
		case PAUSE:
			pauseLabel.setTranslateX(mainGuy.getCharacter().getTranslateX()+400);
			group.getChildren().add(pauseLabel);
			break;
		case LEVEL1:
			livesRemaining.setTranslateX(mainGuy.getCharacter().getTranslateX());
			if(!group.getChildren().contains(livesRemaining))
				group.getChildren().add(livesRemaining);
			Sounds.sPlayer.playSong(0);
			this.root = new BorderPane(this.group);
			this.gameScene = new Scene(root);
			if(group.getChildren().contains(pauseLabel))
				group.getChildren().remove(pauseLabel);
			primaryStage.setScene(this.gameScene);
			break;
		case LEVEL2:
			break;
		case YOUDIED:
			Sounds.sPlayer.stopSong();
			Sounds.sPlayer.playSFX(1);
			view = FXMLLoader.load(getClass().getResource("/application/YouDied.fxml"));
			this.deadScene = new Scene(view);
			primaryStage.setScene(this.deadScene);
			break;
		case GAMEOVER:
			view = FXMLLoader.load(getClass().getResource("/application/GameOver.fxml"));
			this.gameOverScene = new Scene(view);
			primaryStage.setScene(this.gameOverScene);
			break;
		case YOUWON:
			break;
		}
		primaryStage.show();
	}

	public double calculate() {
		double current = System.currentTimeMillis();
		delta += (current-lastTime);
		lastTime = current;
		//frameCount++;

		if (delta > 0.15) {
			delta = 0.15;
			//frameRate = String.format("FPS %s", frameCount);
			//frameCount = 0;
		}

		return delta;
	}

	public Group spawnEnemies(String eSet)
	{
		//1 is a normal enemy
		//2 is a jumping enemy
		//3 Is a normal enemy on a platform
		Group enemyGroup = new Group();
		int groundOffset;
		for(int x = 0; x < eSet.length(); x++)
		{
			groundOffset = gListOffsets.get(x);
			if(eSet.charAt(x) == '1')
			{
				eList.add(new Enemies(x*tileWidth,groundLevel-groundOffset-20,20,Color.MAGENTA));
			}
			else if(eSet.charAt(x) == '2')
			{
				eList.add(new Enemies(x*tileWidth,groundLevel-groundOffset-20,20,Color.BLUE));
			}
			else if(eSet.charAt(x) >= '3' && eSet.charAt(x) <= '9') {
				eList.add(new Enemies(x*tileWidth, groundLevel-groundOffset-20-45-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA));
			}

			//else if(eSet.charAt(x) >= '3' && eSet.charAt(x) <= '9')
			//{
			//Starting at 3, spawn enemy on platforms. To match the platform height, multiply the string value-2 by 45 and subtract that by
			//The ground level, groundLevel, groundLevel offset gListOffsets.get(x). 
			//Finally, substract in an offset of 20 to account for the circle's bottom.
			//eList.add(new Enemies((1+x)*tileWidth,
			//groundLevel-groundOffset-(45+20)-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA));
			//}
		}
		for(int x = 0; x < eList.size(); x++)
		{
			enemyGroup.getChildren().add(eList.get(x).getCharacter());
		}

		return enemyGroup;
	}

	public Group spawnGround(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> gList) 
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
			gListOffsets.add(gOffset);
		}
		return groundG;

	}

	public Group spawnPlatforms(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> pList) 
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
				r.setY(groundLvlOffset-gListOffsets.get(x)+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1-offsetY);
			}
			else if(lvl.charAt(x) == '9') { //For use for the victory platform
				int width = tileWidth;
				int height = 20;
				Obstacle r = new Obstacle(width, height, Color.DARKSLATEGRAY, cTop);
				pList.add(r);
				platG.getChildren().add(r.getPlat());
				
				r.setX(tileWidth*x);
				r.setY(groundLvlOffset-gListOffsets.get(x)-45);
			}
		}
		return platG;
	}

	public Group spawnObstacles(String lvl, int sizeX, int sizeY, Color c, ArrayList<Obstacle> oList)
	{
		Group obsGroup = new Group();
		for(int x = 0; x < lvl. length(); x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				Obstacle o = new Obstacle(sizeX,sizeY, c);
				o.setX(tileWidth*x);
				o.setY(groundLevel-gListOffsets.get(x)-(Integer.parseInt(String.valueOf(lvl.charAt(x))))*sizeY);
				oList.add(o);
				obsGroup.getChildren().add(o.getPlat());
			}
		}
		return obsGroup;
	}

	public void checkCollision(Character c)
	{
		//character bound variables for readability
		double charTop = c.gety()-c.getCharacter().getRadius();
		double charBot = c.gety()+c.getCharacter().getRadius();
		double charLeft = c.getx()+c.getCharacter().getRadius();
		double charRight = c.getx()-c.getCharacter().getRadius();
		double charRad = c.getCharacter().getRadius();

		//Replaced with for:each, pList1.get(i) was getting tedious :P
		for(Obstacle obstacle : allObs) {
			if(obstacle.collide(c.getx(), c.gety(), charRad, charRad)) {
				//Win if on last obstacle
				if(obstacle.getColor() == Color.DARKSLATEGRAY) { //If you wanna change the color for the winning platform, then make sure to change it in the spawn method too
					System.out.println("You win :^)");
//					score.stop(mainGuy.getLives());
//					System.out.println("Score: " + score.calculateScore(mainGuy.getLives()));
				}
				//obstacle.getPlat().setFill(Color.CORAL);
				double diff;
				//On top of the platform
				if(charBot-12 <= obstacle.getY() && c.getdy() >= 0)
				{
					diff = group.getTranslateY() + (c.gety() - c.getPrevY());
					c.setCollide(true);
					if(c.getColor() == Color.RED)
					{
						c.setGroundLvl(c.gety());
						c.setdy(-.05);
						c.setJumping(false);
						//System.out.println("collide with top of platform");
						c.sety(c.getPrevY());
						c.getCharacter().setTranslateY(mainGuy.getPrevTranslateY());
						//group.setTranslateY(diff);
					}

				}
				//Added 2 more checks for horizontal collision
				//Left of platform collision:
				if(charLeft <= obstacle.getX()) {

					c.setCollideRight(true);
					diff = group.getTranslateX() + (c.getx() - c.getPrevX());
					if(c.getColor() == Color.RED)
					{
						c.setx(c.getPrevX());
						c.getCharacter().setTranslateX(mainGuy.getPrevTranslateX());
						//System.out.println("collide with left side of platform");
						group.setTranslateX(diff);
					}
					//Swap enemy direction when touching an obstacle.
					else if(c.getColor() != Color.RED && obstacle.getColor() == null) //Dont ask how, dont ask why. But it just works.
					{
						c.swapDir();
					}

				}
				//Right of platform collision:
				else if(charRight >= obstacle.getX()+obstacle.getWidth()) {
					c.setCollideLeft(true);
					diff = group.getTranslateX() + (c.getx() - c.getPrevX());
					//System.out.println("obstacle color: that you are colliding with: "+String.valueOf(obstacle.getColor()));
					if(c.getColor() == Color.RED)
					{
						c.setx(c.getPrevX());
						c.getCharacter().setTranslateX(mainGuy.getPrevTranslateX());
						//System.out.println("collide with right side of platform");
						group.setTranslateX(diff);
					}
					else if(c.getColor() != Color.RED && obstacle.getColor() == null)
					{
						c.swapDir();
					}

				}
				//If under the platform:
				else if(charTop <= obstacle.getY()+obstacle.getHeight() && c.getdy() < 0)
				{

					diff = group.getTranslateY() + (c.gety() - c.getPrevY());

					c.setdy(1);
					if(c.getColor() == Color.RED)
					{
						c.sety(c.getPrevY());
						c.getCharacter().setTranslateY(mainGuy.getPrevTranslateY());
						//System.out.println("collide with bottom of platform");
						//group.setTranslateY(diff);
					}
				}
			}
			else {
				c.setCollide(false);
				//obstacle.getPlat().setFill(Color.DARKORCHID); //reset color if not touching
				mainGuy.setCollideLeft(false);
				mainGuy.setCollideRight(false);
			}
		}
	}

	//Method for enemies to turn around if they are next to a hole.
	public void groundCheck(Enemies e, String holes)
	{
		for(int x = 0; x < holes.length(); x++)
		{
			if(holes.charAt(x) == '0')
			{
				double holeRight = tileWidth*(x+1);
				double holeLeft = tileWidth*x;
				double offset = 15;
				if(e.getx() >= holeLeft-offset && e.getx() <= holeRight+offset && e.getColor() != Color.DARKMAGENTA)
				{
					e.swapDir();
					break;
				}
			}
		}
	}
}
