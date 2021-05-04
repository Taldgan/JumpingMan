package application;

import java.util.Date;

public class Score {

	public static Date live1start, live1stop, live2start, live2stop, live3start, live3stop;
	
	public void start(int livesRemaining) {
		switch (livesRemaining) {
		case 3:
			live3start = new Date();
			break;
		case 2:
			live2start = new Date();
			break;
		case 1:
			live1start = new Date();
			break;
		default:
			System.out.println("Error in Score.java start()");
		}
	}
	
	public void stop(int livesRemaining) {
		switch (livesRemaining) {
		case 3:
			live3stop = new Date();
			break;
		case 2:
			live2stop = new Date();
			break;
		case 1:
			live1stop = new Date();
			break;
		case 0:
			break;
		default:
			System.out.println("Error in Score.java stop()");
		}
	}
	
	public int calculateScore(int livesRemaining) {
		if (livesRemaining == 2) {
			return ((int) (live3stop.getTime() - live3start.getTime()))/1000;// + 
					//(int)(live2stop.getTime() - live2start.getTime()))/1000;
		} else if (livesRemaining == 1) {
			return ((int) (live3stop.getTime() - live3start.getTime()) + 
					(int)(live2stop.getTime() - live2start.getTime()))/1000;// +
					//(int)(live1stop.getTime() - live1start.getTime()))/1000;
		} else if (livesRemaining == 0) {
			return ((int) (live3stop.getTime() - live3start.getTime()) + 
					(int)(live2stop.getTime() - live2start.getTime()) +
					(int)(live1stop.getTime() - live1start.getTime()))/1000;
		} else {
			System.out.println("error getting score");
			return 0;
		}
	}
}
