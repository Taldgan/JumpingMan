package application;

import java.util.Date;

public class Score {

	public static Date live1start, live1stop, live2start, live2stop, live3start, live3stop;
	
	public void start(int livesRemaining) {
		switch (livesRemaining) {
		case 3:
			live3start = new Date();
			System.out.println("3 started");
			break;
		case 2:
			live2start = new Date();
			System.out.println("2 started");
			break;
		case 1:
			live1start = new Date();
			System.out.println("1 started");
			break;
		default:
			System.out.println("Error in Score.java start()");
		}
	}
	
	public void stop(int livesRemaining) {
		switch (livesRemaining) {
		case 3:
			System.out.println("3 stopped");
			live3stop = new Date();
			break;
		case 2:
			live2stop = new Date();
			System.out.println("2 stopped");
			break;
		case 1:
			live1stop = new Date();
			System.out.println("1 stopped");
			break;
		case 0:
			System.out.println("somehow in case 0");
			break;
		default:
			System.out.println("Error in Score.java stop()");
		}
	}
	
	public int calculateScore(int livesRemaining) {
		if (livesRemaining == 3) {
			return (int) (live3stop.getTime() - live3start.getTime());
		} else if (livesRemaining == 2) {
			return (int) (live3stop.getTime() - live3start.getTime()) + 
					(int)(live2stop.getTime() - live2start.getTime());
		} else if (livesRemaining == 1) {
			return (int) (live3stop.getTime() - live3start.getTime()) + 
					(int)(live2stop.getTime() - live2start.getTime()) +
					(int)(live1stop.getTime() - live1start.getTime());
		}  else {
			System.out.println("error getting score");
			return 0;
		}
	}
}
