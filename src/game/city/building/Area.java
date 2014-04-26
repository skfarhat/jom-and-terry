package game.city.building;

import game.Globals;
import game.city.road.Highway;
import game.city.road.Road;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;


public class Area {
	
	// TODO: make the loops only one loop
	private TiledMap areaTileMap;
	private Line[] mapBounds;
	private Gate exitGate = null; 
	private int numberOfRobbedBuildings = 0; 

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
	 * ArrayList containing all the created houses
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

	public Area(String areaMapPath) {
		try{
			initMap(areaMapPath);
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

		int bldgID = 0;
		// HOUSES
		// ========================================================================================
		int housesObjectCount = areaTileMap.getObjectCount(Globals.HOUSE_OBJECT_INDEX);

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

			houses.add(house);
			buildings.add(house);

			bldgID++;
		}

		// BANKS
		// ========================================================================================
		int banksObjectCount = areaTileMap.getObjectCount(Globals.BANK_OBJECT_INDEX);

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

			// add the bank to the banks array 
			banks.add(bank);
			buildings.add(bank);

			bldgID++;
		}

		int shopsObjectCount = areaTileMap.getObjectCount(Globals.SHOP_OBJECT_INDEX);
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

			shops.add(shop);
			buildings.add(shop);

			bldgID++;
		}

		// HIGHWAY
		// ========================================================================================
		int highwayObjectCount = areaTileMap.getObjectCount(Globals.HIGHWAY_OBJECT_INDEX);

		for (int objectIndex = 0; objectIndex < highwayObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = areaTileMap.getObjectWidth(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);
			float height = areaTileMap.getObjectHeight(Globals.HIGHWAY_OBJECT_INDEX, objectIndex);

			// create the rectangle of the highway
			Rectangle rect = new Rectangle(x, y, width, height);

			
			Highway highway = new Highway(position, rect);
			highways.add(highway);
		}

		// GATES
		// ==========================================================================================
		int gatesObjectCount = areaTileMap.getObjectCount(Globals.GATES_OBJECT_INDEX);

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
		this.exitGate = gates.get(randIndex);
		
		
		
		// ROAD
		// ==========================================================================================
		int roadsObjectCount = areaTileMap.getObjectCount(Globals.ROAD_OBJECT_INDEX);

		for (int objectIndex = 0; objectIndex < roadsObjectCount; objectIndex++) {
			int x = areaTileMap.getObjectX(Globals.ROAD_OBJECT_INDEX, objectIndex);
			int y = areaTileMap.getObjectY(Globals.ROAD_OBJECT_INDEX, objectIndex);
			Point position = new Point(x, y);

			float width = 	areaTileMap.getObjectWidth(Globals.ROAD_OBJECT_INDEX, objectIndex);
			float height =	areaTileMap.getObjectHeight(Globals.ROAD_OBJECT_INDEX, objectIndex);

			// create the rectangle of the road
			Rectangle rect = new Rectangle(x, y, width, height);

			Road road = new Road(position, rect); 
			
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
			// TODO: LEVEL UP
			// the player has finished the game
			exitGate.setOpen(true);

		}
	}

	public int getNumberOfRobbedBuildings() {
		return numberOfRobbedBuildings;
	}
	
	public void reset(){
		numberOfRobbedBuildings = 0; 
	}


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

}

