package game;
import java.io.InputStream;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;


public class Font {
	private final static String FONT_PATH = "res/Font/"; 

	public static TrueTypeFont getFont(String fontName, float size){
		InputStream inputStream = ResourceLoader.getResourceAsStream(FONT_PATH + fontName);
		try {
			java.awt.Font awtFont2 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(size); // set font size
			
			return new TrueTypeFont(awtFont2, true);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null; 
		}
	}
}
