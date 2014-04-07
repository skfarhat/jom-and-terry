package game.menu;

import game.Game;
import game.Globals;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AreaPick extends Menu {	
	private final String BACK_BUTTON_PATH = "res/buttons/back-button.png";
	private final String AREA_1_IMAGE_PATH = "res/city/area/area1.png"; 
	private final String AREA_2_IMAGE_PATH = "res/city/area/area2.png";
	private final String AREA_3_IMAGE_PATH = "res/city/area/area3.png";
	private final String AREA_4_IMAGE_PATH = "res/city/area/area4.png";
	private final String AREA_5_IMAGE_PATH = "res/city/area/area5.png";
	
	private StateBasedGame sbg = null; 
	
	public AreaPick(int state, StateBasedGame sbg){
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
		
		g.drawString("Choose the area", Globals.APP_WIDTH/2 - 100, 50);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {

		super.update(arg0, arg1, arg2);
	}

	@Override
	public int getID() {
		return Globals.AREA_PICK;
	}

	@Override
	public void initButtons() {
		Image backButtonImage = null, area1Image = null, area2Image = null, area3Image = null, area4Image = null, area5Image = null;

		try{
			backButtonImage = new Image(BACK_BUTTON_PATH);
			area1Image = new Image(AREA_1_IMAGE_PATH);
			area2Image = new Image(AREA_2_IMAGE_PATH);
			area3Image = new Image(AREA_3_IMAGE_PATH);
			area4Image = new Image(AREA_4_IMAGE_PATH);
			area5Image = new Image(AREA_5_IMAGE_PATH);
		}
		catch (SlickException se)
		{
			se.printStackTrace();
		}
 
		int verticalMargin = 220; 
		int horizontalMargin = 450; 

		int x1 = (int) 0; 
		int y1 = (int) verticalMargin * 1;

		int x2 = (int) x1 + 	horizontalMargin* 1; 
		int y2 = (int)  verticalMargin * 1;

		int x3 = (int) x1 + horizontalMargin * 2; 
		int y3 = (int)  verticalMargin* 1;

		int x4 = (int) 0; 
		int y4 = (int) verticalMargin*2;

		
		int x5 = (int) x1 + horizontalMargin* 1; 
		int y5 = (int) verticalMargin*2;

		MenuButton backButton = new MenuButton(sbg.getContainer(),backButtonImage , 0, 0) {
			@Override
			public void performAction() {
				sbg.enterState(Globals.MAIN_MENU);
			}
		};
		
		MenuButton area1Button = new MenuButton(sbg.getContainer(), area1Image, x1, y1) {
			@Override
			public void performAction() {
				sbg.enterState(Globals.PLAY);
			}
		};
		MenuButton area2Button = new MenuButton(sbg.getContainer(), area2Image, x2, y2) {
			@Override
			public void performAction() {
//				sbg.enterState(2);

			}
		};
		MenuButton area3Button = new MenuButton(sbg.getContainer(), area3Image, x3, y3) {
			@Override
			public void performAction() {
//				sbg.enterState(3);

			}
		};
		MenuButton area4Button = new MenuButton(sbg.getContainer(), area4Image, x4, y4) {
			@Override
			public void performAction() {
//				sbg.getContainer().exit();
			}
		};
		MenuButton area5Button = new MenuButton(sbg.getContainer(), area5Image, x5, y5) {
			@Override
			public void performAction() {
//				sbg.getContainer().exit();
			}
		};


		this.setButtons(backButton, area1Button, area2Button, area3Button, area4Button, area5Button); 		
	}

}
