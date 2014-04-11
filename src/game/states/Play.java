package game.states;

import game.Game;
import game.Globals;
import game.city.Camera;
import game.city.building.Bank;
import game.city.building.Building;
import game.city.building.House;
import game.city.building.Shop;
import game.city.person.Movable;
import game.city.person.Person;
import game.city.person.PoliceOffice;
import game.city.person.Policeman;
import game.city.person.PolicemanUser;
import game.city.person.Robber;
import game.city.person.RobberComputer;
import game.city.person.RobberUser;
import game.city.person.SecurityGuard;
import game.city.road.Highway;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.tiled.TiledMap;

public class Play extends BasicGameState {
	enum FlagType{
		NONE , 
		RED, 
		DARK_BLUE, 
		VIOLET, 
		GREEN
	}; 

	// NONE, RED, DARK_BLUE, VIOLET, GREEN 
	public boolean flagsShown[] = {true, true, true, true, true};  

	// Singleton 
	public static final Play play = new Play();
	public static Play getInstance(){
		return play;
	}

	// TODO: move to Globals
	private final String cityTileMapPath = "res/city/city.tmx";

	private Camera camera;
	private Integer gameTime = 0; 

	private TiledMap cityTileMap;

	/**
	 * Main character can either be police or robber
	 */
	private Person mainCharacter = null; 
	public boolean userIsRobber;
	boolean isGameOver = false;
	public Rectangle toDraw;  

	// Characters
	private Robber robber;
	private PoliceOffice policeOffice;
	// =============================================================================================================================

	// For Collision Detection
	ArrayList<Rectangle> blocks;
	boolean blocked[][];


	// INIT
	// ===============================================================================================================================
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {

		blocks = new ArrayList<>(20);

		initMap(); 				// Tile Map
		initRobber(); 			// Robber		
	}

	public void start() throws SlickException{
		setMainCharacter(); 

		initCamera(); 			// Camera to center on the robber
		initSecurityGuards(); 	// Initialize Security Guards		

		startGameTimer();		// Start timer

	}

	private final void initCamera() throws SlickException {
		camera = new Camera(cityTileMap, mainCharacter);
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

					blocks.add(new Rectangle((float) i * Globals.TILE_SIZE, (float) j
							* Globals.TILE_SIZE, Globals.TILE_SIZE, Globals.TILE_SIZE));
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

			Point position = new Point(x,y);
			float width = cityTileMap.getObjectWidth(0, objectIndex);
			float height = cityTileMap.getObjectHeight(0, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000);


			// create new house. The house is added to the static houses array
			new House(objectIndex, position, width, height, money);

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
			Point position = new Point (x,y);

			float width = cityTileMap.getObjectWidth(1, objectIndex);
			float height = cityTileMap.getObjectHeight(1, objectIndex);

			// TODO: put this in the Banks class
			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000000);

			// Create new bank that is added to the static bank array
			new Bank(objectIndex, position, width, height, money);

			// add the bank to the banks Array
		}

		// TODO: SHOPS
		// Banks Object Group has index 1
		int shopsObjectCount = cityTileMap.getObjectCount(2);
		// create all Banks
		for (int objectIndex = 0; objectIndex < shopsObjectCount; objectIndex++) {
			int x = cityTileMap.getObjectX(2, objectIndex);
			int y = cityTileMap.getObjectY(2, objectIndex);
			Point position = new Point(x, y); 

			float width = cityTileMap.getObjectWidth(2, objectIndex);
			float height = cityTileMap.getObjectHeight(2, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 10000);

			// Create new Shop that is added to the static shop array
			new Shop(objectIndex, position, width, height, money);

			// the shop is added to the shops static ArrayList in the constructor
		}
		
		
		// HIGHWAY
		// ========================================================================================
		int highwayObjectCount = cityTileMap.getObjectCount(3);
		
		for (int objectIndex = 0; objectIndex < highwayObjectCount; objectIndex++) {
			int x = cityTileMap.getObjectX(3, objectIndex);
			int y = cityTileMap.getObjectY(3, objectIndex);
			Point position = new Point(x, y); 

			float width = cityTileMap.getObjectWidth(3, objectIndex);
			float height = cityTileMap.getObjectHeight(3, objectIndex);
			
			// create the rectangle of the highway
			Rectangle rect = new Rectangle(x,y,width,height);
			
			// Create new Shop that is added to the static shop array
			new Highway(position, rect);
		}
		
	}

	private final void initRobber() throws SlickException {
		if (userIsRobber)
			robber = new RobberUser();
		else
			robber = new RobberComputer();
	}

	private final void initPoliceOffice() throws SlickException {

		boolean userIsPolice = !userIsRobber; 
		this.policeOffice = new PoliceOffice(this.robber, userIsPolice);
	}

	/**
	 * Method needs to be called after the initMap method to make sure the banks
	 * array has already been filled
	 * 
	 * @throws SlickException
	 */
	private final void initSecurityGuards() throws SlickException {
		for (SecurityGuard sg : SecurityGuard.securityGuards) {
			sg.setRobber(this.robber);
		}
	}

	public void setMainCharacter(Person mainCharacter) {
		this.mainCharacter = mainCharacter;
		if (mainCharacter instanceof PolicemanUser){
			PolicemanUser police = (PolicemanUser) mainCharacter; 
			police.setSelected(true);
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
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);
		userIsRobber = Game.getInstance().getAccount().getIsRobber();
		initRobber();
		initPoliceOffice();
		setMainCharacter();
		start();
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);


		if (robber instanceof RobberComputer){
			RobberComputer temp = (RobberComputer) robber; 
			temp.stopTimers();
		}

		policeOffice.stopPolicemenPatrols();
	}

	// ===============================================================================================================================

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		// Draw Camera
		camera.draw(gameTime);

		boolean showRobber = (userIsRobber ||  PoliceOffice.robberVisibleCount > 0);
		// Draw Robber
		robber.draw(showRobber);

		// Draw Policemen
		policeOffice.draw();

		// Draw Security Guards
		SecurityGuard sg;
		for (int i = 0; i < SecurityGuard.securityGuards.size(); i++) {
			sg = SecurityGuard.securityGuards.get(i);
			sg.draw();
		}

		// Draw the Buildings 
		for (Building bldg : Building.buildings) { 
			bldg.draw((flagsShown[bldg.getFlag().flagID]));
		}
			
		// Draw the Highways
		for (Highway highway: Highway.highways){
			highway.draw();
		}

	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		Input input = gc.getInput();

		processInput(input);
	}

	/**
	 * Function that checks the input to the game be it Mouse Press or Keyboard
	 * Button
	 * 
	 * @param input
	 * @return
	 */
	private void processInput(Input input) {

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

			camera.getPlayerLog().clickButton();
			// user is police
			if (userIsRobber == false){

				int destX = input.getMouseX();
				int destY = input.getMouseY();


				// get the building
				Point pos = new Point(destX, destY);

				Building bldg = selectBuilding(pos);

				// display info for this building
				if (bldg != null) 
					bldg.setShowBuildingInfo(true);


				// save the previous policeman and deselect him later
				PolicemanUser prevPoliceman = (PolicemanUser) Play.getInstance().getMainCharacter();

				// get the policeman that is selected by the mouse
				PolicemanUser policeman = (PolicemanUser) selectPoliceman(destX, destY);

				// 
				if (policeman == null)
					return; 

				// set the isSelected to true
				policeman.setSelected(true);

				// Deselect the previous policeman
				prevPoliceman.setSelected(false); 

				// set the main character in the Play
				Play.getInstance().setMainCharacter(policeman);
			}

			else {
				// get the building
				Point pnt = new Point (input.getMouseX(), input.getMouseY());

				Building bldg = selectBuilding(pnt);

				if (bldg==null)
					return; 

				// display info for this building
				bldg.setShowBuildingInfo(true);


			}

		}

		policeOffice.processInput(input);

		// Move left, right, up, down		
		Movable movable = (Movable) mainCharacter;
		movable.processInput(input);



		if (input.isKeyDown(Input.KEY_ESCAPE)) {

			// go to the pause menu
			Game.getInstance().enterState(Globals.PAUSE);
		}
		else {
			robber.stop();
		}
	}

	private Building selectBuilding(Point pnt){
		Camera camera = Play.getInstance().getCamera();
		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + pnt.getX() - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + pnt.getY() - Globals.SELECTION_ERROR/2,
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
		Camera camera = Play.getInstance().getCamera();

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

	// GETTERS/SETTERS
	// ================================================================================================================================

	public Person getMainCharacter() {
		return mainCharacter;
	}

	/**
	 * Getter method for the camera
	 * @return camera
	 */
	public Camera getCamera() {
		return camera;
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

		// go to Game Over state
		Game.getInstance().enterState(Globals.GAME_OVER,new FadeOutTransition(), new FadeInTransition());
	}

	/**
	 * Play class will have ID 1 This method returns 1
	 */
	@Override
	public int getID() {
		return Globals.PLAY;
	}

	public void showFlag(int flagId){
		if (flagId < flagsShown.length && flagId >=0)
			flagsShown[flagId] = true; 
	}

	public void hideFlag(int flagId){
		if (flagId < flagsShown.length && flagId >=0)
			flagsShown[flagId] = false; 
	}

	public Robber getRobber() {
		return robber;
	}
}
