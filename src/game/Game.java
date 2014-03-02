package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static final String gamename = "Jom & Terry";
	public static final int menu = 0;
	public static final int play = 1;

	public Game(String gamename) {
		super(gamename);
		this.addState(new Menu(menu));
		this.addState(new Play(play));
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menu).init(gc, this);
		this.getState(play).init(gc, this);
		this.enterState(play);
	}
	
	
	public static void main(String[] args) {

		AppGameContainer container;
		try {
			container = new AppGameContainer(new Game(gamename));

			container.setDisplayMode(800, 800, false);

			// hide the FPS Text
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
