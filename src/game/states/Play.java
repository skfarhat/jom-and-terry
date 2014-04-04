package game.states;

import game.Game;
import game.Globals;
import game.city.Camera;
import game.city.building.Bank;
import game.city.building.Building;
import game.city.building.House;
import game.city.building.PoliceOffice;
import game.city.building.Shop;
import game.city.person.Movable;
import game.city.person.Person;
import game.city.person.Policeman;
import game.city.person.Robber;
import game.city.person.SecurityGuard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.lwjgl.opengl.Drawable;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

public class Play extends BasicGameState {
	private String cityTileMapPath = "res/city/city.tmx";

	private GameContainer gameContainer = null;
	private StateBasedGame sbg = null;

	private Camera camera;
	private Integer gameTime = 0; 

	/**
	 * Main character can either be police or robber
	 */
	private Person mainCharacter = null; 
	public boolean userIsRobber;
	boolean isGameOver = false;

	public Rectangle toDraw;  
	//	private Integer targetDelta = 16; // msec
	private Integer TILE_SIZE = 16;

	// Characters
	private Robber robber;
	public PoliceOffice policeOffice;
	// ====================================================================================

	// Array of all the security guards
	// array is filled after all the banks have been initialized
	// used to access the security guards in play
	// the alternative is accessing the SG from bank arrays but then O(n2)
	public ArrayList<SecurityGuard> securityGuardsArray;
	private TiledMap cityTileMap;

	// For Collision Detection
	ArrayList<Rectangle> blocks;
	boolean blocked[][];


	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		userIsRobber = Game.getInstance().getAccount().getIsRobber();
		setMainCharacter();
	}

	// INIT
	// ===============================================================================================================================
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.gameContainer = gc;
		this.sbg = sbg;

		blocks = new ArrayList<>(20);


		initMap(); 				// Tile Map
		initRobber(); 			// Robber
		initPoliceOffice(); 	// PoliceOffice
		setMainCharacter(); 

		initCamera(); 			// Camera to center on the robber
		initSecurityGuards(); 	// Initialize Security Guards		

		startGameTimer();		// Start timer
	}

	private final void initCamera() throws SlickException {
		camera = new Camera(gameContainer, cityTileMap, mainCharacter);
	}

	private final void initMap() throws SlickException {
		cityTileMap = new TiledMap(cityTileMapPath);

		// puts tiles with blocked set true in the collision array
		blocked = new boolean[cityTileMap.getWidth()][cityTileMap.getHeight()];

		for (int i = 0; i < blocked.length; i++) {
			for (int j = 0; j < blocked[0].length; j++) {

				// need to check if the houses layer has a tile on the given x,y
				// position
				// TODO: WHY 1 ? ID of what ?
				int tileId = cityTileMap.getTileId(i, j, 1);

				if (tileId != 0) {
					// then there is a tile at that position
					blocked[i][j] = true;

					blocks.add(new Rectangle((float) i * TILE_SIZE, (float) j
							* TILE_SIZE, TILE_SIZE, TILE_SIZE));
				}
			}
		}

		// HOUSES
		// ========================================================================================
		// TODO: Make the index Constant in a separate file
		// Houses Object Group has index 0
		int housesObjectCount = cityTileMap.getObjectCount(0);

		// create all Houses
		for (int objectIndex = 0; objectIndex < housesObjectCount; objectIndex++) {
			int x = cityTileMap.getObjectX(0, objectIndex);
			int y = cityTileMap.getObjectY(0, objectIndex);
			float width = cityTileMap.getObjectWidth(0, objectIndex);
			float height = cityTileMap.getObjectHeight(0, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000);
			House house = new House(objectIndex, x, y, width, height, money);

			// add the house to the houses Array
		}

		// BANKS
		// ========================================================================================
		// Banks Object Group has index 1
		int banksObjectCount = cityTileMap.getObjectCount(1);
		//		banks = new ArrayList<>(10);

		// create all Banks
		for (int objectIndex = 0; objectIndex < banksObjectCount; objectIndex++) {
			int x = cityTileMap.getObjectX(1, objectIndex);
			int y = cityTileMap.getObjectY(1, objectIndex);
			float width = cityTileMap.getObjectWidth(1, objectIndex);
			float height = cityTileMap.getObjectHeight(1, objectIndex);

			// TODO: put this in the Banks class
			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000000);

			Bank bank = new Bank(objectIndex, x, y, width, height, money);

			// add the bank to the banks Array
		}

		// TODO: SHOPS
		// Banks Object Group has index 1
		int shopsObjectCount = cityTileMap.getObjectCount(2);
		// create all Banks
		for (int objectIndex = 0; objectIndex < shopsObjectCount; objectIndex++) {
			int x = cityTileMap.getObjectX(2, objectIndex);
			int y = cityTileMap.getObjectY(2, objectIndex);
			float width = cityTileMap.getObjectWidth(2, objectIndex);
			float height = cityTileMap.getObjectHeight(2, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 10000);

			Shop shop = new Shop(objectIndex, x, y, width, height, money);

			// the shop is added to the shops static ArrayList in the constructor
		}
	}

	private final void initRobber() throws SlickException {
		robber = new Robber(userIsRobber);

	}

	private final void initPoliceOffice() throws SlickException {

		this.policeOffice = new PoliceOffice(this, this.robber);
	}

	/**
	 * Method needs to be called after the initMap method to make sure the banks
	 * array has already been filled
	 * 
	 * @throws SlickException
	 */
	private final void initSecurityGuards() throws SlickException {
		this.securityGuardsArray = new ArrayList<>();
		for (Bank bank : Bank.banks) {
			for (SecurityGuard sg : bank.getSecurityGuards()) {
				sg.setRobber(this.robber);
				sg.setPlay(this);
				sg.setPoliceOffice(this.policeOffice);

				this.securityGuardsArray.add(sg);
			}
		}
	}

	public void setMainCharacter(Person mainCharacter) {
		this.mainCharacter = mainCharacter;
		if (mainCharacter instanceof Policeman){
			Policeman police = (Policeman) mainCharacter; 
			police.isSelected = true; 
		}
		if (camera !=null)
			camera.setPerson(mainCharacter);
	}

	public void setMainCharacter(){
		if (userIsRobber)
			setMainCharacter(robber);
		else
			setMainCharacter(policeOffice.getPoliceForceArray().get(0));
	}


	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		// Draw Camera
		camera.draw(gameTime);

		// Draw Robber
		robber.draw();

		// Draw Policemen
		policeOffice.draw();

		// Draw Security Guards
		SecurityGuard sg;
		for (int i = 0; i < securityGuardsArray.size(); i++) {
			sg = securityGuardsArray.get(i);
			sg.draw();
		}

		// Draw the Buildings 
		for (Building bldg : Building.buildings) {
			bldg.draw();
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();

		processInput(input);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isLocked(int x, int y) {

		boolean square = blocked[(int) x / TILE_SIZE][(int) y / TILE_SIZE];

		// The cell is locked if the square if filled
		return square;
	}

	/**
	 * Function that checks the input to the game be it Mouse Press or Keyboard
	 * Button
	 * 
	 * @param input
	 * @return
	 */
	private void processInput(Input input) {
		// MOUSE: Control for Police
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

			int destX = input.getMouseX();
			int destY = input.getMouseY();

			// if robber 
			// can select a house to get information about it
			if (userIsRobber){

				// get the building
				Building bldg = selectBuilding(destX, destY);

				// display info for this building
				bldg.displayBuildingInfo();
			}
			// if Policeman 
			// can select different policemen to control
			else {	
				// save the previous policeman and deselect him later
				Policeman prevPoliceman = (Policeman) mainCharacter;

				// get the policeman that is selected by the mouse
				Policeman policeman = selectPoliceman(destX, destY);

				// 
				if (policeman == null)
					return; 

				// set the isSelected to true
				policeman.isSelected = true; 

				// unselect the previous policeman
				prevPoliceman.isSelected = false; 

				// set the main character
				setMainCharacter(policeman);

				// TODO or get the house ?	
			}
		}


		// Move left, right, up, down		
		Movable movable = (Movable) mainCharacter;
		movable.processInput(input);


		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			// 3 --> PAUSE
			sbg.enterState(3);
		}
		else {
			robber.stop();
		}
	}


	private Building selectBuilding(int destX, int destY){
		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + destX - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + destY - Globals.SELECTION_ERROR/2,
				Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);
		
		
		for (Building bldg: Building.buildings){
			if (bldg.rect.intersects(rect))
			{
				return bldg;  
			}
		}
		return null;
	}

	/**
	 * @param destX x-position of the mouse input
	 * @param destY y-position of the mouse input
	 * @return a policeman object if the destX and destY passed are close to a policeman in the map. Returns null otherwise
	 */
	private Policeman selectPoliceman(int destX, int destY){
		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + destX - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + destY - Globals.SELECTION_ERROR/2,
				Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);
		
		for (Policeman policeman: policeOffice.getPoliceForceArray()){
			if (policeman.rect.intersects(rect))
			{
				return policeman;  
			}
		}
		return null;
	}

	private void startGameTimer(){
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameTime++; 
			}
		});
		timer.start();
	}

	public void gameOver() {

		robber.isCaught = true;
		isGameOver = true;

		// GAME OVER STATE
		this.sbg.enterState(4, new FadeOutTransition(), new FadeInTransition());
	}

	/**
	 * Play class will have ID 1 This method returns 1
	 */
	@Override
	public int getID() {
		return Globals.PLAY;
	}


}
