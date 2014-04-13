package game.states;

import game.Game;
import game.menu.MenuButton;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PausedState extends BasicGameState {
	
	private static String backToMenuImagePath = "res/buttons/main-menu.png";
	private static String resumeGamePath = "res/buttons/resume.png";
	private static String savePath = "res/buttons/save-button.png";
	private GameContainer gc;
	private StateBasedGame sbg; 
	
	/**
	 * String used to indicated when the game is saved (or some other message)
	 */
	private String message = ""; 
	protected boolean leftMouseButtonReleased;
	protected MenuButton[] buttons;
	
	
	public PausedState(int state, StateBasedGame sbg){
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.gc = gc;
		this.sbg = sbg; 
		
		initButtons();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		Font font = g.getFont(); 

		final String pauseMenuString = "Pause Menu";
		g.drawString(pauseMenuString, gc.getWidth()/2 - font.getWidth(pauseMenuString)/2, gc.getHeight()/3);
		g.drawString(message,  gc.getWidth()/2 - font.getWidth(message)/2, gc.getHeight()/3 + 30);
		for(MenuButton b : buttons)
		{
			b.render(g);
		}
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		this.checkForButtonClicks();

		Input input = gc.getInput();
		processInput(input);

	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		message = ""; 
	}
	
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);
		message = ""; 
	}
	private void processInput(Input input) {
		if (input.isKeyDown(Input.KEY_SPACE))
		{
			this.sbg.enterState(1);
		}
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
		return 3;
	}

	public void initButtons() {
		Image backToMenuImage = null, resumeGameImage = null, saveImage=null;  

		try{
			backToMenuImage = new Image(backToMenuImagePath);
			resumeGameImage = new Image(resumeGamePath);
			saveImage = new Image(savePath);
		}
		catch (SlickException se)
		{
			se.printStackTrace();
		}
 
		float height = 50.0f; 
		int verticalMargin = 100; 

		int x1 = (int) (this.gc.getWidth()/2.0f - resumeGameImage.getWidth()/2.0f); 
		int y1 = (int) (this.gc.getHeight()/2.0f - height/2.0f);
	
		int x2 = (int) (this.gc.getWidth()/2.0f - backToMenuImage.getWidth()/2.0f); 
		int y2 = y1 + 1*verticalMargin;

		int x3 = (int) (this.gc.getWidth()/2.0f - backToMenuImage.getWidth()/2.0f); 
		int y3 = y1 + 2*verticalMargin;
		
		
		MenuButton resumeGameButton = new MenuButton(this.gc, resumeGameImage, x1, y1) {
			@Override
			public void performAction() {		
				// back to the main menu
				sbg.enterState(1);
			}
		};
		
		MenuButton saveButton = new MenuButton(this.gc, saveImage, x2, y2) {
			@Override
			public void performAction() {		

				Game.getInstance().getAccount().save();
				message = "Saved"; 
			}
		};

		MenuButton backToMenuButton = new MenuButton(this.gc, backToMenuImage, x3, y3) {
			@Override
			public void performAction() {		
				// back to the main menu
				sbg.enterState(0);
			}
		};


		this.setButtons(resumeGameButton, saveButton, backToMenuButton); 
	}
	public final void setButtons(MenuButton...buttons) {
		this.buttons = buttons;
	}
}
