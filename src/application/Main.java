package application;
	
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
		
	@Override
	public void start(Stage primaryStage) {
	    try {
	    	GameObject game = new GameObject();
	    	
	    	game.render(primaryStage);
	    	game.processInput();
	    	AnimationTimer timer = new AnimationTimer() {

				@Override
				public void handle(long arg0) {
					game.update();
				}
	    	};
	    	timer.start();
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
