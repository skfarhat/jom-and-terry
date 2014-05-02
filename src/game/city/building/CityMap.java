package game.city.building;

import game.Globals;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class CityMap implements TileBasedMap {

	private TiledMap map;
	private boolean[][] MAP;

	private int HEIGHT; 
	private int WIDTH; 
	
	public CityMap(String areaMapPath) throws SlickException {

		map = new TiledMap(areaMapPath);
		WIDTH 	= map.getWidth(); 
		HEIGHT 	= map.getHeight();
		
		// double them 
//		WIDTH *=1;
//		HEIGHT *=1;
		MAP = new boolean[WIDTH][HEIGHT];
		
		initCollisionMap();

	}

	//	quality control
	/*
	 * sami.kfarhat@gmail.com
	 * Offer: quality control consultant beirut
	 * repor
	 * 2nd june
	 * unlimited period
	 * start date
	 * salary: $27000 12 equal months
	 * bonus
	 * 2014: salary decemeber 2015
	 * eligibility of bonus 
	 * allowance 5000LL
	 * 8000LL
	 * no taxes
	 * parking allowance ($100) goes into salary 105$ (taxes)
	 * lunch tickets 5000LL, coupons 
	 * benefits medical insurance 1st class (part of dental and eye)
	 * 3 month eligible allicot ??
	 * ADSL free murex 
	 * 25 working days 
	 * at least 1 year
	 *  
	 * may 09  
	 * 
	 */
	public void initCollisionMap(){

		// Collision only for Houses
		int collisionObjectCount = map.getObjectCount(Globals.POLICE_COLL_OBJECT_INDEX);

		for (int objectIndex = 0; objectIndex < collisionObjectCount; objectIndex++) {
			int x = map.getObjectX(Globals.POLICE_COLL_OBJECT_INDEX, objectIndex)
					/ Globals.TILE_SIZE;
			int y = map.getObjectY(Globals.POLICE_COLL_OBJECT_INDEX, objectIndex)
					/ Globals.TILE_SIZE;
			int width = map.getObjectWidth(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int height = map.getObjectHeight(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			//			int x = map.getObjectX(Globals.POLICE_COLL_OBJECT_INDEX, objectIndex);
			//			int y = map.getObjectY(Globals.POLICE_COLL_OBJECT_INDEX, objectIndex);
			//			int width = map.getObjectWidth(Globals.POLICE_COLL_OBJECT_INDEX,
			//					objectIndex) / Globals.TILE_SIZE;
			//			int height = map.getObjectHeight(Globals.POLICE_COLL_OBJECT_INDEX,
			//					objectIndex) / Globals.TILE_SIZE;

			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					MAP[x + i][y + j] = true;

		}

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				System.out.print((MAP[x][y])?"1":"0");
			}
			System.out.println();
		}
	}

	@Override
	public boolean blocked(PathFindingContext ctx, int x, int y) {
		return MAP[x][y];
	}

	@Override
	public float getCost(PathFindingContext ctx, int x, int y) {
		return 1.0f;
	}

	@Override
	public int getHeightInTiles() {
		return HEIGHT;
	}

	@Override
	public int getWidthInTiles() {
		return WIDTH; 
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

	public int getWidth() {
		return WIDTH; 
	}

	public int getHeight() {
		return HEIGHT;
	}

	public int getTileWidth() {
		return map.getTileWidth();
	}

	public int getTileHeight() {
		return map.getTileHeight();
	}

	public void render(int x, int y, int sx, int sy, int width, int height) {
		map.render(x, y, sx, sy, width, height);
	}
}