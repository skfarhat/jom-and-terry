package game;

import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.tiled.TiledMap;

public class Game extends BasicGame {
	private String cityTileMapPath = "/Users/Apple/Documents/new-eclipse-workspace/Slick Tutorial/res/city/city.tmx";
	private String playerSpriteSheet = "/Users/Apple/Documents/new-eclipse-workspace/Slick Tutorial/res/player1.png";


	private Integer TILE_SIZE = 16; 
	private TiledMap cityTileMap; 
	private Player player; 


	//	float spriteSheetWidth;
	//	float spriteSheetHeight;
	int spritesPerRow = 6;
	int spritesPerColumn = 2;

	int targetDelta = 16;	//msec

	int spriteWidth;
	int spriteHeight;


	// For Collision Detection
	ArrayList<Object> blockedObjects; 
	
	boolean blocked[][]; 

	public Game(String title) {
		super(title);
	}


	@Override
	public void init(GameContainer gc) throws SlickException {
		// Tile Map 
		initMap();

		// Player
		initPlayer(); 

		System.out.println(cityTileMap.getLayerCount());
	}

	private void initMap() throws SlickException
	{
		cityTileMap = new TiledMap(cityTileMapPath);
		System.out.println(cityTileMap.getHeight() + " " + cityTileMap.getWidth());

		//		Integer roadsID = cityTileMap.getLayerIndex("Roads"); 
		//		System.out.println("Roads ID : " + roadsID);

		Integer housesLayerID = cityTileMap.getLayerIndex("Houses"); 
//		System.out.println("Houses ID : " + housesLayerID);


		//puts tiles with blocked set true in the collision array
		blocked = new boolean[cityTileMap.getWidth()*TILE_SIZE][cityTileMap.getHeight()*TILE_SIZE];
		
		for (int x=0; x < blocked.length; x++) {
			for (int y=0; y < blocked.length;y++) {
				
				// need to check if the houses layer has a tile on the given x,y position
				int tileId = cityTileMap.getTileId(x/TILE_SIZE, y/TILE_SIZE, housesLayerID);

				if (tileId!=0)
				{
					// then there is a tile at that position
					blocked[x][y] = true; 
				}
			}
		}


//
//		/// For Visualizing the blocked array 
//		//		
//		int ymax = blocked[0].length; 
//		int xmax = blocked.length; 
//		System.out.println(String.format("xmax:%d, ymax:%d",xmax, ymax));
//		for (int i=0 ; i < xmax; i++)
//		{
//			for (int j=0; j < ymax; j++)
//			{
//				System.out.print(blocked[j][i]?"1 ":"0 ");
//			}
//			System.out.println();
//		}


	}

	private void initPlayer() throws SlickException {
		//Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(playerSpriteSheet);
		int spriteSheetWidth 	= spriteSheetImage.getWidth();
		int spriteSheetHeight 	= spriteSheetImage.getHeight();

		//Compute the width and height of the individual 
		// sprite images.
		spriteWidth = (int)(spriteSheetWidth/spritesPerRow);
		spriteHeight =(int)(spriteSheetHeight/spritesPerColumn);

		player = new Player(playerSpriteSheet, spriteWidth, spriteHeight);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();

		areKeysPressed(input);

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		cityTileMap.render(0, 0);

		//Draw the currently selected animation image at the
		// specified location
		player.currentAnimation.draw(player.getSpriteX(),player.getSpriteY());
		//		g.drawRect(player.spriteX, player.spriteY,  5, 5);

		int xPos= (int) ((player.getSpriteX() + player.spriteWidth)/TILE_SIZE) , yPos = (int)  ((player.getSpriteY()+player.spriteHeight)/TILE_SIZE);

		String positionString = String.format("(%d, %d)", xPos, yPos); 
		g.drawString(positionString, 0, 00);

		int X= (int) player.getSpriteX()  , Y = (int) player.getSpriteY();

		String positionS = String.format("(%d, %d)", X, Y); 
		g.drawString(positionS, 0, 20);
	}

	public static void main(String []args)
	{
		Game game = new Game("Jom & Terry");
		AppGameContainer container;
		try {
			container = new AppGameContainer(game);
			container.setDisplayMode(800, 800, false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	private boolean areKeysPressed(Input input) {


		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_DOWN)) 
		{

			if (input.isKeyDown(Input.KEY_RIGHT))
			{
				player.moveRight();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides())
				{
					player.spriteX--;
					player.rect.setX(player.spriteX);
				}
			}
			else if (input.isKeyDown(Input.KEY_LEFT))
			{
				player.moveLeft();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides())
				{
					player.spriteX++;
					player.rect.setX(player.spriteX);
				}
			}
			else if (input.isKeyDown(Input.KEY_UP))
			{
				player.moveUp();

				// if Player collides with an object 
				// increment the position back (reverse the position change)
				if (collides())
				{
					player.spriteY++;
					player.rect.setY(player.spriteY);
				}
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
				player.moveDown();

				// if Player collides with an object 
				// decrement the position back (reverse the position change)
				if (collides())
				{
					player.spriteY--;
					player.rect.setX(player.spriteY);
				} 
			}

			return true;
		}
		else {
			player.stop();
			return false;
		}

	}

	private boolean collides()
	{
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px) 
		
		int xBefore = (int)	(Math.floor(player.rect.getMinX()));
		int xAfter 	= (int) (Math.ceil(player.rect.getMaxX()));
		
		int yBefore = (int)	(Math.floor(player.rect.getMinY()));
		int yAfter	= (int) (Math.ceil(player.rect.getMaxY()));
		
		//		System.out.println(String.format("xpos,ypos: %d, %d", xPos, yPos));
		// if the player is trying to move beyond the bounds of the city (index < 0) or (index > maxWidth or maxHeight) 
		// return collision 
		if (xBefore < 0|| xAfter >= blocked.length || yBefore < 0|| yAfter >= blocked[0].length )
			return true; 
		

		boolean blockedBool =  blocked[xBefore][yBefore] || blocked[xBefore][yAfter]  || blocked[xAfter][yBefore]  || blocked[xAfter][yAfter]; 
		return blockedBool; 
	}
}
