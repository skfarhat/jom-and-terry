package game.menu;

import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class MenuButton  {
	protected MouseOverArea moa;
	protected GUIContext guiContext;
	private String title = null; 
	private int x, y;
	private Image image; 
	
	public MenuButton(GUIContext guiContext, Image image, int x, int y) {
		this.x = x; 
		this.y = y; 
		this.image = image; 
		this.guiContext = guiContext;
		moa = new MouseOverArea(guiContext, image, x, y);   
	}
	public MenuButton(GUIContext guiContext, Image image, String title,  int x, int y) {
		this.x = x; 
		this.y = y; 
		this.title = title; 
		this.image = image; 

		this.guiContext = guiContext;
		moa = new MouseOverArea(guiContext, image, x, y);   
	}

	public void render(Graphics g) {
		moa.render(guiContext, g);
		
		Font font = g.getFont(); 
		int fontHeight = font.getLineHeight(); 
		
		if (title!=null)
			g.drawString(title, x +image.getWidth()/2 - title.length()*5, y + image.getHeight()/2 - fontHeight/2);
	}

	public void performAction(){
	}

	public boolean isMouseOver() {
		return moa.isMouseOver();
	}
}