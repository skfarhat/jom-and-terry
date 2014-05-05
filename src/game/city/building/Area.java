package game.city.building;

import game.Game;
import game.Globals;
import game.city.Camera;
import game.city.person.PoliceOffice;
import game.city.person.Robber;
import game.city.person.RobberComputer;
import game.city.person.RobberUser;
import game.city.person.SecurityGuard;
import game.city.road.Highway;
import game.city.road.Road;
import game.states.Savable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

@SuppressWarnings("unchecked")
public class Area implements Savable{

	// TODO: make the loops only one loop
	private TiledMap areaTileMap;
	private CityMap cityMap; 
	private Line[] mapBounds;
	private Gate exitGate = null; 
	private int numberOfRobbedBuildings = 0; 
	protected int level; 

	private int totalPossibleScore = 0; 
	protected Camera camera;

	protected Robber robber; 
	protected PoliceOffice policeOffice; 

	/**
	 * Array containing all the security guards created in the current area
	 */
	private ArrayList<SecurityGuard> securityGuards = new ArrayList<>(10);
	/**
	 * An array containing all the buildings created in the current area
	 */
	private ArrayList<Building> buildings = new ArrayList<>(20);
	/**
	 * ArrayList that contains all the created banks
	 */
	private ArrayList<Bank> banks = new ArrayList<>(10);
	/**
	 * ArrayList containing all the created shops
	 */
	private ArrayList<Shop> shops = new ArrayList<>(10); 
	/**
	 */
	private ArrayList<House> houses = new ArrayList<>(10);
	/**
	 * ArrayList containing all the created highways
	 */
	private ArrayList<Highway> highways = new ArrayList<>(10);
	/**
	 * ArrayList containing all the created gates. One gate will open when the user has robbed all the buildings
	 */
	private ArrayList<Gate> gates = new ArrayList<>(10);	
	/**
	 * ArrayList containing all the road objects.
	 */
	private ArrayList<Road> roads = new ArrayList<>(10);

	/**
	 * Constructor
	 * @param level
	 * @param cityMapPath
	 */
	public Area(int level) {
		this.level = level; 
		final String cityMapPath = Globals.cityTMXPaths[level-1];	// (levels start at 1)

		if (level <= 0 || cityMapPath == null)	// TODO: throw Exception
			return; 

		try {
			cityMap = new CityMap(cityMapPath);
			initMap(cityMapPath);

			boolean userIsRobber = Game.getInstance().getAccount().getIsRobber();
			boolean userIsPolice = !userIsRobber;

			robber = (userIsRobber)? new RobberUser(this): new RobberComputer(this);	// Create Robber
			policeOffice = new PoliceOffice(this, robber, userIsPolice);				// Create Police Office		
			camera = new Camera(cityMap);												// Create Camera

			/*
			 * Set the robber for all the security guards
			 * Can't pass robber in the constructor of Security Guards 
			 * because they are created befrore the robber is
			 * along with all the buildings (see initMap())
			 */
			for (Bank bank: banks)
				for (SecurityGuard sg : bank.getSecurityGuards())
					sg.setRobber(robber);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
	}

	private final void initMap(String areaMapPath) throws SlickException {
		areaTileMap = new TiledMap(areaMapPath);

		float mapWidth = areaTileMap.getWidth() * areaTileMap.getTileWidth();
		float mapHeight = areaTileMap.getHeight() * areaTileMap.getTileHeight();
		// init the mapBounds
		Line line1 = new Line(-1, -1, // top left to top right
				mapWidth, -1);
		Line line2 = new Line(-1, -1, // top left to bottom left
				-1, mapHeight);
		Line line3 = new Line(mapWidth, mapHeight, // bottom right to top right
				mapWidth, -1);
		Line line4 = new Line(mapWidth, mapHeight, // bottom right to bottom
				// left
				-1, mapHeight);

		mapBounds = new Line[] { line1, line2, line3, line4 };

		// HOUSES
		// ========================================================================================
		int housesObjectCount = areaTileMap.getObjectCount(Globals.HOUSE_OBJECT_INDEX);
		int banksObjectCount = areaTileMap.getObjectCount(Globals.BANK_OBJECT_INDEX);
		int shopsObjectCount = areaTileMap.getObjectCount(Globals.SHOP_OBJECT_INDEX);
		int highwayObjectCount = areaTileMap.getObjectCount(Globals.HIGHWAY_OBJECT_INDEX);
		int gatesObjectCount = areaTileMap.getObjectCount(Globals.GATES_OBJECT_INDEX);
		int roadsObjectCount = areaTileMap.getObjectCount(Globals.ROAD_OBJECT_INDEX);
  
		int bldgID = 0;

		// create all Houses
		for (int objectIndex = 0; objectIndex < housesObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.HOUSE_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.HOUSE_OBJECT_INDEX, objectIndex);

			Point position = new Point(x, y);
			float width = areaTileMap.getObjectWidth(Globals.HOUSE_OBJECT_INDEX, objectIndex);
			float height = areaTileMap.getObjectHeight(Globals.HOUSE_OBJECT_INDEX, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000);

			// create new house. The house is added to the static houses array
			House house = new House(bldgID, this, position, width, height, money);

			// add the score possible from the house to the total possible score
			totalPossibleScore+=house.score;

			houses.add(house);
			buildings.add(house);
 
			bldgID++;
		}

		// BANKS
		// ========================================================================================


		// create all Banks
		for (int objectIndex = 0; objectIndex < banksObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.BANK_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.BANK_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = areaTileMap.getObjectWidth(Globals.BANK_OBJECT_INDEX, objectIndex);
			float height = areaTileMap.getObjectHeight(Globals.BANK_OBJECT_INDEX, objectIndex);

			// TODO: put this in the Banks class
			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 1000000);

			// Create new bank that is added to the static bank array
			Bank bank = new Bank(bldgID, this, position, width, height, money);

			// add the score possible from the bank to the total possible score
			totalPossibleScore+=bank.score;

			// add the bank to the banks array 
			banks.add(bank);
			buildings.add(bank);


			// add the bank's security guards to the securityGuards array
			securityGuards.addAll(bank.getSecurityGuards());

			bldgID++;
		}


		// create all Banks
		for (int objectIndex = 0; objectIndex < shopsObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.SHOP_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.SHOP_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = areaTileMap.getObjectWidth(Globals.SHOP_OBJECT_INDEX, objectIndex);
			float height = areaTileMap.getObjectHeight(Globals.SHOP_OBJECT_INDEX, objectIndex);

			// some random number (1000 ? 3000 ? ...) and need it to be positive
			// thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10) * 10000);

			// Create new Shop that is added to the static shop array
			Shop shop = new Shop(bldgID, this, position, width, height, money);

			// add the score possible from the shop to the total possible score
			totalPossibleScore+=shop.score;

			shops.add(shop);
			buildings.add(shop);

			bldgID++;
		}

		// HIGHWAY
		// ========================================================================================

		for (int objectIndex = 0; objectIndex < highwayObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = areaTileMap.getObjectWidth(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			float height = areaTileMap.getObjectHeight(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);

			// create the rectangle of the highway
			Rectangle rect = new Rectangle(x, y, width, height);


			Highway highway = new Highway(this, position, rect);
			highways.add(highway);
 
		}

		// GATES
		// ==========================================================================================


		for (int objectIndex = 0; objectIndex < gatesObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.GATES_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.GATES_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = 	areaTileMap.getObjectWidth(Globals.GATES_OBJECT_INDEX, objectIndex);
			float height =	areaTileMap.getObjectHeight(Globals.GATES_OBJECT_INDEX, objectIndex);

			// create the rectangle of the highway
			Rectangle rect = new Rectangle(x, y, width, height);

			Gate gate = new Gate(position, rect);

			gates.add(gate);

		}

		// randomly choose a gate as the exit game

		int randIndex = new Random(System.currentTimeMillis()).nextInt(gates.size()); 
		exitGate = gates.get(randIndex);



		// ROAD
		for (int objectIndex = 0; objectIndex < roadsObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.ROAD_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.ROAD_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = 	areaTileMap.getObjectWidth(Globals.ROAD_OBJECT_INDEX, objectIndex);
			float height =	areaTileMap.getObjectHeight(Globals.ROAD_OBJECT_INDEX, objectIndex);

			// create the rectangle of the road
			Rectangle rect = new Rectangle(x, y, width, height);

			Road road = new Road(this, position, rect); 

			roads.add(road);
		}
	}

	public Line[] getMapBounds() {
		return mapBounds;
	}

	public TiledMap getAreaTileMap() {
		return areaTileMap;
	}

	public void incrementNumberOfRobbedBuildings(){
		numberOfRobbedBuildings++; 

		if (numberOfRobbedBuildings == buildings.size()){
			// Exit Gate is open the player can finish the level
			exitGate.setOpen(true);

		}
	}

	public int getNumberOfRobbedBuildings() {
		return numberOfRobbedBuildings;
	}

	public void reset(){
		numberOfRobbedBuildings = 0; 
	}

	// Getters/Setters
	public Gate getExitGate() {
		return exitGate;
	}
	public ArrayList<Gate> getGates() {
		return gates;
	}
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	public ArrayList<Road> getRoads() {
		return roads;
	}
	public ArrayList<SecurityGuard> getSecurityGuards() {
		return securityGuards;
	}
	public ArrayList<House> getHouses() {
		return houses;
	}
	public ArrayList<Bank> getBanks() {
		return banks;
	}
	public ArrayList<Shop> getShops() {
		return shops;
	}
	public Robber getRobber() {
		return robber;
	}
	public PoliceOffice getPoliceOffice() {
		return policeOffice;
	}
	public int getLevel() {
		return level;
	}
	public Camera getCamera() {
		return camera;
	}	
	public CityMap getCityMap() {
		return cityMap;
	}
	public int getTotalPossibleScore() {
		return totalPossibleScore;
	}
	// Savable
	@Override
	public JSONObject save() {
		JSONObject saveObject = new JSONObject(); 

		// Save: Robber
		JSONObject robberObj = getRobber().save();

		// Save: Policemen
		JSONObject policeOfficeObj = getPoliceOffice().save();

		// Save: Buildings
		JSONObject bldgObjects = new JSONObject();
		for (Building bldg: buildings){
			JSONObject bldgObject = bldg.save(); 
			bldgObjects.put(bldg.ID, bldgObject);
		}

		saveObject.put(Globals.BUILDINGS, bldgObjects);
		saveObject.put(Globals.ROBBER, robberObj);
		saveObject.put(Globals.POLICE_OFFICE, policeOfficeObj);

		// Save: Level
		saveObject.put(Globals.LEVEL, getLevel());
		return saveObject; 
	}
	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;
		HashMap<Object, Object> bldgsMap = (HashMap<Object, Object>) map.get(Globals.BUILDINGS);


		// LOAD: Robber
		Object robberObj = map.get(Globals.ROBBER);
		getRobber().load(robberObj);

		// LOAD: PoliceOffice
		Object policeOfficeObj = map.get(Globals.POLICE_OFFICE);
		getPoliceOffice().load(policeOfficeObj);

		numberOfRobbedBuildings = 0; 
		for (Building bldg: buildings){
			HashMap<Object, Object> bldgMap = (HashMap<Object, Object> ) bldgsMap.get(""+ bldg.ID);
			bldg.load(bldgMap);
			if (bldg.getIsCompletelyRobbed())
				numberOfRobbedBuildings++;
		}
	}
}

