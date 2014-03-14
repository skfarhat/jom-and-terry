package game.city.building;

import game.Game;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;

public class FillingBar implements Renderable {
	
	Rectangle filledPart; 
	Rectangle mainRect; 
	
	public FillingBar(int x, int y, float width, float height){
		mainRect = new Rectangle(x, y, width, height);
		
		// initially the width for the filled part is 0 
		filledPart = new Rectangle(x,y,0,height);
	}
	public void update(float percent){
		filledPart.setWidth(percent*mainRect.getWidth());
	}
	
	public void reset(){
		filledPart.setWidth(0.0f);
	}
	
	@Override
	public void draw(float x, float y){
		Graphics g =Game.getGame().getContainer().getGraphics();
				
		g.draw(mainRect);
		g.draw(filledPart);
	}

}
