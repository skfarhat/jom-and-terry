package game;

import game.city.building.Bank;
import game.city.building.Building;
import game.city.building.House;
import game.city.building.PoliceOffice;
import game.city.building.Shop;
import game.city.person.Robber;
import game.city.person.SecurityGuard;

import java.util.ArrayList;
import java.util.Random;

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

	// The user can choose to be the robber or the policeman before he starts
	// the game
	boolean userIsRobber = true;
	boolean isGameOver = false;

	int targetDelta = 16; // msec
	private Integer TILE_SIZE = 16;

	boolean highlightHouse = false;

	// Characters
	private Robber robber; // Main Player
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

	// Buildings
	private ArrayList<Building> buildings;
	private ArrayList<House> houses;
	private ArrayList<Bank> banks;
	private ArrayList<Shop> shops;

	// CONSTRUCTOR
	// ==============================================
	public Play(int state) {
	}

	// INIT
	// ===============================================================================================================================
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		System.out.println("Init");
		this.gameContainer = gc;
		this.sbg = sbg;

		blocks = new ArrayList<>(20);

		initMap(); // Tile Map
		initRobber(); // Robber
		initPoliceOffice(); // Policeoffice
		initCamera(); // Camera to center on the robber
		initSecurityGuards(); // Init Security Guards

		// pass instance of police force to Robber
		// TODO: only if the user is Police
		// this.robber.setPoliceForceArray(policeForceArray);
	}

	private void initCamera() throws SlickException {
		camera = new Camera(gameContainer, cityTileMap, robber);
	}

	private void initMap() throws SlickException {
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

		houses = new ArrayList<>(10);

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
			houses.add(house);
		}

		// BANKS
		// ========================================================================================
		// Banks Object Group has index 1
		int banksObjectCount = cityTileMap.getObjectCount(1);
		banks = new ArrayList<>(10);

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
			banks.add(bank);
		}

		// TODO: SHOPS
		shops = new ArrayList<>(10);

		// add all houses, banks and shops to the buildings array
		buildings = new ArrayList<>(banks.size() + houses.size() + shops.size());
		buildings.addAll(0, houses);
		buildings.addAll(0, banks);
		buildings.addAll(0, shops);
	}

	private void initRobber() throws SlickException {

		robber = new Robber(userIsRobber);

	}

	private void initPoliceOffice() throws SlickException {

		this.policeOffice = new PoliceOffice(this, this.robber);
	}

	/**
	 * Method needs to be called after the initMap method to make sure the banks
	 * array has already been filled
	 * 
	 * @throws SlickException
	 */
	private void initSecurityGuards() throws SlickException {
		this.securityGuardsArray = new ArrayList<>();
		for (Bank bank : banks) {
			for (SecurityGuard sg : bank.getSecurityGuards()) {
				sg.setRobber(this.robber);
				sg.setPlay(this);
				sg.setPoliceOffice(this.policeOffice);

				this.securityGuardsArray.add(sg);
			}
		}
	}

	// ===============================================================================================================================

	// GETTER AND SETTER FUNCTIONS
	public ArrayList<House> getHouses() {
		return houses;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		// Draw Camera
		// TODO put 'centerOn' and 'translateGraphics' in 'draw'
		camera.centerOn(robber.rect);
		camera.draw();
		camera.translateGraphics();

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

		for (Building bldg : buildings) {
			bldg.draw();
		}

		// ===============================================================
		// DRAW THE BUILDINGS

//		// for (Building bld : this.)
//
//		// Draw MONEY
//		// Draw the Money above the houses
//		int money = -1;
//		String moneyStr;
//		House house;
//		for (int i = 0; i < houses.size(); i++) {
//			house = houses.get(i);
//			money = house.money;
//			moneyStr = String.format("$%d", money);
//			g.drawString(moneyStr, house.xPos, house.yPos);
//		}
//		// Draw the Money above the banks
//		Bank bank;
//		for (int i = 0; i < banks.size(); i++) {
//			bank = banks.get(i);
//			money = bank.money;
//			moneyStr = String.format("$%d", money);
//			g.drawString(moneyStr, bank.xPos, bank.yPos);
//		}
		// ===============================================================

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

			if (!isLocked(destX, destY)) {
				// Move the policeman to this position
				// police1.move(destX, destY);
			} else
				System.out.println("Is locked");
		}

		// ARROWS: UP DOWN LEFT RIGHT

		if (input.isKeyDown(Input.KEY_RIGHT)) {
			robber.moveRight();

			// if Player collides with an object
			// decrement the position back (reverse the position change)
			if (collides()) {
				robber.normalForceLeft();
			}
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			robber.moveLeft();

			// if Player collides with an object
			// decrement the position back (reverse the position change)
			if (collides()) {
				robber.normalForceRight();
			}
		} else if (input.isKeyDown(Input.KEY_UP)) {
			robber.moveUp();

			// if Player collides with an object
			// increment the position back (reverse the position change)
			if (collides()) {
				robber.normalForceDown();
			}
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			robber.moveDown();

			// if Player collides with an object
			// decrement the position back (reverse the position change)
			if (collides()) {
				robber.normalForceUp();
			}
		} else if (input.isKeyDown(Input.KEY_SPACE)) {
			robber.rob();
		} else if (input.isKeyDown(Input.KEY_ESCAPE)) {
			// 3 --> PAUSE
			sbg.enterState(3);
		}

		else {
			robber.stop();
		}

	}

	private boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		boolean isInCollision = false;
		robber.nearByBldg = null;
		for (House house : houses) {
			if (robber.rect.intersects(house.frame)) {
				highlightHouse = isInCollision = true;
				robber.nearByBldg = house;
			}
		}
		for (Bank bank : banks) {
			if (robber.rect.intersects(bank.frame)) {
				highlightHouse = isInCollision = true;
				robber.nearByBldg = bank;
			}
		}
		return highlightHouse = isInCollision;
	}

	/**
	 * Play class will have ID 1 This method returns 1
	 */
	@Override
	public int getID() {
		return 1;
	}

	public void gameOver() {
		System.out.println("Game OVER");
		robber.isCaught = true;
		isGameOver = true;

		// GAME OVER STATE
		this.sbg.enterState(4, new FadeOutTransition(), new FadeInTransition());
	}
}
