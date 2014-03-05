package game;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState {

	public Menu() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	public Menu(int state)
	{
		
	}
	
	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		// 
		int x = gc.getWidth()/2 -50; 
		g.drawString("Play Now", x, 100);
		g.drawString("Settings", x, 200);
		g.drawString("Stats",  x, 300);
	}



	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Menu class will have ID 0
	 * This method returns 0
	 */
	@Override
	public int getID() {
		return 0;
	}

}
