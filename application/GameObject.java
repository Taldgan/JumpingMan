package application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class GameObject extends InputFunctions{
	
	Boolean running;
	
	Rectangle background = new Rectangle(2000, 200);
	Character ref = new Character(50, 50, 20, Color.YELLOW);
	Character ref2 = new Character (300, 50, 20, Color.GREEN);
	Character ref3 = new Character (500, 50, 20, Color.BLUE);
	Character ref4 = new Character (700, 50, 20, Color.ORANGE);
	Character ref5 = new Character (900, 50, 20, Color.PURPLE);
	Character mainGuy = new Character(100, 100, 20, Color.RED);
	Group group = new Group(background, ref.getCharacter(), ref2.getCharacter(), ref3.getCharacter(), ref4.getCharacter(), ref5.getCharacter(), mainGuy.getCharacter());
	BorderPane root = new BorderPane(group);
	Scene scene = new Scene(root);
	
	double lastTime = System.currentTimeMillis();
	double delta;
	double gravity = 1;
		
	public GameObject() {
		
		this.running = true;
		
		group.setManaged(false);
		root.setPrefSize(640, 480);
		//mainGuy.getCharacter().setCenterX(root.getPrefWidth()/2);
		//mainGuy.getCharacter().setCenterY(root.getPrefHeight()/2);
		
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
		
		System.out.println("character translate x = " + mainGuy.getCharacter().getTranslateX());
		System.out.println("group translate x = " + group.getTranslateX());
		System.out.println("dx = " + mainGuy.getdx());
		System.out.println("x = " + mainGuy.getx());
		System.out.println("dy = " + mainGuy.getdy());
		System.out.println("y = " + mainGuy.gety());
	
		if (mainGuy.gety() > 100) {
			//System.out.println("guy is over the line");
			mainGuy.jumping = false;
			//mainGuy.character.setCenterY(100);
			//mainGuy.setCharacter(50, 50, 20, Color.GREEN);
			mainGuy.sety(100);
			mainGuy.setdy(0);
			
		}
		
		if ((mainGuy.gety() < 0) || (mainGuy.jumping == true)) {
			//System.out.println("guy is in the air");
			mainGuy.move();
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			mainGuy.setdy(mainGuy.getdy() + (gravity*calculate()));
			mainGuy.sety(mainGuy.gety() + mainGuy.getdy()*calculate());
		}
		
		/*if (mainGuy.walking) {
			//System.out.println("guy is walking");
			mainGuy.move();
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			//mainGuy.setdy(mainGuy.getdy() + (gravity*calculate()));
			//mainGuy.sety(mainGuy.gety() + mainGuy.getdy()*calculate());
		}*/
		
		if (mainGuy.getdx() != 0) {
			//System.out.println("guy is walking");
			
			//Speed cap
			if (mainGuy.getdx() > 3)
				mainGuy.setdx(3);
			if (mainGuy.getdx() < -3)
				mainGuy.setdx(-3);
			
			//Stop ghost movement
			
			mainGuy.move();
			group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
			group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
			//mainGuy.setdy(mainGuy.getdy() + (gravity*calculate()));
			//mainGuy.sety(mainGuy.gety() + mainGuy.getdy()*calculate());
		}
	
	/*
		mainGuy.move();
		group.setTranslateX(group.getTranslateX() - mainGuy.getdx());
		group.setTranslateY(group.getTranslateY() - mainGuy.getdy());
		mainGuy.setdy(mainGuy.getdy() + gravity*calculate());
		mainGuy.sety(mainGuy.gety() + mainGuy.getdy()*calculate());
	*/
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
}
