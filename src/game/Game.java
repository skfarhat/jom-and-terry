package game;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
	// ==============================================================================================================================
	// Paths
	// ==============================================================================================================================
	private String cityTileMapPath = "res/city/city.tmx";
	private String playerSpriteSheet = "res/player1.png";

public class Game extends StateBasedGame {

	int targetDelta = 16; // msec
	private Integer TILE_SIZE = 16;

	// Characters
	// ==============================================================================================================================
	private Player player; // Main Player
	private Policeman police1; // Policeman

	private TiledMap cityTileMap;

	// For Collision Detection
	// ==============================================================================================================================
	ArrayList<Rectangle> blocks;
	boolean blocked[][];

	// CONSTRUCTOR
	// ==============================================================================================================================
	public Game(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		blocks = new ArrayList<>(20);

		initMap(); // Tile Map
		initPlayer(); // Player
		initPoliceman(); // Policeman

	}

	private void initMap() throws SlickException {
		cityTileMap = new TiledMap(cityTileMapPath);

		// puts tiles with blocked set true in the collision array
		blocked = new boolean[cityTileMap.getWidth()][cityTileMap.getHeight()];

		for (int i = 0; i < blocked.length; i++) {
			for (int j = 0; j < blocked.length; j++) {

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

		// save all the houses in an ArrayList

		// for (int i=0; i < cityTileMap.)

		//
		// /// For Visualizing the blocked array
		// //
		// int ymax = blocked[0].length;
		// int xmax = blocked.length;
		// System.out.println(String.format("xmax:%d, ymax:%d",xmax, ymax));
		// for (int i=0 ; i < xmax; i++)
		// {
		// for (int j=0; j < ymax; j++)
		// {
		// System.out.print(blocked[j][i]?"1 ":"0 ");
		// }
		// System.out.println();
		// }
	}

	private void initPlayer() throws SlickException {
		// Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(playerSpriteSheet);
		int spriteSheetWidth = spriteSheetImage.getWidth();
		int spriteSheetHeight = spriteSheetImage.getHeight();

		// Compute the width and height of the individual
		// sprite images.
		spriteWidth = (int) (spriteSheetWidth / spritesPerRow);
		spriteHeight = (int) (spriteSheetHeight / spritesPerColumn);

		player = new Player(playerSpriteSheet, spriteWidth, spriteHeight);
	}

	private void initPoliceman() throws SlickException {
		// initial Position for the policeman
		int x = 400, y = 500;
		police1 = new Policeman(x, y, "Police-1", 50.0f);

	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
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
	private boolean processInput(Input input) {

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

			int destX = input.getMouseX();
			int destY = input.getMouseY();

			if (!isLocked(destX, destY)) {
				System.out.println(String.format("Go here (%d,%d)", destX,
						destY));

				police1.move(destX, destY);
				// // Move the policeman to this position
				// police1.direction = new Vector2f(destX - police1.xPos, destY
				// - police1.yPos); // set the direction to which the policeman
				// should go
				// police1.isMoving = true; // set the isMoving to true

			} else
				System.out.println("Is locked");
		}
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT)
				|| input.isKeyDown(Input.KEY_UP)
				|| input.isKeyDown(Input.KEY_DOWN)) {
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				player.moveRight();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides()) {
					player.spriteX--;
					player.rect.setX(player.spriteX);
				}
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				player.moveLeft();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides()) {
					player.spriteX++;
					player.rect.setX(player.spriteX);
				}
			} else if (input.isKeyDown(Input.KEY_UP)) {
				player.moveUp();

				// if Player collides with an object
				// increment the position back (reverse the position change)
				if (collides()) {
					player.spriteY++;
					player.rect.setY(player.spriteY);
				}
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				player.moveDown();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides()) {
					player.spriteY--;
					player.rect.setX(player.spriteY);
				}
			}

			return true;
		} else {
			player.stop();
			return false;
		}

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		cityTileMap.render(0, 0);

		// Draw the currently selected animation image at the
		// specified location
		player.currentAnimation.draw(player.getSpriteX(), player.getSpriteY());

		// Draw Policeman
		police1.draw();

	}

	private boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		boolean isInCollision = false;
		for (Rectangle ret : blocks) {
			if (player.rect.intersects(ret)) {
				isInCollision = true;
			}
		}

		return isInCollision;
	}

	public static void main(String[] args) {
		Game game = new Game("Jom & Terry");
		AppGameContainer container;
		try {
			container = new AppGameContainer(new Game(gamename));

			container.setDisplayMode(800, 800, false);

			// hide the FPS Text
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
