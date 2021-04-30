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
	private AudioInputStream sfxStream;
	private boolean sfxPlayed;
	public Sounds()
	{
		sfxCollection = new ArrayList<String>();
		sfxPlayed = false;
		sfxCollection.add("src/application/sounds/Jump.wav");
	}
	
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
    		sfxPlayed = true;
    	}
	}
}
