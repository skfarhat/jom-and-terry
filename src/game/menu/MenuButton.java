package game.menu;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.gui.MouseOverArea;

public class MenuButton  {
	   protected MouseOverArea moa;
	   protected GUIContext guiContext;

	   public MenuButton(GUIContext guiContext, Image image, int x, int y) {
	      this.guiContext = guiContext;
	      moa = new MouseOverArea(guiContext, image, x, y);
	      
	   }
	   
	   public void render(Graphics g) {
	      moa.render(guiContext, g);
	   }
	   
	   public void performAction(){
		   System.out.print("here");
	   }
	   
	   public boolean isMouseOver() {
	      return moa.isMouseOver();
	   }
	}