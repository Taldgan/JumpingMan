package application.model;

import java.util.Date;

public class Score {

	public static Date live1start, live1stop, live2start, live2stop, live3start, live3stop;
	public static int finalScore;
	public static long maxTime;
	
	public static void start() {		
		maxTime = System.currentTimeMillis() + 30000;
	}
	
	public static void stop() {
		maxTime -= System.currentTimeMillis();
	}
	
	public long calculateTimeScore() {
		if (maxTime >= 0) {
			return maxTime;
		} else {
			return 0;
		}
	}
	
	public void setScore(int score) {
		Score.finalScore = score;
	}
	public int getScore() {
		return Score.finalScore;
	}
}
