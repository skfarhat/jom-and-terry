package game.menu;

import game.Globals;

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

	private String playNowImagePath 		= "res/buttons/play-now.png";
	private String settingsImagePath 		= "res/buttons/settings.png";
	private String statisticsImagePath 		= "res/buttons/statistics.png";
	private String exitImagePath 			= "res/buttons/exit.png";
	private String backgroundImagePath 		= "res/background.png";

	private static Integer backgroundX; 
	
	private Image backgroundImage = null;


	public MainMenu(int state, StateBasedGame sbg){
		super(state,sbg);
		this.gameContainer = sbg; 
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {

		initButtons();
		this.backgroundImage = new Image(backgroundImagePath);
		backgroundX = (Globals.APP_WIDTH-backgroundImage.getWidth())/2;
	}
	
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) 
			throws SlickException{
		super.update(arg0, arg1, arg2);
		
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
		Image playNowImage = null, exitImage = null, statisticsImage = null, settingsImage = null; 

		try{
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



		MenuButton playNowButton = new MenuButton(this.gameContainer.getContainer(), playNowImage, x1, y1) {
			@Override
			public void performAction() {
				gameContainer.enterState(Globals.PLAYER_PICK);
			}
		};
		MenuButton statisticsButton = new MenuButton(this.gameContainer.getContainer(), statisticsImage, x2, y2) {
			@Override
			public void performAction() {
				gameContainer.enterState(2);

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

		this.setButtons(playNowButton,settingsButton, statisticsButton, exitButton); 
	}


	@Override
	public int getID() {
		return Globals.MAIN_MENU;
	}



}
