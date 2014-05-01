package game.city.building;

import game.Game;

import java.io.InputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.ResourceLoader;

public class LoadingView {

	private Font font; 
	private final static String FONT_PATH = "res/Font/Petitinho.ttf";
	private final static float FILLING_BAR_HEIGHT = 12.0f; 
	private Rectangle filledPart; 
	private Rectangle mainRect; 
	private String filledPercentString = ""; 

	public LoadingView(float x, float y, float width) {
		mainRect = new Rectangle(x, y, width, FILLING_BAR_HEIGHT);

		// initially the width for the filled part is 0 
		filledPart = new Rectangle(x,y,0,FILLING_BAR_HEIGHT);

		InputStream inputStream = ResourceLoader.getResourceAsStream(FONT_PATH);
		try {
			java.awt.Font awtFont2 = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(11.0f); // set font size

			this.font = new TrueTypeFont(awtFont2, true);

		} catch (Exception e) {
			e.printStackTrace();
			this.font = null; 
		}
	}


	public void update(float percent){
		filledPart.setWidth(percent*mainRect.getWidth());
		filledPercentString = String.format("%d%%", (int) (percent*100));
	}

	public void reset(){
		update(0.0f);
	}

	public void draw(){

		// get the Graphics instance
		Graphics g = Game.getInstance().getContainer().getGraphics();
		// get the initial color to be able to reset it later
		Color intialColor = g.getColor();

		// draw the empty rect
		g.draw(mainRect);

		// draw the filled part of the rect
		g.fill(filledPart);

		// Draw the percentage
		g.setFont(font); 
		g.setColor(Color.blue);
//		g.drawString(filledPercentString, mainRect.getX(), mainRect.getY());

		// reset the color 
		g.setColor(intialColor);
	}
	

}
