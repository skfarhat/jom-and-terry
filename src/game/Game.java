package game;

import game.menu.Menu;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static final Game game  = new Game(Globals.gamename);

	private Game(String gamename) {
		super(gamename);

		// 0 MENU
		this.addState(new Menu(this));

		// 1 PLAY
		Play play = new Play(Globals.playIndex);		
		this.addState(play);

		// 3  PAUSE
		PausedState pause = new PausedState(Globals.pauseIndex, this);
		this.addState(pause);

		// 5 GAME OVER
		GameOverState gameOver = new GameOverState(Globals.gameOverIndex, this);
		this.addState(gameOver);


		// 5 LOADING GAME STATE
		LoadingState loadingState = new LoadingState(Globals.loadingIndex, this);
		this.addState(loadingState);
	}

	public static Game getInstance() {
		return game;
	}


	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(Globals.menuIndex).init(gc, this);
		this.getState(Globals.playIndex).init(gc, this);
		this.getState(Globals.pauseIndex).init(gc, this);
		this.getState(Globals.gameOverIndex).init(gc, this);

		this.enterState(Globals.menuIndex);
	}


	public static void main(String[] args) {

		AppGameContainer container;
		try {
			Game game = Game.getInstance(); 
			container = new AppGameContainer(game);

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