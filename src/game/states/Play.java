package game.states;

import game.AudioGame;
import game.Game;
import game.Globals;
import game.city.Camera;
import game.city.building.Area;
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
import game.city.road.Road;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.Timer;

import org.json.simple.JSONObject;
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

@SuppressWarnings("unchecked")
public class Play extends BasicGameState implements Savable {
	enum FlagType {
		NONE, RED, DARK_BLUE, VIOLET, GREEN
	};

	// NONE, RED, DARK_BLUE, VIOLET, GREEN
	public boolean flagsShown[] = { true, true, true, true, true };

	// Singleton
	public static final Play play = new Play();

	public static Play getInstance() {

		return play;
	}

	private Area area;

	private Camera camera;
	private Integer gameTime = 0;

	// Characters
	private Robber robber;
	private PoliceOffice policeOffice;

	/**
	 * Main character can either be police or robber
	 */
	private Person mainCharacter = null;
	public boolean userIsRobber;
	private boolean isResumingGame = false;
	private boolean isPausingGame = false;
	private boolean isGameOver = false;
	private boolean isLevelUp = false;

	// =============================================================================================================================

	// INIT
	// ===============================================================================================================================
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
	}

	public void start() throws SlickException {
		userIsRobber = Game.getInstance().getAccount().getIsRobber();

		initRobber();
		initPoliceOffice();

		setMainCharacter();

		initCamera(); // Camera to center on the robber
		initSecurityGuards(); // Initialize Security Guards

		if (isResumingGame) {
			HashMap<Object, Object> resumeGame = Game.getInstance()
					.getAccount().getResumeGame();

			load(resumeGame);

			setResumingGame(false);
		}

		startGameTimer(); // Start timer

	}

	private final void initCamera() throws SlickException {
		camera = new Camera(area.getAreaTileMap(), mainCharacter);
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
		if (mainCharacter instanceof PolicemanUser) {
			PolicemanUser police = (PolicemanUser) mainCharacter;
			police.setSelected(true);
		}
		if (camera != null)
			camera.setPerson(mainCharacter);
	}

	public void setMainCharacter() {
		if (userIsRobber)
			setMainCharacter(robber);
		else
			setMainCharacter(policeOffice.getPoliceForceArray().get(0));
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.enter(container, game);

		if (!isPausingGame)
			start();

		isPausingGame = false;
	}

	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		super.leave(container, game);

		if (robber instanceof RobberComputer) {
			RobberComputer temp = (RobberComputer) robber;
			temp.stopTimers();
		}

		policeOffice.stopPolicemenPatrols();

		if (!isGameOver)
			// save the game
			Game.getInstance().getAccount().save();
	}

	public void pauseGame() {
		isPausingGame = true;
		// go to the pause menu
		Game.getInstance().enterState(Globals.PAUSE);
	}

	public void reset() {
		isPausingGame = false;
		gameTime = 0;
		mainCharacter = null;
		isResumingGame = false;
		isPausingGame = false;

		// remove buildings
		Building.buildings.clear();
		Shop.shops.clear();
		House.getHouses().clear();
		Bank.banks.clear();

		// reset the flags
		for (int i = 0; i < flagsShown.length; i++)
			flagsShown[i] = true;
	}

	// ===============================================================================================================================

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {

		// Draw Camera
		camera.draw(gameTime);

		boolean showRobber = (userIsRobber || PoliceOffice.robberVisibleCount > 0);

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
		for (Highway highway : Highway.highways) {
			highway.draw();
		}

		for (Road road: area.getRoads()){
			road.drawRoadInfo();
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
			if (userIsRobber == false) {

				int destX = input.getMouseX();
				int destY = input.getMouseY();

				// get the building
				Point pos = new Point(destX, destY);

				Building bldg = selectBuilding(pos);

				// display info for this building
				if (bldg != null)
					bldg.setShowBuildingInfo(true);

				// save the previous policeman and deselect him later
				PolicemanUser prevPoliceman = (PolicemanUser) Play
						.getInstance().getMainCharacter();

				// get the policeman that is selected by the mouse
				PolicemanUser policeman = (PolicemanUser) selectPoliceman(
						destX, destY);

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

				// SELECT BUILDING
				Point pnt = new Point(input.getMouseX(), input.getMouseY());
				Building bldg = selectBuilding(pnt);

				if (bldg != null) {
					bldg.setShowBuildingInfo(true);					// display info for this building
					return;
				}

				// SELECT ROAD
				Road road = selectRoad(pnt);
				if (road != null)
					road.setShowRoadInfo(true);
			}

		}

		policeOffice.processInput(input);

		// Move left, right, up, down
		Movable movable = (Movable) mainCharacter;
		movable.processInput(input);

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			pauseGame();
		}
	}

	private Building selectBuilding(Point pnt) {
		Camera camera = Play.getInstance().getCamera();
		// create a rectangle and use the intersect method to check whether
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(camera.getCameraX() + pnt.getX()
				- Globals.SELECTION_ERROR / 2, camera.getCameraY() + pnt.getY()
				- Globals.SELECTION_ERROR / 2, Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);

		for (Building bldg : Building.buildings) {
			if (bldg.getRect().intersects(rect)) {
				return bldg;
			}
		}
		return null;
	}

	private Road selectRoad(Point pnt) {
		Camera camera = Play.getInstance().getCamera();
		// create a rectangle and use the intersect method to check whether
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(camera.getCameraX() + pnt.getX()
				- Globals.SELECTION_ERROR / 2, camera.getCameraY() + pnt.getY()
				- Globals.SELECTION_ERROR / 2, Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);

		for (Road road : area.getRoads()) {
			if (road.getRect().intersects(rect)) {
				return road;
			}
		}
		return null;
	}

	/**
	 * @param destX
	 *            x-position of the mouse input
	 * @param destY
	 *            y-position of the mouse input
	 * @return a policeman object if the destX and destY passed are close to a
	 *         policeman in the map. Returns null otherwise
	 */
	private Policeman selectPoliceman(int destX, int destY) {
		Camera camera = Play.getInstance().getCamera();

		// create a rectangle and use the intersect method to check whether
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(camera.getCameraX() + destX
				- Globals.SELECTION_ERROR / 2, camera.getCameraY() + destY
				- Globals.SELECTION_ERROR / 2, Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);

		for (Policeman policeman : policeOffice.getPoliceForceArray()) {
			if (policeman.rect.intersects(rect)) {
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
	 * 
	 * @return camera
	 */
	public Camera getCamera() {
		return camera;
	}

	private void startGameTimer() {
		Timer timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameTime++;
			}
		});
		timer.start();
	}

	public void levelUp() {

		// return if already entered this function
		if (isLevelUp)
			return;

		AudioGame.playAsSound("success.ogg");
		isLevelUp = true;
		gameOver(true);
	}

	public void gameOver(boolean youWin) {

		if (isGameOver)
			return; 

		
		System.out.println("Should be entering state");
		GameOverState gameOver = (GameOverState) Game.getInstance().getState(Globals.GAME_OVER);
		
		System.out.println("userIsRobber: " + userIsRobber + "\nYouWin: " + youWin);
		gameOver.set(userIsRobber, youWin);
		
		Game.getInstance().enterState(Globals.GAME_OVER,
				new FadeOutTransition(), new FadeInTransition());
		
		
		isGameOver = true;
	}

	public void showFlag(int flagId) {
		if (flagId < flagsShown.length && flagId >= 0)
			flagsShown[flagId] = true;
	}

	public void hideFlag(int flagId) {
		if (flagId < flagsShown.length && flagId >= 0)
			flagsShown[flagId] = false;
	}

	public Robber getRobber() {
		return robber;
	}

	public void setResumingGame(boolean isResumingGame) {
		this.isResumingGame = isResumingGame;
	}

	public boolean isResumingGame() {
		return isResumingGame;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return area;
	}

	/**
	 * Play class will have ID 1 This method returns 1
	 */
	@Override
	public int getID() {
		return Globals.PLAY;
	}

	// SAVABLE
	// ================================================================================================================================

	@Override
	public JSONObject save() {

		// Save: Robber
		JSONObject robberObj = robber.save();

		// Save: Policemen
		JSONObject policeOfficeObj = policeOffice.save();

		// Save: Buildings
		JSONObject bldgsObj = Building.saveBuildings();

		// TODO: save highway information (numberof times passed...)
		// TODO: selected main character

		JSONObject map = new JSONObject();
		map.put(Globals.ROBBER, robberObj);
		map.put(Globals.POLICE_OFFICE, policeOfficeObj);
		map.put(Globals.BUILDINGS, bldgsObj);
		map.put(Globals.TIME, this.gameTime);
		map.put(Globals.TIME, this.gameTime);

		return map;
	}

	@Override
	public void load(Object loadObj) {

		HashMap<Object, Object> resumeGame = (HashMap<Object, Object>) loadObj;

		assert resumeGame != null : "There is some type of inconsistency in the resume game";

		gameTime = (int) resumeGame.get(Globals.TIME);

		// LOAD: Robber
		Object robberObj = resumeGame.get(Globals.ROBBER);
		robber.load(robberObj);

		// LOAD: PoliceOffice
		Object policeOfficeObj = resumeGame.get(Globals.POLICE_OFFICE);
		policeOffice.load(policeOfficeObj);

		// LOAD: Buildings
		Object buildingsObj = resumeGame.get(Globals.BUILDINGS);
		Building.loadBuildings(buildingsObj);
	}

}
