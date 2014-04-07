package game.states;

import game.Account;
import game.Font;
import game.Game;
import game.Globals;
import game.menu.Menu;
import game.menu.MenuButton;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.StateBasedGame;

public class NewAccountView extends Menu {
	// Image Paths
	private static String createAccountImagePath = "res/buttons/create-account-button.png";
	private static String backgroundImagePath = "res/background.png";
	private final  String BACK_BUTTON_PATH = "res/buttons/back-button.png";

	private static Integer TEXT_FIELD_WIDTH = 200;
	private static Integer TEXT_FIELD_HEIGHT = 30;
	private static Integer TOP_MARGIN = 50;

	private Image backgroundImage = null;
	private TextField usernameTextField;
	
	private boolean failedToCreateAccount = false; 
	// Constructor
	public NewAccountView(int state, StateBasedGame sbg) {
		super(state, sbg);
	}

	@Override
	public int getID() {
		return Globals.NEW_ACCOUNT_PICK;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		this.backgroundImage = new Image(backgroundImagePath);
		initButtons();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		super.render(gc, sbg, g);
		g.drawImage(this.backgroundImage, 0, 0);
		usernameTextField.render(gc, g);

		if (failedToCreateAccount){
			// save the previous graphics color
			Color prevColor = g.getColor();

			// change the color
			g.setColor(Color.white);
			
			final int x = (int) (Game.getInstance().getContainer().getWidth() / 2.0f - TEXT_FIELD_WIDTH / 2.0f);
			final int y2 = this.backgroundImage.getHeight() + TOP_MARGIN*4;

			// draw string
			g.drawString("Failed to create account" , x, y2);

			// change the color back to what it was
			g.setColor(prevColor);
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		failedToCreateAccount = false; 
	}
	
	@Override
	public void initButtons() {
		GameContainer container = Game.getInstance().getContainer();
		Image createAccountImage = null, backbuttonImage = null ; 	
		TrueTypeFont font = Font.getFont(Globals.PETITINHO_FONT,16.0f);

		try {
			createAccountImage = new Image(createAccountImagePath);
			backbuttonImage = new Image(BACK_BUTTON_PATH);
		} catch (SlickException exc) {
			exc.printStackTrace();
		}

		// (x,y) coordinates
		final int x = (int) (Game.getInstance().getContainer().getWidth() / 2.0f - TEXT_FIELD_WIDTH / 2.0f);
		final int y = this.backgroundImage.getHeight() + TOP_MARGIN;
		final int x1 = (int) (Globals.APP_WIDTH / 2.0f - createAccountImage.getWidth() / 2.0f);
		final int y1 = this.backgroundImage.getHeight() + TOP_MARGIN*2;

		// Text Field
		usernameTextField = new TextField(container, font, x, y,
				TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
		usernameTextField.setBackgroundColor(Color.white);
		usernameTextField.setCursorVisible(true);
		usernameTextField.setTextColor(Color.black);

		// Buttons
		MenuButton createAccountButton = new MenuButton(container,
				createAccountImage, x1, y1) {
			@Override
			public void performAction() {

				// get Username from Textfield		
				String username = usernameTextField.getText(); 

				// initialize account
				Account account = new Account(username);

				// create account 
				failedToCreateAccount = !account.create();

				if (!failedToCreateAccount){
					Game.getInstance().enterState(Globals.ACCOUNT_PICK);
				}
			}
		};

		MenuButton backButton = new MenuButton(container, backbuttonImage , 0, this.backgroundImage.getHeight()) {
			@Override
			public void performAction() {
				Game.getInstance().enterState(Globals.MAIN_MENU);
			}
		};

		// set the Buttons of the View
		this.setButtons(createAccountButton, backButton);
	}

}
