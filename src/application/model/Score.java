package application.model;

import java.util.Date;

/**
 * Score class, contains methods for counting and keeping track of the score based off of time spent in a level.
 * @author Caleb Kopecky
 *
 */
public class Score {

	//Variables for keeping track of the time lives started at
	public static Date live1start, live1stop, live2start, live2stop, live3start, live3stop;
	public static int finalScore;
	public static long maxTime;
	
	/**
	 * Called upon player starting a level. Sets the maximum amount of time before the
	 * time-based score bonus is set to 0
	 */
	public static void start() {		
		maxTime = System.currentTimeMillis() + 30000;
	}
	
	/**
	 * Determines the amount of time a player took to complete a level. Reassigns maxTime, for 
	 * use in the calculateTimeScore() method.
	 */
	public static void stop() {
		maxTime -= System.currentTimeMillis();
	}
	
	/**
	 * Calculates the difference of the maximum amount of time to finish a level and gain a score bonus for time, returning it.
	 * @return long - returns the bonus to add, if it is greater than 0
	 */
	public long calculateTimeScore() {
		if (maxTime >= 0) {
			return maxTime;
		} else {
			return 0;
		}
	}
	
	/**
	 * Sets the final score
	 * @param score - the score to set
	 */
	public void setScore(int score) {
		Score.finalScore = score;
	}
	/**
	 * Gets the score
	 * @return int - returns the score
	 */
	public int getScore() {
		return Score.finalScore;
	}
}
