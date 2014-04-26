package game.menu;

import game.Account;
import game.AudioGame;
import game.Game;
import game.Globals;

import java.io.File;
import java.io.FilenameFilter;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AccountPick extends Menu {

	private static String NEW_ACCOUNT_IMAGE_PATH 	= "res/Buttons/new-account-button.png";
	private static String ACCOUNT_NAME_IMAGE_PATH 	= "res/Buttons/account-name.png";
	private String backgroundImagePath 				= "res/background.png";


	private String[] fileNames;
	private Image backgroundImage = null;
	private static Integer backgroundX; 

	private static Integer TOP_MARGIN = 20; 
	
	public AccountPick(int state, StateBasedGame sbg) {
		super();
		loadAccountNames();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		loadAccountNames();
		initButtons();
	}
	private void loadAccountNames() {

		File saveDirectory = new File(Globals.SAVE_DIRECTORY_PATH);
		FilenameFilter fileFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		};

		fileNames = saveDirectory.list(fileFilter);
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
		super.render(gc, sbg, g);	
		g.drawImage(this.backgroundImage, backgroundX, 0);

	}

	@Override
	public void initButtons() {
		Image newAccountImage = null, accountImage = null;

		try {
			newAccountImage = new Image(NEW_ACCOUNT_IMAGE_PATH);
			accountImage = new Image(ACCOUNT_NAME_IMAGE_PATH);
		} catch (SlickException se) {
			se.printStackTrace();
		}
		int verticalMargin = 50;

		int x = (int) (Globals.APP_WIDTH/ 2.0f - newAccountImage
				.getWidth() / 2.0f);
		int y = this.backgroundImage.getHeight() + TOP_MARGIN;

		MenuButton newAccountButton = new MenuButton(Game.getInstance()
				.getContainer(), newAccountImage, x, y) {
			@Override
			public void performAction() {
				AudioGame.playAsSound("button-21.ogg");

				Game.getInstance().enterState(Globals.NEW_ACCOUNT_PICK);
			}
		};

		MenuButton[] menuButtons = new MenuButton[fileNames.length + 1];
		int i = 0;
		for (String fileName : fileNames) {
			x = (int) (Game.getInstance().getContainer().getWidth() / 2.0f - accountImage
					.getWidth() / 2.0f);
			int y1 = y + verticalMargin * (i + 1);
			final String username = fileName.replace(".json", ""); 
			MenuButton menuButton = new MenuButton(Game.getInstance()
					.getContainer(), accountImage, username.toUpperCase(),  x, y1) {
				@Override
				public void performAction() {
					
					AudioGame.playAsSound("button-21.ogg");
					
					// load account 
					Account account = Account.load(username);
					
					// set the current selected account 
					Game.getInstance().setAccount(account);
					
					GameContainer container = Game.getInstance().getContainer(); 
					try {

						Game.getInstance().getState(Globals.MAIN_MENU).init(container, Game.getInstance());
						Game.getInstance().enterState(Globals.MAIN_MENU);
					} catch (SlickException e) {
						e.printStackTrace();
					}
					// go to Main Menu


				}
			};

			menuButtons[i] = menuButton;
			i++;
		}
		
		menuButtons[fileNames.length] = newAccountButton;

		this.setButtons(menuButtons);
	}

	@Override
	public int getID() {
		return Globals.ACCOUNT_PICK;
	}
}
