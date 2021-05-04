package application;

import java.util.Date;

public class Score {

	public static Date live1start, live1stop, live2start, live2stop, live3start, live3stop;
	public static int finalScore;
	public static long maxTime;
	
	public void start() {		
		System.out.println("start: " + System.currentTimeMillis());
		maxTime = System.currentTimeMillis() + 20000;
	}
	
	public void stop() {
		System.out.println("stop: " + System.currentTimeMillis());
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
