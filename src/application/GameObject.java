package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GameObject extends InputFunctions{
	//String locations/types
	String lvl1Set1 = "0100000110020301030100100000203020010000001";
	String lvl1Set2 = "000000002000400000002";
	String lvl1Set3 = "0001100";
	String lvl1ESet = "03001010300405020501210001000201000222"; //Enemy set
	String lvl1GSet1 = "11101001110111111111111111";

	String lvl1OSet = "000101000020"; //Obstacle set, Make sure these dont clip into platforms
	
	Rectangle theVoid = new Rectangle(5000, 5000, Color.BLACK);
	Rectangle background = new Rectangle(4000, 1200, Color.LIGHTSKYBLUE);
	Rectangle ground = new Rectangle(4000, 100, Color.GREEN);
	//Obstacle ground = new Obstacle(4000, 100, Color.GREEN);
	//Obstacle ground = new Obstacle(2000, 100, Color.GREEN);
	//Rectangle obstacleBox = new Rectangle(50, 50, Color.BROWN);
	//Obstacle obstacleBox = new Obstacle(50, 50, Color.BLACK);
	//Rectangle obstacleBoxTop = new Rectangle(50, 5, Color.GREEN);
	//Rectangle obstacleBoxBot = new Rectangle(50, 5, Color.BLACK);

	Character ref = new Character(50, 50, 20, Color.YELLOW);
	Character ref2 = new Character (300, 50, 20, Color.GREEN);
	Character ref3 = new Character (500, 50, 20, Color.BLUE);
	Character ref4 = new Character (700, 50, 20, Color.ORANGE);
	Character ref5 = new Character (900, 50, 20, Color.PURPLE);
	//Enemy vars
	ArrayList<Enemies> eList = new ArrayList<Enemies>();
	Group e1 = spawnEnemies(lvl1ESet);

	//Platform vars
	ArrayList<Obstacle> gList = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList1 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList2 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList3 = new ArrayList<Obstacle>();

	Group platformSet1 = spawnPlatforms(lvl1Set1,0,0,Color.SADDLEBROWN, Color.GREEN, pList1);
	Group platformSet2 = spawnPlatforms(lvl1Set2,-50,50, Color.SADDLEBROWN, Color.GREEN, pList2);
	Group groundSet1 = spawnGround(lvl1GSet1, 0, 0,  Color.SADDLEBROWN, Color.GREEN, gList);

	//Obstacle vars
	ArrayList<Obstacle> oList1 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> allObs = new ArrayList<Obstacle>();
	Group obstacleSet1 = spawnObstacles(lvl1OSet,50,50,Color.BLACK,oList1); //50x50 squares as obstacles, feel free to change the numbers
	
	//Etc
	Character mainGuy = new Character(250, 450-25, 20, Color.RED);
	Group group = new Group(theVoid, background, groundSet1, ref.getCharacter(), ref2.getCharacter(), 
		ref3.getCharacter(), ref4.getCharacter(), ref5.getCharacter(), mainGuy.getCharacter(),e1,platformSet1,platformSet2,obstacleSet1);

	BorderPane root = new BorderPane(group);
	Scene scene = new Scene(root);
	
	
	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;
	
	public GameObject() {

		group.setManaged(false);
		
		//ground.setX(0);
		//ground.setY(300);
		
		//allObs.add(ground);
		allObs.addAll(gList);
		allObs.addAll(pList1);
		allObs.addAll(pList2);
		allObs.addAll(pList3);
		
		theVoid.setY(-2500);
		theVoid.setX(-2500);
		
		//mainGuy.setGroundLvl(100);
		
		
		root.setPrefSize(500, 500);
	}
	
	public void processInput() {
		
		this.scene.setOnKeyPressed(e ->{
	    	keyPressed(e, mainGuy);
		});
	    
	    this.scene.setOnKeyReleased(e ->{
	    	keyReleased(e, mainGuy);
	    });
	    
	}
	
	public void update() {
		checkCollision(mainGuy);

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
		
		//TODO replace ground with obstacle
		
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
				if(eList.get(x).getx() >= eList.get(x).getInitialX()+70 || eList.get(x).getx() <= eList.get(x).getInitialX()-70)
				{
					eList.get(x).swapDir();
					eList.get(x).setInitialX(eList.get(x).getx());
				}
			}
			//Chance for an enemy to swap directions (5/1000 chance) per frame refresh.
			//This works, but let's not have this be a thing
			/*int ran = eList.get(x).getRNG(1000);
			if(ran >= 0 && ran <= 5)
				eList.get(x).swapDir();*/
			
			eList.get(x).enemyMove();
			
			//Check collision with the player
			if(eList.get(x).collide(mainGuy.getx(),mainGuy.gety(),mainGuy.getRadius(),mainGuy.getRadius()))
			{
				//Player got hit, go to game over screen or whatever. For now, change the enemy's color.
				eList.get(x).getCharacter().setFill(Color.YELLOW);
			}
			else
				eList.get(x).getCharacter().setFill(eList.get(x).getColor());
			
			//Check collision with obstacles/platforms
			checkCollision(eList.get(x)); 
			
		}

		//=====================================================
		//Update platforms, testing collision. Moved down to a method at the bottom so
		//That enemies can also collide with objects.

		checkCollision(mainGuy);
		
	}
	
	public void render(Stage primaryStage) {
		
		primaryStage.setScene(this.scene);
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
		for(int x = 0; x < eSet.length(); x++)
		{
			if(eSet.charAt(x) == '1')
			{
				eList.add(new Enemies(250+x*90,450-20,20,Color.MAGENTA));
			}
			else if(eSet.charAt(x) == '2')
			{
				eList.add(new Enemies(250+x*90,450-20,20,Color.BLUE));
			}
			else if(eSet.charAt(x) >= '3' && eSet.charAt(x) <= '9')
			{
				//Starting at 3, spawn enemy on platforms. To match the platform height, multiply the string value-2 by 40 and subtract that by 245
				eList.add(new Enemies(250+(1+x)*90,
						450-55-45*(Integer.parseInt(String.valueOf(eSet.charAt(x)))-2),20,Color.DARKMAGENTA));
			}
		}
		for(int x = 0; x < eList.size(); x++)
		{
			enemyGroup.getChildren().add(eList.get(x).getCharacter());
		}
			
		return enemyGroup;
	}
	
	public Group spawnGround(String lvl, int offsetX, int offsetY, Color c, Color cTop, ArrayList<Obstacle> gList) 
	{
		Group groundG = new Group();
		for(int i = 0; i < lvl.length(); i++) {
			if(lvl.charAt(i) != '0') {
				Obstacle g = new Obstacle(100, 400, c, cTop);
				gList.add(g);
				groundG.getChildren().add(g.getPlat());
				groundG.getChildren().add(g.getPlatTop());
				
				g.setX(100*i);
				g.setY(450);
			}
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
		for(int x = 0; x < lvl.length();x++)
		{
			if(lvl.charAt(x) != '0') //If the current char is not 0, create a platform in that spot.
			{
				//Spawn platform based off of char's location in string
				//Each char will be 90 pixels of space, and will spawn at a height of 265-(y*45)

				Obstacle r = new Obstacle(90,25,c, cTop); //Platforms are 90x25
				pList.add(r);
				platG.getChildren().add(r.getPlat());
				platG.getChildren().add(r.getPlatTop());
				
				r.setX(250+90*x+offsetX);
				r.setY(415+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1-offsetY);
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
				oList.add(o);
				obsGroup.getChildren().add(o.getPlat());
				//As a heads up, this will also be added to first platform set, pList1
				pList1.add(o);
				
				o.setX(250+90*x);
				o.setY(450-(Integer.parseInt(String.valueOf(lvl.charAt(x))))*sizeY);
				//o.setY(300);
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
				//obstacle.getPlat().setFill(Color.CORAL);
				double diff;
				//On top of the platform
				if(charBot-12 <= obstacle.getY() && c.getdy() >= 0)
				{
					diff = group.getTranslateY() + (c.gety() - c.getPrevY());
					c.setGroundLvl(c.gety());
					c.setCollide(true);
					
					
					c.setJumping(false);
					
					if(c.getColor() == Color.RED)
					{
						c.setdy(-.025);
							
						System.out.println("collide top");
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
						System.out.println("collide right");
						group.setTranslateX(diff);
					}
					else
						c.swapDir();
				}
				//Right of platform collision:
				else if(charRight >= obstacle.getX()+obstacle.getWidth()) {
					
					c.setCollideLeft(true);
					diff = group.getTranslateX() + (c.getx() - c.getPrevX());
					
					if(c.getColor() == Color.RED)
					{
						c.setx(c.getPrevX());
						c.getCharacter().setTranslateX(mainGuy.getPrevTranslateX());
						System.out.println("collide left");
						group.setTranslateX(diff);
					}
					else
						c.swapDir();
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
						System.out.println("collide bot");
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

		for(Obstacle obstacle : oList1)
		{
			if(obstacle.collide(c.getx(), c.gety(), charRad, charRad)) {
				c.swapDir(); //Boolean for the ai to swap directions if they touch an obstacle.
				obstacle.getPlat().setFill(Color.BEIGE);
			}
			else {
				obstacle.getPlat().setFill(obstacle.getColor()); //reset color if not touching
			}
		}
	}
	
}
