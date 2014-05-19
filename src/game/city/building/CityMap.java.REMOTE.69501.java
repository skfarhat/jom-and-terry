package game.city.building;

import game.Globals;
import game.city.person.Person;
import game.city.person.Policeman;
import game.city.person.Robber;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * Map needed for pathfinding utilities
 * 
 * @author michael
 * 
 */
public class CityMap implements TileBasedMap {

	/**
	 * A block's state
	 * 
	 */
	enum TileBlock {
		OPEN_TILE, POLICE_BLOCKED_TILE, ROBBER_BLOCKED_TILE, BLOCKED
	}

	private TiledMap map;
	private TileBlock[][] collisionMap;

	private int HEIGHT;
	private int WIDTH;

	/**
	 * Constructor
	 * 
	 * @param areaMapPath
	 */
	public CityMap(String areaMapPath) {

		try {
			map = new TiledMap(areaMapPath);
		} catch (SlickException e) {
		}
		WIDTH = map.getWidth();
		HEIGHT = map.getHeight();

		// double them
		collisionMap = new TileBlock[WIDTH][HEIGHT];

		initCollisionMap();

	}

	/**
	 * Build the collision map
	 */
	public void initCollisionMap() {

		// All open tiles initially
		for (int i = 0; i < WIDTH; i++)
			for (int j = 0; j < HEIGHT; j++)
				collisionMap[i][j] = TileBlock.OPEN_TILE;

		int policeCollisionObjects = map
				.getObjectCount(Globals.POLICE_COLL_OBJECT_INDEX);
		// Police Blocked Tiles
		for (int objectIndex = 0; objectIndex < policeCollisionObjects; objectIndex++) {
			int x = map.getObjectX(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int y = map.getObjectY(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int width = map.getObjectWidth(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int height = map.getObjectHeight(Globals.POLICE_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;

			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					collisionMap[x + i][y + j] = TileBlock.POLICE_BLOCKED_TILE;
		}

		int robberCollisionObjects = map
				.getObjectCount(Globals.ROBBER_COLL_OBJECT_INDEX);
		for (int objectIndex = 0; objectIndex < robberCollisionObjects; objectIndex++) {
			int x = map.getObjectX(Globals.ROBBER_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int y = map.getObjectY(Globals.ROBBER_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int width = map.getObjectWidth(Globals.ROBBER_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;
			int height = map.getObjectHeight(Globals.ROBBER_COLL_OBJECT_INDEX,
					objectIndex) / Globals.TILE_SIZE;

			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++) {

					if (collisionMap[x + i][y + j] == TileBlock.POLICE_BLOCKED_TILE)
						collisionMap[x + i][y + j] = TileBlock.BLOCKED;
				}
		}

		// printMap();
	}

	/**
	 * For debugging only, prints map to stdout
	 */
	public void printMap() {
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				TileBlock block = collisionMap[i][j];
				if (block == TileBlock.BLOCKED)
					System.out.print("x");
				else if (block == TileBlock.POLICE_BLOCKED_TILE)
					System.out.print("P");
				else if (block == TileBlock.ROBBER_BLOCKED_TILE)
					System.out.print("R");
				else
					System.out.print("0");
			}
			System.out.println();
		}
	}

	/**
	 * Checks if a Person is blocked
	 * 
	 * @param mover
	 * @return
	 */
	public boolean blocked(Mover mover) {
		Person person = (Person) mover;
		Rectangle rect = person.rect;
		int maxX = Math.round(rect.getMaxX() / Globals.TILE_SIZE);
		int minX = Math.round(rect.getMinX() / Globals.TILE_SIZE);
		int maxY = Math.round(rect.getMaxY() / Globals.TILE_SIZE);
		int minY = Math.round(rect.getMinY() / Globals.TILE_SIZE);

		if (maxX >= getWidthInTiles())
			maxX = getWidthInTiles() - 1;
		if (maxY >= getHeightInTiles())
			maxY = getHeightInTiles() - 1;
		if (maxY < 0)
			maxY = 0;
		if (maxX < 0)
			maxX = 0;

		// Check if map blocked (for both: Robber and Policeman)
		if (collisionMap[maxX][minY] == TileBlock.BLOCKED
				|| collisionMap[maxX][maxY] == TileBlock.BLOCKED
				|| collisionMap[minX][maxY] == TileBlock.BLOCKED
				|| collisionMap[minX][minY] == TileBlock.BLOCKED)
			return true;

		// Check if map blocked for Robber
		if (mover instanceof Robber) {
			if (collisionMap[maxX][minY] == TileBlock.ROBBER_BLOCKED_TILE
					|| collisionMap[maxX][maxY] == TileBlock.ROBBER_BLOCKED_TILE
					|| collisionMap[minX][maxY] == TileBlock.ROBBER_BLOCKED_TILE
					|| collisionMap[minX][minY] == TileBlock.ROBBER_BLOCKED_TILE)
				return true;
		}
		// Check if map blocked for Policeman
		else if (mover instanceof Policeman) {
			if (collisionMap[maxX][minY] == TileBlock.POLICE_BLOCKED_TILE
					|| collisionMap[maxX][maxY] == TileBlock.POLICE_BLOCKED_TILE
					|| collisionMap[minX][maxY] == TileBlock.POLICE_BLOCKED_TILE
					|| collisionMap[minX][minY] == TileBlock.POLICE_BLOCKED_TILE)
				return true;
		}

		// if neither make it not-blocked
		return false;
	}

	/**
	 * Used by pathfinder
	 */
	@Override
	public boolean blocked(PathFindingContext ctx, int x, int y) {
		Mover mover = ctx.getMover();
		if (mover instanceof Robber) {
			if (collisionMap[x][y] == TileBlock.BLOCKED)
				return true;
			if (collisionMap[x][y] == TileBlock.ROBBER_BLOCKED_TILE)
				return true;
			else
				return false;
		} else if (mover instanceof Policeman) {
			if (collisionMap[x][y] == TileBlock.BLOCKED)
				return true;
			if (collisionMap[x][y] == TileBlock.POLICE_BLOCKED_TILE)
				return true;
			else
				return false;
		}
		// if neither make it not-blocked
		else
			return false;
	}

	/**
	 * Used by pathfinder (graph weight)
	 */
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