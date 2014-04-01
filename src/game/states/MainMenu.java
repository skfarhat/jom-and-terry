package game.states;

import game.Account;
import game.Globals;
import game.menu.Menu;
import game.menu.MenuButton;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Inherits from the abstract class Menu
 * @author Sami
 *
 */
public class MainMenu extends Menu
{

	private StateBasedGame gameContainer = null;

	private final String BACK_BUTTON_PATH 	= "res/buttons/back-button.png";
	private String playNowImagePath 		= "res/buttons/play-now.png";
	private String settingsImagePath 		= "res/buttons/settings.png";
	private String statisticsImagePath 		= "res/buttons/statistics.png";
	private String exitImagePath 			= "res/buttons/exit.png";
	private String backgroundImagePath 		= "res/background.png";

	private static Integer backgroundX; 
	private Image backgroundImage = null;

	private Account account = null; 
	
 	public MainMenu(int state, StateBasedGame sbg){
		super(state,sbg);
		this.gameContainer = sbg; 
	}
 	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		
		this.backgroundImage = new Image(backgroundImagePath);
		backgroundX = (Globals.APP_WIDTH-backgroundImage.getWidth())/2;
		
		initButtons();
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.drawImage(this.backgroundImage, backgroundX, 0);
		for(MenuButton b : buttons)
		{
			b.render(g);
		}
	}
	
	@Override
	public void initButtons() {
		Image playNowImage = null, exitImage = null, statisticsImage = null, settingsImage = null, backButtonImage = null; 

		try{
			backButtonImage = new Image(BACK_BUTTON_PATH);
			playNowImage = new Image(playNowImagePath);
			settingsImage = new Image(settingsImagePath);
			statisticsImage = new Image(statisticsImagePath);
			exitImage = new Image(exitImagePath);
		}
		catch (SlickException se)
		{
			se.printStackTrace();
		}

		float height = 50.0f; 
		int verticalMargin = 100; 

		int x1 = (int) (gameContainer.getContainer().getWidth()/2.0f - playNowImage.getWidth()/2.0f); 
		int y1 = (int) (gameContainer.getContainer().getHeight()/2.0f - height/2.0f);


		int x2 = (int) (gameContainer.getContainer().getWidth()/2.0f - statisticsImage.getWidth()/2.0f); 
		int y2 = (int) y1 + verticalMargin*1;

		int x3 = (int) (gameContainer.getContainer().getWidth()/2.0f - settingsImage.getWidth()/2.0f); 
		int y3 = (int) y1 + verticalMargin*2;

		int x4 = (int) (gameContainer.getContainer().getWidth()/2.0f - exitImage.getWidth()/2.0f); 
		int y4 = (int) y1 + verticalMargin*3;

		MenuButton backButton = new MenuButton(this.gameContainer.getContainer(), backButtonImage, 0, this.backgroundImage.getHeight()) {
			@Override
			public void performAction() {
				gameContainer.enterState(Globals.ACCOUNT_PICK);
			}
		};

		MenuButton playNowButton = new MenuButton(this.gameContainer.getContainer(), playNowImage, x1, y1) {
			@Override
			public void performAction() {
				gameContainer.enterState(Globals.PLAYER_PICK);
			}
		};
		MenuButton statisticsButton = new MenuButton(this.gameContainer.getContainer(), statisticsImage, x2, y2) {
			@Override
			public void performAction() {
				
				// get the stats view
				StatsView statsView = (StatsView) gameContainer.getState(Globals.STATS_VIEW);
				
				// set the account
				statsView.setAccount(account);
				
				// enter the new view (state) 
				gameContainer.enterState(Globals.STATS_VIEW);
			}
		};
		MenuButton settingsButton = new MenuButton(this.gameContainer.getContainer(), settingsImage, x3, y3) {
			@Override
			public void performAction() {
				gameContainer.enterState(3);

			}
		};
		MenuButton exitButton = new MenuButton(this.gameContainer.getContainer(), exitImage, x4, y4) {
			@Override
			public void performAction() {
				gameContainer.getContainer().exit();
			}
		};

		this.setButtons(backButton, playNowButton,settingsButton, statisticsButton, exitButton); 
	}

	@Override
	public int getID() {
		return Globals.MAIN_MENU;
	}

}
