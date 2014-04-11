package game.city;

import game.Game;
import game.Globals;
import game.city.person.Person;
import game.menu.PlayerLog;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.tiled.TiledMap;

public class Camera {

	/** the map used for our scene */
	protected TiledMap map;

	/** the number of tiles in x-direction (width) */
	protected int numTilesX;

	/** the number of tiles in y-direction (height) */
	protected int numTilesY;

	/** the height of the map in pixel */
	protected int mapHeight;

	/** the width of the map in pixel */
	protected int mapWidth;

	/** the width of one tile of the map in pixel */
	protected int tileWidth;

	/** the height of one tile of the map in pixel */
	protected int tileHeight;

	/** the GameContainer, used for getting the size of the GameCanvas */
	protected GameContainer gc = Game.getInstance().getContainer();

	/** the x-position of our "camera" in pixel */
	protected float cameraX;

	/** the y-position of our "camera" in pixel */
	protected float cameraY;

	/** Information about the player's money and score */
	private PlayerLog playerLog;

	private Person person; 

	/**
	 * Create a new camera
	 * 
	 * @param gc the GameContainer, used for getting the size of the GameCanvas
	 * @param map the TiledMap used for the current scene
	 */
	public Camera(TiledMap map, Person person) {
		this.map = map;

		this.numTilesX = map.getWidth();
		this.numTilesY = map.getHeight();

		this.tileWidth = map.getTileWidth();
		this.tileHeight = map.getTileHeight();

		this.mapWidth = this.numTilesX * this.tileWidth;
		this.mapHeight = this.numTilesY * this.tileHeight;

		this.person = person; 

		Point position = new Point(Globals.APP_WIDTH-150, 0);
		this.playerLog = new PlayerLog(person, position);
	}

	/**
	 * "locks" the camera on the given coordinates. The camera tries to keep the location in it's center.
	 * 
	 * @param x the real x-coordinate (in pixel) which should be centered on the screen
	 * @param y the real y-coordinate (in pixel) which should be centered on the screen
	 */
	public void centerOn(float x, float y) {
		//try to set the given position as center of the camera by default
		cameraX = x - gc.getWidth()  / 2;
		cameraY = y - gc.getHeight() / 2;

		//if the camera is at the right or left edge lock it to prevent a black bar
		if(cameraX < 0) cameraX = 0;
		if(cameraX + gc.getWidth() > mapWidth) cameraX = mapWidth - gc.getWidth();

		//if the camera is at the top or bottom edge lock it to prevent a black bar
		if(cameraY < 0) cameraY = 0;
		if(cameraY + gc.getHeight() > mapHeight) cameraY = mapHeight - gc.getHeight();
	}

	/**
	 * "locks" the camera on the center of the given Rectangle. The camera tries to keep the location in it's center.
	 * 
	 * @param x the x-coordinate (in pixel) of the top-left corner of the rectangle
	 * @param y the y-coordinate (in pixel) of the top-left corner of the rectangle
	 * @param height the height (in pixel) of the rectangle
	 * @param width the width (in pixel) of the rectangle
	 */
	public void centerOn(float x, float y, float height, float width) {
		this.centerOn(x + width / 2, y + height / 2);
	}

	/**
	 * "locks the camera on the center of the given Shape. The camera tries to keep the location in it's center.
	 * @param shape the Shape which should be centered on the screen
	 */
	public void centerOn(Shape shape) {
		this.centerOn(shape.getCenterX(), shape.getCenterY());
	}

	/**
	 * draws the part of the map which is currently focussed by the camera on the screen
	 */
	public void drawMap() {
		this.drawMap(0, 0);
	}

	public void draw(int playTimer) {
		this.centerOn(person.rect);
		this.drawMap(0, 0);
		// Draw the money the Robber has
		this.playerLog.draw(playTimer);
		this.translateGraphics();
	}
	/**
	 * draws the part of the map which is currently focussed by the camera on the screen.<br>
	 * You need to draw something over the offset, to prevent the edge of the map to be displayed below it<br>
	 * Has to be called before Camera.translateGraphics() !
	 * @param offsetX the x-coordinate (in pixel) where the camera should start drawing the map at
	 * @param offsetY the y-coordinate (in pixel) where the camera should start drawing the map at
	 */

	public void drawMap(int offsetX, int offsetY) {
		//calculate the offset to the next tile (needed by TiledMap.render())
		int tileOffsetX = (int) - (cameraX % tileWidth);
		int tileOffsetY = (int) - (cameraY % tileHeight);

		//calculate the index of the leftmost tile that is being displayed
		int tileIndexX = (int) (cameraX / tileWidth);
		int tileIndexY = (int) (cameraY / tileHeight);

		//finally draw the section of the map on the screen
		map.render(   
				tileOffsetX + offsetX, 
				tileOffsetY + offsetY, 
				tileIndexX,  
				tileIndexY,
				(gc.getWidth()  - tileOffsetX) / tileWidth  + 1,
				(gc.getHeight() - tileOffsetY) / tileHeight + 1);
	}

	/**
	 * Translates the Graphics-context to the coordinates of the map - now everything
	 * can be drawn with it's NATURAL coordinates.
	 */
	public void translateGraphics() {
		gc.getGraphics().translate(-cameraX, -cameraY);
	}
	/**
	 * Reverses the Graphics-translation of Camera.translatesGraphics().
	 * Call this before drawing HUD-elements or the like
	 */
	public void untranslateGraphics() {
		gc.getGraphics().translate(cameraX, cameraY);
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public float getCameraX() {
		return cameraX;
	}
	public float getCameraY() {
		return cameraY;
	}

	public PlayerLog getPlayerLog() {
		return playerLog;
	}
}
