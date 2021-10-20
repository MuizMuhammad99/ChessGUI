package gui.sfx;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;

/**
 * An audio file which can be played
 *
 */
public class AudioFile {

	private Clip clip;

	/**
	 * Constructor
	 * @param stream audio input stream
	 */
	public AudioFile(AudioInputStream stream) {
		DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat());
		
		try {
			this.clip = (Clip) AudioSystem.getLine(info);
			clip.open(stream);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * plays the audio
	 */
	public void play() {
		clip.setFramePosition(0);
		clip.start();
	}

	/**
	 * resets the audio
	 */
	public void reset() {
		clip.setFramePosition(0);
	}
}
