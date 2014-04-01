package game.states;

import game.Account;
import game.Game;
import game.Globals;
import game.menu.Menu;
import game.menu.MenuButton;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class StatsView extends Menu {
	private final  String BACK_BUTTON_PATH = "res/buttons/back-button.png";
	private static String backgroundImagePath = "res/background.png";
	private Image backgroundImage = null;

	Account account; 

	// position to start drawing the high-score strings
	private static Integer xStatPosition = 30;
	private static Integer yStatPosition;
	private static Integer yMargin = 20;

	private String timeSpentStr; 
	private String highscoreStr;
	private String highestLevelStr;
	private String usernameStr;


	public StatsView(int state, StateBasedGame sbg){
		super(state, sbg); 
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		// get the account from the singleton Game
		this.account = Game.getInstance().getAccount(); 
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {		

		this.backgroundImage = new Image(backgroundImagePath);
		yStatPosition = this.backgroundImage.getHeight();
		xStatPosition = this.backgroundImage.getWidth()/2; 
		initButtons();

	}
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		super.render(gc, sbg, g);

		this.backgroundImage.draw(0, 0);

		if (account !=null){
			
			Font font = g.getFont(); 
			
			usernameStr = String.format("Username: %s", account.getUsername());								// username 
			highscoreStr = String.format("Highscore: %d", account.getHighscore()); 							// highscore
			highestLevelStr = String.format("Highest Level: %d", account.getHighestLevelReached());			// highest level reached 
			timeSpentStr = String.format("Time Spent: %d", account.getTimePlaying());  				// time spent playing

			//TODO previous scores	

			g.drawString(usernameStr, xStatPosition - font.getWidth(usernameStr)/2, yStatPosition + yMargin);			// username
			g.drawString(highscoreStr, xStatPosition - font.getWidth(highscoreStr)/2, yStatPosition + yMargin*2);		// highscore
			g.drawString(highestLevelStr, xStatPosition - font.getWidth(highestLevelStr)/2, yStatPosition + yMargin*3);	// highest level
			g.drawString(timeSpentStr, xStatPosition - font.getWidth(timeSpentStr)/2, yStatPosition + yMargin*4);		// time spent playing

		}
	}
	@Override
	public void initButtons() {
		Image backbuttonImage = null;
		GameContainer container = Game.getInstance().getContainer();

		try {
			backbuttonImage = new Image(BACK_BUTTON_PATH);
		} catch (SlickException exc) {
			exc.printStackTrace();
		}

		MenuButton backButton = new MenuButton(container, backbuttonImage , 0, this.backgroundImage.getHeight()) {
			@Override
			public void performAction() {
				Game.getInstance().enterState(Globals.MAIN_MENU);
			}
		};

		this.setButtons(backButton);
	}

	@Override
	public int getID() {
		return Globals.STATS_VIEW;
	}

}
