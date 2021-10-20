package gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import gui.sfx.AudioFile;

/**
 * Manages the assets for the game
 *
 */
public class Assets {

	private static final HashMap<String, BufferedImage> assets = new HashMap<String, BufferedImage>();
	private static final HashMap<String, AudioFile> sounds = new HashMap<String, AudioFile>();
	
	/**
	 * Load the assets
	 */
	public static void load() {
		assets.put("black_bishop", loadImage("/black_bishop.png"));
		assets.put("black_rook", loadImage("/black_rook.png"));
		assets.put("black_knight", loadImage("/black_knight.png"));
		assets.put("black_king", loadImage("/black_king.png"));
		assets.put("black_queen", loadImage("/black_queen.png"));
		assets.put("black_pawn", loadImage("/black_pawn.png"));
		
		assets.put("white_bishop", loadImage("/white_bishop.png"));
		assets.put("white_rook", loadImage("/white_rook.png"));
		assets.put("white_knight", loadImage("/white_knight.png"));
		assets.put("white_king", loadImage("/white_king.png"));
		assets.put("white_queen", loadImage("/white_queen.png"));
		assets.put("white_pawn", loadImage("/white_pawn.png"));
		
		assets.put("light_brown_cell", loadImage("/light_brown_cell.png"));
		assets.put("dark_brown_cell", loadImage("/dark_brown_cell.png"));
		assets.put("light_gray_cell", loadImage("/light_gray_cell.png"));
		assets.put("dark_gray_cell", loadImage("/dark_gray_cell.png"));
		
		
		assets.put("undo", loadImage("/undo.png"));
		assets.put("redo", loadImage("/redo.png"));
		assets.put("hint", loadImage("/hint.png"));
		assets.put("save", loadImage("/save.png"));
		assets.put("exit", loadImage("/exit.png"));
		
		//sound
		sounds.put("move", new AudioFile(loadAudioInputStream("/move.wav")));
		
	}
	
	//getters

	public static BufferedImage getImage(String key) {
		return assets.get(key);
	}
	
	public static AudioFile getAudioFile(String key) {
		return sounds.get(key);
	}

	//loader methods
	
	private static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Assets.class.getResourceAsStream(path));
		} catch (IOException e) {
			e.printStackTrace();
			
		}

		return null;
	}
	

	private static AudioInputStream loadAudioInputStream(String path) {
		AudioInputStream stream = null;
		try {
			stream = AudioSystem.getAudioInputStream(Assets.class.getResource(path));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stream;
	}

}
