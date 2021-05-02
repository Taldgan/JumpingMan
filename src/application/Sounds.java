package application;

import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
public class Sounds 
{
	private Clip sfx;
	private Clip song;
	private ArrayList<String> sfxCollection;
	private ArrayList<String> bgmCollection;
	private AudioInputStream sfxStream;
	private AudioInputStream bgmStream;
	static Sounds sPlayer = new Sounds();
	private boolean played = false;
	public Sounds()
	{
		sfxCollection = new ArrayList<String>();
		bgmCollection = new ArrayList<String>();
		
		//Clips and songs will be played according to their index in this array list
		sfxCollection.add("src/application/sounds/Jump.wav");
		sfxCollection.add("src/application/sounds/Death.wav");
		
		bgmCollection.add("src/application/sounds/ex101.wav");
	}
	
	//Invoke these methods through the static object sPlayer
	//ie, Sounds.sPlayer.playSFX(0);
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
	
	public void stopSong()
	{
		if(song != null && song.isOpen())
		{
			song.stop();
			played = false;
		}
			
	}
}
