package application.model;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;

/**
 * Class for playing sounds and background music. Contains methods to start/stop background music, 
 * as well as for playing sound effects.
 * 
 * @author Gabriel Pastelero
 *
 */
public class Sounds 
{
	private Clip sfx;
	private Clip song;
	//ArrayLists of: Sound Effects, then Background Music
	private ArrayList<String> sfxCollection;
	private ArrayList<String> bgmCollection;
	private AudioInputStream sfxStream;
	private AudioInputStream bgmStream;
	public static Sounds sPlayer = new Sounds();
	private boolean played = false;
	public Sounds()
	{
		sfxCollection = new ArrayList<String>();
		bgmCollection = new ArrayList<String>();
		
		//Clips and songs will be played according to their index in this array list
		sfxCollection.add("src/resources/sounds/Jump.wav");
		sfxCollection.add("src/resources/sounds/Death.wav");
		sfxCollection.add("src/resources/sounds/pointBox.wav");
		sfxCollection.add("src/resources/sounds/enemyDeath.wav");
		sfxCollection.add("src/resources/sounds/winLevel.wav");
		
		bgmCollection.add("src/resources/sounds/bgm.wav");
		bgmCollection.add("src/resources/sounds/ex101.wav");
	}
	
	//Invoke these (playSFX, playSong, stopSong) methods through the static object sPlayer
	//ie, Sounds.sPlayer.playSFX(0);

	/**
	 * Plays a sound effect from the SFX ArrayList.
	 * @param num - index for SFX ArrayList, determining which sound effect to play
	 */
	public void playSFX(int num)
	{
		try
		{
			sfxStream = AudioSystem.getAudioInputStream(new File(sfxCollection.get(num)));
			sfx = AudioSystem.getClip();
			sfx.open(sfxStream);
		}
		catch (UnsupportedAudioFileException e)
		{
        	e.printStackTrace();
        }
	    catch (IOException e)
	    {
	       e.printStackTrace();
	    }
	    catch (LineUnavailableException e)
	    {
        	e.printStackTrace();
    	}
    	finally
    	{
    		sfx.start();
    		//soundEffect.loop(Clip.LOOP_CONTINUOUSLY);
    	}

	}
	
	/**
	 * Plays a song from the BGM ArrayList.
	 * @param num - index in BGM ArrayList of the song to play
	 */
	public void playSong(int num)
	{
		if(!played)
		{
			try
			{
				bgmStream = AudioSystem.getAudioInputStream(new File(bgmCollection.get(num)));
				song = AudioSystem.getClip();
				song.open(bgmStream);
			}
			catch (UnsupportedAudioFileException e)
			{
	        	e.printStackTrace();
	        }
		    catch (IOException e)
		    {
		       e.printStackTrace();
		    }
		    catch (LineUnavailableException e)
		    {
	        	e.printStackTrace();
	    	}
	    	finally
	    	{
	    		song.start();
	    		song.loop(Clip.LOOP_CONTINUOUSLY);
	    		played = true;
	    	}
		}
	}
	
	/**
	 * Stops songs that are playing.
	 */
	public void stopSong()
	{
		if(song != null && song.isOpen())
		{
			song.stop();
			played = false;
		}
	}
}
