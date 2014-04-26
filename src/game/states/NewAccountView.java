package game.states;

import game.Account;
import game.AudioGame;
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
	private final static String createAccountImagePath = "res/buttons/create-account-button.png";
	private final static String backgroundImagePath = "res/background.png";
	private final  String BACK_BUTTON_PATH = "res/buttons/back-button.png";

	private final String ROBBER_IMAGE_PATH = "res/buttons/robber-button.png";
	private final String POLICE_IMAGE_PATH = "res/buttons/cop-button-2.png";
	
	private final static Integer TEXT_FIELD_WIDTH = 200;
	private final static Integer TEXT_FIELD_HEIGHT = 30;
	private final static Integer TOP_MARGIN = 50;

	private Image backgroundImage = null;
	private TextField usernameTextField;
	
	private final int BUTTON_1_X = 150; 
	private final int BUTTON_1_Y = 380; 
	
	private final int BUTTON_2_X = 500;
	private final int BUTTON_2_Y = 380;
	
	private final int TEXT_FIELD_X = 900; 
	private final int TEXT_FIELD_Y = 400;
	
	private boolean createSuccess = false;
	
	
	// Constructor
	public NewAccountView(int state, StateBasedGame sbg) {
		super();
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
		
		g.drawString("Enter Username: " , TEXT_FIELD_X, TEXT_FIELD_Y-50);
		usernameTextField.render(gc, g);

		if (createSuccess){
			// save the previous graphics color
			Color prevColor = g.getColor();

			// change the color
			g.setColor(Color.red);

			// draw string
			g.drawString("Failed to create account" , TEXT_FIELD_X, TEXT_FIELD_Y+50);

			// change the color back to what it was
			g.setColor(prevColor);
		}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		
		createSuccess = false; 
	}
	
	@Override
	public void initButtons() {
		GameContainer container = Game.getInstance().getContainer();

		// Font
		TrueTypeFont font = Font.getFont(Globals.PETITINHO_FONT,16.0f);
		
		// Images
		Image createAccountImage = null,
				backbuttonImage = null, 
				policeImage = null,
				robberImage = null;

		// Init Images
		try{
			createAccountImage = new Image(createAccountImagePath);
			backbuttonImage = new Image(BACK_BUTTON_PATH);
			robberImage = new Image(ROBBER_IMAGE_PATH);
			policeImage = new Image(POLICE_IMAGE_PATH);
		}
		catch (SlickException se) {se.printStackTrace();}
		
		// Buttons	
		MenuButton backButton = new MenuButton(container, backbuttonImage , 0, this.backgroundImage.getHeight()) {
			@Override
			public void performAction() {
				Game.getInstance().enterState(Globals.ACCOUNT_PICK);
			}
		};

		MenuButton robberButton = new MenuButton(container, robberImage, BUTTON_2_X, BUTTON_2_Y) {
			@Override
			public void performAction() {
				createAccount(true);
			}
		};
		MenuButton policeButton = new MenuButton(container, policeImage, BUTTON_1_X, BUTTON_1_Y) {
			@Override
			public void performAction() {
				createAccount(false);
			}
		};
		
		// Text Field
		usernameTextField = new TextField(container, font, TEXT_FIELD_X, TEXT_FIELD_Y,
				TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
		usernameTextField.setBackgroundColor(Color.white);
		usernameTextField.setCursorVisible(true);
		usernameTextField.setTextColor(Color.black);


		// set the Buttons of the View
		this.setButtons(robberButton, policeButton, backButton);
	}

	public void createAccount(boolean isRobber){
		AudioGame.playAsSound("button-21.ogg");

		// get Username from Textfield		
		String username = usernameTextField.getText(); 

		// initialize account
		Account account = new Account(username);

		// create account 
		createSuccess = account.create();

		if (createSuccess){
			account.setIsRobber(isRobber);
			Game.getInstance().enterState(Globals.ACCOUNT_PICK);
		}		
	}
}
