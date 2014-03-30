package game.menu;

import game.Game;
import game.Globals;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class PlayerPick extends Menu{
	private final String BACK_BUTTON_PATH = "res/buttons/back-button.png";
	private final String ROBBER_IMAGE_PATH = "res/buttons/robber-button.png";
	private final String POLICE_IMAGE_PATH = "res/buttons/cop-button-2.png";
	private StateBasedGame sbg = null; 

	
	private final int BUTTON_1_X = 100; 
	private final int BUTTON_1_Y = 200; 
	
	private final int BUTTON_2_X = 700;
	private final int BUTTON_2_Y = 200;
	
	
	public PlayerPick(int state, StateBasedGame sbg){
		super(state,sbg);
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {	
		sbg = (StateBasedGame) Game.getInstance();
		super.init(arg0, arg1);
	}

	@Override
	public void render(GameContainer arg0, StateBasedGame arg1, Graphics g)
			throws SlickException {
		super.render(arg0, arg1, g);
		
		g.drawString("Choose player", Globals.APP_WIDTH/2 - 100, 50);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {

		super.update(arg0, arg1, arg2);
	}

	@Override
	public int getID() {
		return Globals.PLAYER_PICK;
	}

	@Override
	public void initButtons() {
		Image backButtonImage = null, policeImage = null, robberImage = null;

		try{
			backButtonImage = new Image(BACK_BUTTON_PATH);
			robberImage = new Image(ROBBER_IMAGE_PATH);
			policeImage = new Image(POLICE_IMAGE_PATH);
		}
		catch (SlickException se)
		{
			se.printStackTrace();
		}

		MenuButton backButton = new MenuButton(sbg.getContainer(),backButtonImage , 0, 0) {
			@Override
			public void performAction() {
//				sbg.enterState(Globals.MAIN_MENU);
			}
		};
		MenuButton robberButton = new MenuButton(sbg.getContainer(), robberImage, BUTTON_2_X, BUTTON_2_Y) {
			@Override
			public void performAction() {
				sbg.enterState(Globals.AREA_PICK);
			}
		};
		MenuButton policeButton = new MenuButton(sbg.getContainer(), policeImage, BUTTON_1_X, BUTTON_1_Y) {
			@Override
			public void performAction() {
				sbg.enterState(Globals.AREA_PICK);

			}
		};



		this.setButtons(backButton, robberButton, policeButton); 		
	}

}
