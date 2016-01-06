package core;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * 
 * @author Felix Wohnhaas Implements the audio to the game
 */
public class Audio {
	Clip clip = null;

	public Audio(String filename) {
		try {
			File f = new File(filename);
			clip = AudioSystem.getClip();
			AudioInputStream ais = AudioSystem.getAudioInputStream(f);
			clip.open(ais);
		} catch (Exception exception) {
			System.out.println("Failed To Play The WAV File!");
		}
	}

	public void start() {
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop() {
		clip.stop();
	}
}
