package game;

import game.menu.Menu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static final String gamename = "Jom & Terry";
	public static final int menuIndex = 0;
	public static final int playIndex = 1;
	public static final int pauseIndex = 3;
	public static final int gameOverIndex = 4;

	static public float width = 800.0f;
	static public float height = 800.0f;
	
	public Game(String gamename) {
		super(gamename);
		
		// 0 MENU
		this.addState(new Menu(this));
		
		// 1 PLAY
		Play play = new Play(playIndex);		
		this.addState(play);
		
		// 3  PAUSE
		PausedState pause = new PausedState(pauseIndex, this);
		this.addState(pause);
		
		// 5 GAME OVER
		GameOverState gameOver = new GameOverState(gameOverIndex, this);
		this.addState(gameOver);
 	}

	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(menuIndex).init(gc, this);
		this.getState(playIndex).init(gc, this);
		this.getState(pauseIndex).init(gc, this);
		this.getState(gameOverIndex).init(gc, this);
		
		this.enterState(menuIndex);
	}
	
	
	public static void main(String[] args) {

		AppGameContainer container;
		try {
			container = new AppGameContainer(new Game(gamename));

			container.setDisplayMode(800, 800, false);

			// hide the FPS Text
			container.setTargetFrameRate(250);
			container.setShowFPS(true);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}