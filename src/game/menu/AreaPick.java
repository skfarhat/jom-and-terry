package game.menu;

import game.AudioGame;
import game.Game;
import game.Globals;
import game.city.building.Area;
import game.states.Play;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class AreaPick extends Menu {
	private StateBasedGame sbg = null; 

	private final String BACK_BUTTON_PATH = "res/buttons/back-button.png";
	private final String AREA_1_IMAGE_PATH = "res/city/area/area1.png"; 
	private final String AREA_2_IMAGE_PATH = "res/city/area/area2.png";
	private final String AREA_3_IMAGE_PATH = "res/city/area/area3.png";
	private final String AREA_4_IMAGE_PATH = "res/city/area/area4.png";
	private final String AREA_5_IMAGE_PATH = "res/city/area/area5.png";

	private final String AREA_1_LOCKED_IMAGE_PATH = "res/city/area/area1-locked.png"; 
	private final String AREA_2_LOCKED_IMAGE_PATH = "res/city/area/area2-locked.png";
	private final String AREA_3_LOCKED_IMAGE_PATH = "res/city/area/area3-locked.png";
	private final String AREA_4_LOCKED_IMAGE_PATH = "res/city/area/area4-locked.png";
	private final String AREA_5_LOCKED_IMAGE_PATH = "res/city/area/area5-locked.png";

	// area image paths
	final String areaImagePaths[] = {
			AREA_1_IMAGE_PATH, 
			AREA_2_IMAGE_PATH,
			AREA_3_IMAGE_PATH,
			AREA_4_IMAGE_PATH,
			AREA_5_IMAGE_PATH,
	};

	// area locked image paths
	final String lockedAreaImagePaths[] = {
			AREA_1_LOCKED_IMAGE_PATH,
			AREA_2_LOCKED_IMAGE_PATH,
			AREA_3_LOCKED_IMAGE_PATH,
			AREA_4_LOCKED_IMAGE_PATH,
			AREA_5_LOCKED_IMAGE_PATH
	}; 

	final String cityTMXPaths[] = {
			Globals.CITY_1,
			Globals.CITY_2,
			Globals.CITY_3,
			Globals.CITY_4,
			Globals.CITY_5
	};

	public AreaPick(int state, StateBasedGame sbg){
		super();
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {	
		sbg = (StateBasedGame) Game.getInstance();
		//		initButtons();
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		initButtons();
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
		int verticalMargin = 220; 
		int horizontalMargin = 450; 

		int x1 = (int) 0; 

		MenuButton []menuButtons = new MenuButton[Globals.CITY_COUNT+1];


		Image backButtonImage;
		try {backButtonImage = new Image(BACK_BUTTON_PATH);}
		catch (SlickException e) {backButtonImage = null;}

		// Back Button
		menuButtons[0] = new MenuButton(sbg.getContainer(), backButtonImage , 0, 0) {
			@Override
			public void performAction() {
				sbg.enterState(Globals.MAIN_MENU);
			}
		}
		;

		for (int i=1; i <= Globals.CITY_COUNT; i++){

			final boolean unlocked; 

			// image path for the image of the button
			String imagePath = null;

			if (unlocked = (i <= Game.getInstance().getAccount().getHighestLevelReached()))
				imagePath = areaImagePaths[i-1]; 
			else
				imagePath = lockedAreaImagePaths[i-1];

			// Position
			// arrange the images (2x3) (row x column)
			int x = (int) x1 + 	horizontalMargin* ((i-1)%3); 
			int y = (int)  verticalMargin * (1+ (int)((i-1)/3));

			// Image
			Image areaImage;
			try {areaImage = new Image (imagePath);}
			catch (SlickException e) {areaImage = null;}

			final int index = i; 
			menuButtons[i] =  new MenuButton(sbg.getContainer(), areaImage, x, y) {
				@Override
				public void performAction() {
					if (unlocked)
						enterPlay(cityTMXPaths[index-1]);
				}
			}
			;
		}

		this.setButtons(menuButtons);
	}

	public void enterPlay(String cityPath){
		AudioGame.playAsSound("button-21.ogg");
		Area area = new Area(cityPath);
		Play.getInstance().setArea(area);
		sbg.enterState(Globals.PLAY);
	}

}
