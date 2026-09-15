package tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AssetsLoader {

	public static final String IMG_FILE_PATH = "assets/img/";
	public static final String FONT_FILE_PATH = "assets/fonts/";
	public static final String TEXT_FILE_PATH = "assets/text/";
	public static final String CLIP_FILE_PATH = "assets/sounds/";

// ======================================================================================================================================================

	public static BufferedImage loadImage(String filePath) throws IOException {
		return Function.openImage(AssetsLoader.IMG_FILE_PATH + filePath);
	}

	public static BufferedImage loadSaveImg(String filePath) throws IOException {
		return AssetsLoader.loadImage("save_img/" + filePath);
	}

	public static BufferedImage loadTilesTexture(String filePath) throws IOException {
		return AssetsLoader.loadImage("tiles/" + filePath);
	}

	public static BufferedImage loadEntitySprites(String filePath) throws IOException {
		return AssetsLoader.loadImage("entity/" + filePath);
	}

// ======================================================================================================================================================

	public static String[] loadText(String filePath) throws FileNotFoundException, IOException {
		return Function.readAllLines(AssetsLoader.TEXT_FILE_PATH + filePath);
	}

	public static String[] loadConf(String filePath) throws FileNotFoundException, IOException {
		return AssetsLoader.loadText("conf/" + filePath);
	}

// ======================================================================================================================================================

	public static Font loadFont(String filePath, int style, int size) throws FontFormatException, IOException {
		File file = new File(AssetsLoader.FONT_FILE_PATH + filePath);
		if (file.exists()) {
			Font font = Font.createFont(Font.TRUETYPE_FONT, file);
			return font.deriveFont(style, (float) size);
		}
		try(InputStream is = AssetsLoader.class.getClassLoader().getResourceAsStream(AssetsLoader.FONT_FILE_PATH + filePath)) {
			if (is != null) {
				Font font = Font.createFont(Font.TRUETYPE_FONT, is);
				return font.deriveFont(style, (float) size);
			}
		}
		throw new FileNotFoundException("Font file not found: " + filePath);
	}

// ======================================================================================================================================================

	public static Clip loadClip(String filePath, double volume, Object source) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
		Clip result = AudioSystem.getClip();
		AudioInputStream inputStream = null;

		File file = new File(AssetsLoader.CLIP_FILE_PATH + filePath);
		if (file.exists()) {
			inputStream = AudioSystem.getAudioInputStream(file);
		} else {
			String resourcePath = "/" + AssetsLoader.CLIP_FILE_PATH + filePath;
			InputStream is = source != null ? source.getClass().getResourceAsStream(resourcePath) : null;
			if (is == null) {
				is = AssetsLoader.class.getClassLoader().getResourceAsStream(AssetsLoader.CLIP_FILE_PATH + filePath);
			}
			if (is != null) {
				inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			} else {
				throw new FileNotFoundException("Audio clip file not found: " + filePath);
			}
		}

		result.open(inputStream);
		Function.changeClipVolume(result, volume);
		return result;
	}
}