package tools;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

import java.util.Random;
import java.util.Vector;

import java.lang.NumberFormatException;
import java.lang.IllegalArgumentException;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Function {

// ======================================================================================================================================================

	public static Dimension getScreenResolution() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}

	public static void setFullScreenExclusive(JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();

		if(gd.isFullScreenSupported()) {
			gd.setFullScreenWindow(frame);
		} else {
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
		}
	}

	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		if(screen > -1 && screen < gs.length) {
			gs[screen].setFullScreenWindow(frame);
		} else if(gs.length > 0) {
			gs[0].setFullScreenWindow(frame);
		} else {
			throw new RuntimeException("No Screens Found");
		}
	}

	public static void getDefaultConfiguration() {
		System.out.println(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
	}

	private int getDisplayRefreshRate() {
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int rate = gd.getDisplayMode().getRefreshRate();
		return rate == DisplayMode.REFRESH_RATE_UNKNOWN ? 60 : rate;
	}

// ======================================================================================================================================================

	public static int randInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}

	public static String numberFormat(long number) {
		return Function.numberFormat(number, ',', 3);
	}
	public static String numberFormat(long number, char separator) {
		return Function.numberFormat(number, separator, 3);
	}
	public static String numberFormat(long number, int inBetween) {
		return Function.numberFormat(number, '.', inBetween);
	}
	public static String numberFormat(long number, char separator, int inBetween) {
		String str = "" + number;
		String result = "";
		char[] charArray = str.toCharArray();
		int count = 0;
		for(int i = charArray.length - 1; i >= 0; i--) {
			if(count == inBetween) {
				result = separator + result;
				count = 0;
			}
			result = charArray[i] + result;
			count++;
		}
		return result;
	}

	public static String rewriteNumber(int nb, int nbCharacter) {
		return String.format("%0" + nbCharacter + "d", nb);
	}

	public static double mapRange(double value, double inMin, double inMax, double outMin, double outMax) {
		return (value - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
	}

	public static double percent(int nb, double percentage) {
		return nb * percentage / 100;
	}

	public static int dividableBy(int nb, int q) {
		return nb + (q - (nb % q));
	}

// ======================================================================================================================================================

	public static Color getColorFromHex(String hexColor) {
		if(hexColor.startsWith("#")) {
			hexColor = hexColor.substring(1);
		}
		int rgb = Integer.parseInt(hexColor, 16);
		return new Color(rgb);
	}

	public static void print(Object... toPrint) {
		for(Object obj: toPrint) {
			System.out.print(obj.toString() + "\t\t");
		}
		System.out.println();
	}

// ======================================================================================================================================================

	public static void changeClipVolume(Clip clip, double volume) {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

		volume = Math.max(0, volume);
		volume = Math.min(100, volume);

		volume = Function.mapRange(volume, 0, 100, gainControl.getMinimum(), gainControl.getMaximum());
		gainControl.setValue((float)volume);
	}
	
// ======================================================================================================================================================

	public static BufferedImage openImage(String filePath) throws IOException {
		File file = new File(filePath);
		if(file.exists()) {
			return ImageIO.read(file);
		}
		try(java.io.InputStream is = Function.class.getClassLoader().getResourceAsStream(filePath)) {
			if(is != null) {
				return ImageIO.read(is);
			}
		}
		throw new FileNotFoundException("Image file not found: " + filePath);
	}

	public static BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
		BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2D = scaledImage.createGraphics();
		graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
		graphics2D.dispose();
		return scaledImage;
	}


// ======================================================================================================================================================

	public static String[] readAllLines(String filePath) throws FileNotFoundException, IOException {
		return Function.readAllLines(new File(filePath));
	}

	public static String[] readAllLines(File toRead) throws FileNotFoundException, IOException {
		FileReader fReader = new FileReader(toRead);
		BufferedReader bReader = new BufferedReader(fReader);
		Vector<String> array = new Vector<String>();

		String temp = "";
		while(true) {
			temp = bReader.readLine();
			if(temp == null) {
				break;
			}
			array.add(temp);
		}
		fReader.close();
		bReader.close();

		String[] result = new String[array.size()];
		for(int i=0; i<result.length; i++) {
			result[i] = array.elementAt(i);
		}
		return result;
	}

// ======================================================================================================================================================

	public static int stringToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch(NumberFormatException e) {
			return 0;
		}
	}
}