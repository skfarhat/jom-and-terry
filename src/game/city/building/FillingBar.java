package game.city.building;

import game.Game;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;

public class FillingBar implements Renderable {
	
	private final static float FILLING_BAR_HEIGHT = 15.0f; 
	private Rectangle filledPart; 
	private Rectangle mainRect; 
	private String filledPercentString = ""; 
	
	
	public FillingBar(int x, int y, float width){
		mainRect = new Rectangle(x, y, width, FILLING_BAR_HEIGHT);
		
		// initially the width for the filled part is 0 
		filledPart = new Rectangle(x,y,0,FILLING_BAR_HEIGHT);
	}
	public void update(float percent){
		filledPart.setWidth(percent*mainRect.getWidth());
		filledPercentString = String.format("%d%%", (int) (percent*100));
	}
	
	public void reset(){
		filledPart.setWidth(0.0f);
	}
	
	@Override
	public void draw(float x, float y){

		// get the Graphics instance
		Graphics g = Game.getInstance().getContainer().getGraphics();
		// get the initial color to be able to reset it later
		Color intialColor = g.getColor();
		
		// draw the empty rect
		g.draw(mainRect);
	
		// draw the filled part of the rect
		g.fill(filledPart);
		
		// Draw the percentage
		g.setColor(Color.blue);
		g.drawString(filledPercentString, mainRect.getX(), mainRect.getY());
		
		// reset the color 
		g.setColor(intialColor);
	}

}
