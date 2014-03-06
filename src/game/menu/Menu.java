package game.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;



/*
 *  The part with the leftMouseButtonReleased was found in an online source
 */

public class Menu extends BasicGameState
{

	private StateBasedGame gameContainer = null;

	protected boolean leftMouseButtonReleased;
	protected MenuButton[] buttons;
	protected Image background;

	private String playNowImagePath 		= "res/buttons/play-now.png";
	private String settingsImagePath 		= "res/buttons/settings.png";
	private String statisticsImagePath 		= "res/buttons/statistics.png";
	private String exitImagePath 			= "res/buttons/exit.png";
	private String backgroundImagePath 		= "res/buttons/background.png";
	
	private Image backgroundImage = null;

	public Menu(StateBasedGame sbg) {
		this.gameContainer = sbg; 
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		// TODO Auto-generated method stub

		initButtons();
		this.backgroundImage = new Image(backgroundImagePath);
	}


	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		g.drawImage(this.backgroundImage, 0, 0);
		for(MenuButton b : buttons)
		{
			b.render(g);
		}
	}
	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		this.checkForButtonClicks();

	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			leftMouseButtonReleased = true;
		}
	}

	protected final void checkForButtonClicks() {
		if(leftMouseButtonReleased) {
			leftMouseButtonReleased = false;
			this.clickButton();
		}
	}

	private void clickButton() {
		for(MenuButton b : buttons) {
			if(b.isMouseOver())
				b.performAction();
		}
	}


	@Override
	public int getID() {
		return 0;
	}

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
				gameContainer.enterState(1);

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
	public final void setButtons(MenuButton...buttons) {
		this.buttons = buttons;
	}


}
