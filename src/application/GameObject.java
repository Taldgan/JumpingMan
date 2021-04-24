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

	Rectangle theVoid = new Rectangle(5000, 5000, Color.BLACK);
	Rectangle background = new Rectangle(2000, 500, Color.LIGHTSKYBLUE);
	Rectangle ground = new Rectangle(2000, 100, Color.GREEN);
	Rectangle obstacleBox = new Rectangle(50, 50, Color.BROWN);
	Character ref = new Character(50, 50, 20, Color.YELLOW);
	Character ref2 = new Character (300, 50, 20, Color.GREEN);
	Character ref3 = new Character (500, 50, 20, Color.BLUE);
	Character ref4 = new Character (700, 50, 20, Color.ORANGE);
	Character ref5 = new Character (900, 50, 20, Color.PURPLE);
	ArrayList<Enemies> eList = new ArrayList<Enemies>();
	Group e1 = spawnEnemies();
	Character mainGuy = new Character(250, 300-20, 20, Color.RED);
	Group group = new Group(theVoid, background, ground, obstacleBox, ref.getCharacter(), ref2.getCharacter(), ref3.getCharacter(), ref4.getCharacter(), ref5.getCharacter(), mainGuy.getCharacter(),e1);
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
		
		System.out.println("center x = " + (mainGuy.getCharacter().getCenterX() /*+ mainGuy.getCharacter().getTranslateX()*/));
		System.out.println("character translate x = " + mainGuy.getCharacter().getTranslateX());
		System.out.println("group translate x = " + group.getTranslateX());
		System.out.println("dx = " + mainGuy.getdx());
		System.out.println("x = " + mainGuy.getx());
		System.out.println("dy = " + mainGuy.getdy());
		System.out.println("y = " + mainGuy.gety());
		System.out.println("center y = " + (mainGuy.getCharacter().getCenterY() /*+ mainGuy.getCharacter().getTranslateY()*/));
		System.out.println("character translate y " + mainGuy.getCharacter().   getTranslateY());
		System.out.println("group translate y " + group.getTranslateY() + '\n');
		
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
		if (mainGuy.gety() > 280) {
			mainGuy.jumping = false;
			mainGuy.setdy(0);
		}
		if (mainGuy.getCharacter().getCenterY() != 280) {
			mainGuy.setCharacter(250, 280, 20, Color.YELLOW);
		}
		if (mainGuy.getCharacter().getCenterX() != 250) {
			mainGuy.setCharacter(250, 280, 20, Color.ORANGE);
		}
		
		
		//=====================================================
		//Update enemies
		//System.out.println("Number of children: "+enemies.getChildren().size());
		for(int x = 0; x < eList.size();x++)
		{
			if(x%2 != 0) //Made it to where every odd enemy added to the list bounces.
			{
				eList.get(x).enemyJump();
				if(eList.get(x).getJumping())
					eList.get(x).setdy(gravity*calculate()+eList.get(x).getdy());
			}
				
			eList.get(x).enemyMove();
			//Random jumping lmao
			
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
		eList.add(new Enemies(750, 280, 20, Color.WHITE));
		Group enemyGroup = new Group();
		for(int x = 0; x < eList.size(); x++)
		{
			System.out.println("Enemy added");
			enemyGroup.getChildren().add(eList.get(x).getCharacter());
		}
			
		return enemyGroup;
	}
	
}
