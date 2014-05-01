package game.city.building;

import game.Globals;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class CityMap implements TileBasedMap {
	private static final int WIDTH = 10;
	private static final int HEIGHT = 10;

	private TiledMap map;
	private int[][] MAP; 

	public CityMap(String areaMapPath) throws SlickException {

		map = new TiledMap(areaMapPath);
		MAP = new int[map.getWidth()][map.getHeight()];

		int housesObjectCount = map.getObjectCount(0);
		for (int objectIndex = 0; objectIndex < housesObjectCount; objectIndex++) {
			int x = map.getObjectX(Globals.HOUSE_OBJECT_INDEX, objectIndex)/Globals.TILE_SIZE;
			int y = map.getObjectY(Globals.HOUSE_OBJECT_INDEX, objectIndex)/Globals.TILE_SIZE;
			String prop3 = map.getObjectProperty(0, objectIndex, "Passable", "true");

			int width  = map.getObjectWidth(Globals.HOUSE_OBJECT_INDEX, objectIndex)/Globals.TILE_SIZE;
			int height  = map.getObjectHeight(Globals.HOUSE_OBJECT_INDEX, objectIndex)/Globals.TILE_SIZE;
			
			System.out.println("x:" + x + " y: " + y + " " + prop3);
			
			int toPut = (prop3.equals("true"))? 1: 0;  
			
			for (int i=0; i < width; i++)
				for (int j=0; j < height; j++)
					MAP[x+i][y+j] = toPut; 

		}

		for(int x=0; x<map.getWidth(); x++)
		{
			for(int y=0; y<map.getHeight(); y++)
			{
				System.out.print(MAP[y][x]);
			}
			System.out.println();
		}

	}
	@Override
	public boolean blocked(PathFindingContext ctx, int x, int y) {
		return MAP[y][x] == 0;
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
	public void pathFinderVisited(int x, int y) {}

	public int getWidth() {
		return map.getWidth();
	}

	public int getHeight() {
		return map.getHeight();
	}

	public int getTileWidth(){
		return map.getTileWidth(); 
	}

	public int getTileHeight(){
		return map.getTileHeight(); 
	}

	public void render(int x, int y, int sx, int sy, int width, int height){
		map.render(x, y, sx, sy, width, height);
	}
}