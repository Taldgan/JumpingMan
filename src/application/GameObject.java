package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;

public class GameObject extends InputFunctions{
	String lvl1Set1 = "0000000110023301000100100000203020010000001";
	String lvl1Set2 = "000000002000400000002";
	String lvl1Set3 = "0001100";
	Rectangle theVoid = new Rectangle(5000, 5000, Color.BLACK);
	Rectangle background = new Rectangle(4000, 500, Color.LIGHTSKYBLUE);
	Rectangle ground = new Rectangle(4000, 100, Color.GREEN);
	//Obstacle ground = new Obstacle(2000, 100, Color.GREEN);
	//Rectangle obstacleBox = new Rectangle(50, 50, Color.BROWN);
	Obstacle obstacleBox = new Obstacle(50, 50, Color.BROWN);
	Character ref = new Character(50, 50, 20, Color.YELLOW);
	Character ref2 = new Character (300, 50, 20, Color.GREEN);
	Character ref3 = new Character (500, 50, 20, Color.BLUE);
	Character ref4 = new Character (700, 50, 20, Color.ORANGE);
	Character ref5 = new Character (900, 50, 20, Color.PURPLE);
	//Enemy  vars
	ArrayList<Enemies> eList = new ArrayList<Enemies>();
	Group e1 = spawnEnemies();

	//Platform vars
	ArrayList<Obstacle> pList1 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList2 = new ArrayList<Obstacle>();
	ArrayList<Obstacle> pList3 = new ArrayList<Obstacle>();
	Group platformSet1 = spawnPlatforms(lvl1Set1,0,0,Color.DARKORCHID, pList1);
	Group platformSet2 = spawnPlatforms(lvl1Set2,-50,50,Color.DARKOLIVEGREEN, pList2);
	Character mainGuy = new Character(250, 300-20, 20, Color.RED);
	Group group = new Group(theVoid, background, ground, obstacleBox.getPlat(), ref.getCharacter(), ref2.getCharacter(), 
			ref3.getCharacter(), ref4.getCharacter(), ref5.getCharacter(), mainGuy.getCharacter(),e1,platformSet1,platformSet2);
	BorderPane root = new BorderPane(group);
	Scene scene = new Scene(root);
	
	
	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;
	
	public GameObject() {
		group.setManaged(false);
		
		ground.setX(0);
		ground.setY(300);
		
		obstacleBox.setX(500);
		obstacleBox.setY(250);
		
		theVoid.setY(-2500);
		theVoid.setX(-2500);
		
		mainGuy.setGroundLvl(100);
		
		
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
		//mainGuy.setGroundLvl(105);
		//Troubleshooting output
		System.out.println("center x = " + (mainGuy.getCharacter().getCenterX()));
		System.out.println("character translate x = " + mainGuy.getCharacter().getTranslateX());
		System.out.println("mainguy x: "+mainGuy.getx());
		//System.out.println("group translate x = " + group.getTranslateX());
		//System.out.println("dx = " + mainGuy.getdx());
		//System.out.println("center x = " + (mainGuy.getCharacter().getCenterX())); /*+ mainGuy.getCharacter().getTranslateX()));
		//System.out.println("character translate x = " + mainGuy.getCharacter().getTranslateX());
		//System.out.println("group translate x = " + group.getTranslateX());
		//System.out.println("dx = " + mainGuy.getdx());
		System.out.println("Main guy y: "+mainGuy.gety());
		System.out.println("Current ground level: "+mainGuy.getGroundLvl());
		System.out.println("Main guy dy: "+mainGuy.getdy());
		//System.out.println("obstacleBox x: " + obstacleBox.getX() + " y: " + obstacleBox.getY() + 
				//" Colliding: " + pList1.get(pList1.size()-1).collide(mainGuy.getx(), mainGuy.gety(), mainGuy.getCharacter().getRadius(), mainGuy.getCharacter().getRadius()));
		
		if (mainGuy.walking || mainGuy.jumping) {
			mainGuy.move();
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			if (mainGuy.jumping) {
				mainGuy.setdy(mainGuy.getdy() + (gravity*calculate()));
				//mainGuy.sety(mainGuy.gety() + mainGuy.getdy()*calculate());
			}
		}
		
		if (group.getTranslateY() != 0 && !mainGuy.jumping) {
			group.setTranslateY(0);
			mainGuy.getCharacter().setTranslateY(0);
		}

		if (mainGuy.getdx() > 5)
			mainGuy.setdx(5);
		if (mainGuy.getdx() < -5)
			mainGuy.setdx(-5);
		
		if (mainGuy.getdx() != 0 && !mainGuy.walking) {
			if (mainGuy.getdx() > 0)
				mainGuy.setdx(mainGuy.getdx()-0.25);
			if (mainGuy.getdx() < 0)
				mainGuy.setdx(mainGuy.getdx()+0.25);
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			mainGuy.move();
		}
		
		//Hard limit the ground
		if(mainGuy.gety() > 280)
		{
			mainGuy.setJumping(false);
			mainGuy.setdy(0);
			
			if(mainGuy.getCollide())
			{
				//mainGuy.getCharacter().setCenterY(mainGuy.gety());
				//mainGuy.setGroundLvl(mainGuy.gety());
			}
			else
			{
				mainGuy.getCharacter().setCenterY(280);
				mainGuy.setGroundLvl(280);
			}
		}
		
		if (mainGuy.getdy() > 0) {
			//mainGuy.jumping = false;
			//mainGuy.setdy(gravity*calculate());
			//mainGuy.setdy(0);
		}
		
	
		
		/*if (mainGuy.getCharacter().getCenterY() != 280) {
			mainGuy.setCharacter(250, 280, 20, Color.YELLOW);
		}
		if (mainGuy.getCharacter().getCenterX() != 250) {
			mainGuy.setCharacter(250, 280, 20, Color.ORANGE);
		}*/

		
		
		//=====================================================
		//Update enemies
		//System.out.println("Number of children: "+enemies.getChildren().size());
		for(int x = 0; x < eList.size();x++)
		{
			System.out.println("Enemy "+x+" x: "+eList.get(x).getx());
			if(x%2 != 0) //Made it to where every odd enemy added to the list might bounce.
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
			
		}

		//=====================================================
		//Update platforms, testing collision
		
		for(int i = 0; i < pList1.size(); i++) {
			if(pList1.get(i).collide(mainGuy.getx(), mainGuy.gety(), mainGuy.getCharacter().getRadius(), mainGuy.getCharacter().getRadius())) {
				pList1.get(i).getPlat().setFill(Color.CORAL);
				//mainGuy.getCharacter().setCenterY(mainGuy.gety());
				//double temp = mainGuy.getdx();
				//System.out.println("ground level set to: "+pList1.get(i).getY());
				
				
				
				//On top of the platform
				if(mainGuy.gety()+mainGuy.getCharacter().getRadius()-12 <= pList1.get(i).getY())
				{
					mainGuy.setGroundLvl(mainGuy.gety());
					mainGuy.setCollide(true);
					//mainGuy.getCharacter().setCenterY(pList1.get(i).getY());
					/*if(mainGuy.gety()+mainGuy.getCharacter().getRadius()> pList1.get(i).getY())
					{
						mainGuy.setdy(-.05);
					}*/
					//else
					mainGuy.setdy(0);
					
					//Troubleshooting prints
					System.out.println("Main guy y + radius: "+(mainGuy.gety()+mainGuy.getCharacter().getRadius()));
					System.out.println("Platform's y:"+pList1.get(i).getY());
				}
				//If under the platform:
				else if(mainGuy.gety()-mainGuy.getCharacter().getRadius() <= pList1.get(i).getY()+pList1.get(i).getHeight() ) //&&mainGuy.gety()-mainGuy.getCharacter().getRadius() >= pList1.get(i).getY()
					
				{
					//mainGuy.setJumping(false);
					mainGuy.setdy(1);
					//mainGuy.setdy(0);
				}
				//mainGuy.setdx(temp);
				//Break out of loop, since you can only be colliding with at most 2 things.
				i = pList1.size();
				
			}
			else {
				mainGuy.setCollide(false);
				pList1.get(i).getPlat().setFill(Color.DARKORCHID); //reset color if not touching
			}
		}
		
		if(obstacleBox.collide(mainGuy.getx(), mainGuy.gety(), mainGuy.getCharacter().getRadius(), mainGuy.getCharacter().getRadius())) {
				obstacleBox.getPlat().setFill(Color.CORAL);
				//mainGuy.setMinY(obstacleBox.getY()-mainGuy.getCharacter().getRadius()); //set minimum player Y to platform y
			}
			else {
				obstacleBox.getPlat().setFill(Color.DARKORCHID); //reset color if not touching
			}
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
	 
	public Group spawnEnemies()
	{
		eList.add(new Enemies(500, 280, 20, Color.WHITE));
		eList.add(new Enemies(750, 280, 20, Color.BLUE));
		eList.add(new Enemies(1000, 280, 20, Color.WHITE));
		Group enemyGroup = new Group();
		for(int x = 0; x < eList.size(); x++)
		{
			//System.out.println("Enemy added");
			enemyGroup.getChildren().add(eList.get(x).getCharacter());
		}
			
		return enemyGroup;
	}
	
	public Group spawnPlatforms(String lvl, int offsetX, int offsetY, Color c, ArrayList<Obstacle> pList) 
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

				Obstacle r = new Obstacle(90,25,c); //Platforms are 90x25
				pList.add(r);
				platG.getChildren().add(r.getPlat());
				
				r.setX(250+90*x+offsetX);
				r.setY(265+Integer.parseInt(String.valueOf(lvl.charAt(x)))*45*-1-offsetY);
			}
		}
		return platG;
	}
	
}
