package game;

import game.city.building.House;
import game.city.person.Policeman;
import game.city.person.Robber;

import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class Play extends BasicGameState{
	private String cityTileMapPath = "res/city/city.tmx";
	
	// The user can choose to be the robber or the policeman before he starts the game
	boolean userIsRobber = true;
	
	int spritesPerRow = 6;
	int spritesPerColumn = 2;
	int spriteWidth;
	int spriteHeight;

	int targetDelta = 16;	//msec
	private Integer TILE_SIZE = 16;

	// Characters
	// ==============================================================================================================================		
	private Robber robber; 		// Main Player 
	private Policeman police1; 	// Policeman 

	public ArrayList<Policeman> policeForceArray; 

	private TiledMap cityTileMap;


	// For Collision Detection
	// ==============================================================================================================================
	ArrayList<Rectangle> blocks; 
	boolean blocked[][]; 

	
	// Buildings
	// ==============================================================================================================================
	ArrayList<House> houses; 

	// CONSTRUCTOR
	// ==============================================================================================================================
	public Play(int state)
	{

	}
	

	public void init(GameContainer gc, StateBasedGame sbg) throws SlickException
	{
		blocks = new ArrayList<>(20);

		initMap();			// Tile Map
		initRobber();		// Robber 
		initPoliceman();	// Policeman

		// pass instance of police force to Robber
		this.robber.setPoliceForceArray(policeForceArray);
	}

	private void initMap() throws SlickException
	{
		cityTileMap = new TiledMap(cityTileMapPath);

		//puts tiles with blocked set true in the collision array
		blocked = new boolean[cityTileMap.getWidth()][cityTileMap.getHeight()];

		for (int i=0; i < blocked.length; i++) {
			for (int j=0; j < blocked.length;j++) {

				// need to check if the houses layer has a tile on the given x,y position
				// TODO: WHY 1 ? ID of what ? 
				int tileId = cityTileMap.getTileId(i, j, 1);

				if (tileId!=0)
				{
					// then there is a tile at that position
					blocked[i][j] = true; 

					blocks.add(new Rectangle((float)i * TILE_SIZE, (float)j * TILE_SIZE, TILE_SIZE, TILE_SIZE));
				}
			}
		}
		
		// HOUSES
		// ========================================================================================
		// Houses Object Group has index 0 
		int housesObjectCount = cityTileMap.getObjectCount(0);
		
		houses = new ArrayList<>(10);
		
		// create all Houses 
		for (int objectIndex=0; objectIndex < housesObjectCount; objectIndex++)
		{
			int x = cityTileMap.getObjectX(0, objectIndex);
			int y = cityTileMap.getObjectY(0, objectIndex);
			float width = cityTileMap.getObjectWidth(0, objectIndex);
			float height = cityTileMap.getObjectHeight(0, objectIndex);
			
			// some random number (1000 ? 3000 ? ...) and need it to be positive thus the Math.absolute
			int money = Math.abs(((new Random()).nextInt() % 10 ) * 1000); 
			House house = new House(x, y, width, height, money);
			
			// add the house to the houses Array
			houses.add(house);			
		}

	}

	private void initRobber() throws SlickException {
		
		robber = new Robber(userIsRobber); 
		
	}

	private void initPoliceman() throws SlickException {
		// initial Position for the policeman
		int x = 400, y =500;
		police1 = new Policeman(this.robber, x, y, "Police-1", 50.0f);


		// init the Police Force Array
		policeForceArray = new ArrayList<>(5); 
		policeForceArray.add(police1); // add Police1 to the force
	}

	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		
		// Render the City Map
		cityTileMap.render(0, 0);

		//Draw Robber
		robber.draw();

		// Draw Policeman
		police1.draw();
		
		
		// Draw the Money above the houses 
		int money = -1; 
		String moneyStr ;
		House house;
		for (int i=0; i< houses.size(); i++)
		{
			house = houses.get(i);
			money = house.money; 
			moneyStr = String.format("$%d", money);	
			g.drawString(moneyStr, house.xPos, house.yPos);
		}
	}


	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
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

		boolean square = blocked[(int)x/TILE_SIZE][(int)y/TILE_SIZE];

		// The cell is locked if the square if filled 
		return square; 
	}

	/**
	 * Function that checks the input to the game be it Mouse Press or Keyboard Button
	 * @param input
	 * @return
	 */
	private boolean processInput(Input input) {

		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

			int destX = input.getMouseX();
			int destY = input.getMouseY();

			if (!isLocked(destX, destY)) {
				// Move the policeman to this position
				police1.move(destX, destY);			
			}
			else
				System.out.println("Is locked");
		}
		if (input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_DOWN)) 
		{
			if (input.isKeyDown(Input.KEY_RIGHT))
			{
				robber.moveRight();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides())
				{
					robber.xPos--;
					robber.rect.setX(robber.xPos);
				}
			}
			else if (input.isKeyDown(Input.KEY_LEFT))
			{
				robber.moveLeft();

				// if Player collides with an object
				// decrement the position back (reverse the position change)
				if (collides())
				{
					robber.xPos++;
					robber.rect.setX(robber.xPos);
				}
			}
			else if (input.isKeyDown(Input.KEY_UP))
			{
				robber.moveUp();

				// if Player collides with an object 
				// increment the position back (reverse the position change)
				if (collides())
				{
					robber.yPos++;
					robber.rect.setY(robber.yPos);
				}
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
				robber.moveDown();

				// if Player collides with an object 
				// decrement the position back (reverse the position change)
				if (collides())
				{
					robber.yPos--;
					robber.rect.setX(robber.yPos);
				} 
			}

			return true;
		}
		else {
			robber.stop();
			return false;
		}

	}
	
	private boolean collides()
	{
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px) 

		boolean isInCollision = false;
		for(Rectangle ret : blocks) {
			if(robber.rect.intersects(ret)) {
				isInCollision = true;
			}
		}

		return isInCollision;
	}

	public static void main(String []args)
	{
		Game game = new Game("Jom & Terry");
		AppGameContainer container;
		try {
			container = new AppGameContainer(game);
			container.setDisplayMode(800, 800, false);

			// hide the FPS Text
			container.setShowFPS(false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
	}

	/**
	 * Play class will have ID 1
	 * This method returns 1
	 */
	@Override
	public int getID() {
		return 1;
	}

}
