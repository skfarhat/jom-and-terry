package game.states;

import game.Account;
import game.AudioGame;
import game.Game;
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

	private final String BACK_BUTTON_PATH 				= "res/buttons/back-button.png";
	private final String RESUME_GAME_IMAGEPATH 			= "res/buttons/resume-game-button.png";
	private final String RESUME_GAME_DISABLED_IMAGEPATH = "res/buttons/resume-game-disabled.png";
	private final String NEW_GAME_IMAGEPATH 			= "res/buttons/new-game-button.png";	
	private final String SETTINGS_IMAGEPATH 			= "res/buttons/settings-button.png";
	private final String STATISTICS_IMAGEPATH 			= "res/buttons/statistics-button.png";
	private final String EXIT_IMAGEPATH 				= "res/buttons/exit-button.png";
	private final String BACKGROUND_IMAGEPATH 			= "res/background.png";

	private static Integer backgroundX; 
	private Image backgroundImage = null;

	private Account account = null; 

	public MainMenu(int state, StateBasedGame sbg){
		super();
		this.gameContainer = sbg; 
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {

		this.backgroundImage = new Image(BACKGROUND_IMAGEPATH);
		backgroundX = (Globals.APP_WIDTH-backgroundImage.getWidth())/2;

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
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		initButtons();
		Play.getInstance().reset(); 
	}

	@Override
	public void initButtons() {
		Image exitImage = null, statisticsImage = null, settingsImage = null, backButtonImage = null, newGameButtonImage = null, 
				resumeGameButtonImage = null, resumeGameButtonDisabledImage = null; 

		try{

			newGameButtonImage  = new Image(NEW_GAME_IMAGEPATH);						// NEW GAME
			resumeGameButtonImage  = new Image(RESUME_GAME_IMAGEPATH);					// RESUME GAME
			resumeGameButtonDisabledImage  = new Image(RESUME_GAME_DISABLED_IMAGEPATH);	// RESUME GAME DISABLED
			backButtonImage = new Image(BACK_BUTTON_PATH);								// BACK 
			settingsImage = new Image(SETTINGS_IMAGEPATH);								// SETTINGS
			statisticsImage = new Image(STATISTICS_IMAGEPATH);							// STATISTICS
			exitImage = new Image(EXIT_IMAGEPATH);										// EXIT
		}	
		catch (SlickException se)
		{
			se.printStackTrace();
		}

		final float height = 50.0f; 
		final int verticalMargin = 70; 

		final int x1 = (int) (gameContainer.getContainer().getWidth()/2.0f - resumeGameButtonImage.getWidth()/2.0f); 
		final int y1 = (int) (gameContainer.getContainer().getHeight()/2.0f - height/2.0f);

		final int x2 = (int) (gameContainer.getContainer().getWidth()/2.0f - newGameButtonImage.getWidth()/2.0f); 
		final int y2 = (int) y1 + verticalMargin*1;

		final int x3 = (int) (gameContainer.getContainer().getWidth()/2.0f - statisticsImage.getWidth()/2.0f); 
		final int y3 = (int) y1 + verticalMargin*2;

		final int x4 = (int) (gameContainer.getContainer().getWidth()/2.0f - settingsImage.getWidth()/2.0f); 
		final int y4 = (int) y1 + verticalMargin*3;

		final int x5 = (int) (gameContainer.getContainer().getWidth()/2.0f - exitImage.getWidth()/2.0f); 
		final int y5 = (int) y1 + verticalMargin*4;



		final boolean canResume = Game.getInstance().getAccount().getResumeGame()!=null; 
		Image resumeGameImage = (canResume)? resumeGameButtonImage : resumeGameButtonDisabledImage;
		

		MenuButton backButton = new MenuButton(this.gameContainer.getContainer(), backButtonImage, 0, this.backgroundImage.getHeight()) {
			@Override
			public void performAction() {
				gameContainer.enterState(Globals.ACCOUNT_PICK);
			}
		};
		MenuButton resumeGameButton = new MenuButton(this.gameContainer.getContainer(), resumeGameImage, x1, y1) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");
				Play.getInstance().setResumingGame(true);
				gameContainer.enterState(Globals.PLAY);
			}
		};
		resumeGameButton.setEnabled(canResume);

		MenuButton newGameButton = new MenuButton(this.gameContainer.getContainer(), newGameButtonImage, x2, y2) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");
				if (canResume)
				{
					Game.getInstance().getAccount().setResumeGame(null);
					Game.getInstance().getAccount().removeResumeGame();
				}
				gameContainer.enterState(Globals.AREA_PICK);
			}
		};
		MenuButton statisticsButton = new MenuButton(this.gameContainer.getContainer(), statisticsImage, x3, y3) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");
				
				// get the stats view
				StatsView statsView = (StatsView) gameContainer.getState(Globals.STATS_VIEW);

				// set the account
				statsView.setAccount(account);

				// enter the new view (state) 
				gameContainer.enterState(Globals.STATS_VIEW);
			}
		};
		MenuButton settingsButton = new MenuButton(this.gameContainer.getContainer(), settingsImage, x4, y4) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");
				gameContainer.enterState(3);

			}
		};
		MenuButton exitButton = new MenuButton(this.gameContainer.getContainer(), exitImage, x5, y5) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");
				gameContainer.getContainer().exit();
			}
		};

		this.setButtons(resumeGameButton, newGameButton, backButton,settingsButton, statisticsButton, exitButton); 
	}

	@Override
	public int getID() {
		return Globals.MAIN_MENU;
	}

}
