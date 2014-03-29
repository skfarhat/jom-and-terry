package game;

import game.menu.AreaPick;
import game.menu.MainMenu;
import game.menu.PlayerPick;
import game.states.GameOverState;
import game.states.LoadingState;
import game.states.PausedState;
import game.states.Play;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {

	public static final Game game  = new Game(Globals.gamename);

	private Game(String gamename) {
		super(gamename);

		// 0 MENU
		MainMenu mainMenu = new MainMenu(Globals.MAIN_MENU, this);
		this.addState(mainMenu);

		// 1 PLAY
		Play play = new Play(Globals.PLAY);		
		this.addState(play);

		// 3  PAUSE
		PausedState pause = new PausedState(Globals.PAUSE, this);
		this.addState(pause);

		// 5 GAME OVER
		GameOverState gameOver = new GameOverState(Globals.GAME_OVER, this);
		this.addState(gameOver);

		// 5 LOADING GAME STATE
		LoadingState loadingState = new LoadingState(Globals.LOADING, this);
		this.addState(loadingState);

		// 6 AREA PICK
		AreaPick areaPick = new AreaPick(Globals.AREA_PICK, this);
		this.addState(areaPick);
		
		// 7 PLAYER PICK
		PlayerPick playerPick= new PlayerPick(Globals.PLAYER_PICK, this);
		this.addState(playerPick);
		
	}

	public static Game getInstance() {
		return game;
	}

	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(Globals.MAIN_MENU).init(gc, this);
		this.getState(Globals.PLAY).init(gc, this);
		this.getState(Globals.PAUSE).init(gc, this);
		this.getState(Globals.GAME_OVER).init(gc, this);
		this.getState(Globals.LOADING).init(gc, this);
		this.getState(Globals.AREA_PICK).init(gc, this);
		this.getState(Globals.PLAYER_PICK).init(gc, this);
		
		this.enterState(Globals.MAIN_MENU);
	}

	public static void main(String[] args) {

		AppGameContainer container;
		try {
			Game game = Game.getInstance(); 
			container = new AppGameContainer(game);
			container.setDisplayMode(Globals.APP_WIDTH,Globals.APP_HEIGHT,false);
			container.setTargetFrameRate(120);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}