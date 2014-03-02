package game;

import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class TutorialGame extends BasicGame {

	private TiledMap tileMap; 
	private Player player; 


	private String ref = "/Users/Apple/Documents/new-eclipse-workspace/Slick Tutorial/src/slick/tutorial/tutorial-data/walk1.png";
	private String tileMapPath = "/Users/Apple/Documents/new-eclipse-workspace/Slick Tutorial/src/slick/tutorial/tutorial-data/tileset.tmx";

	//	float spriteSheetWidth;
	//	float spriteSheetHeight;
	int spritesPerRow = 6;
	int spritesPerColumn = 2;

	int targetDelta = 16;	//msec


	Animation rightWalkAnimation = null;
	Animation lefttWalkAnimation = null; 

	int spriteWidth;
	int spriteHeight;

	
	public TutorialGame(String title) {
		super(title);

	}

	@Override
	public void render(GameContainer arg0, Graphics g) throws SlickException {
		g.setColor(Color.red); // Preset Color Red
		g.setColor(new Color(0,255,150)); // A  yucky green defined using three integers
		g.setColor(new Color(1.0f,0.5f,0.5f,0.8f)); // A light purple with 80% transparency defined using four floats
		g.setColor(new Color(0x7b2900)); // A brown-reddish defined using hex
		g.drawString("Hello World!",200,200);

		tileMap.render(0, 0);

		g.setDrawMode(g.MODE_NORMAL);
		g.setBackground(Color.gray);

		//Draw the currently selected animation image at the
		// specified location
//		player.currentAnimation.draw(player.spriteX,player.spriteY);

	}

	@Override
	public void init(GameContainer gc) throws SlickException {

		// Tile Map 
		tileMap = new TiledMap(tileMapPath);

		// Player
		initPlayer(); 

	}
	private void initPlayer() throws SlickException {
		//Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(ref);
		int spriteSheetWidth 	= spriteSheetImage.getWidth();
		int spriteSheetHeight 	= spriteSheetImage.getHeight();

		//Compute the width and height of the individual 
		// sprite images.
		spriteWidth = (int)(spriteSheetWidth/spritesPerRow);
		spriteHeight =(int)(spriteSheetHeight/spritesPerColumn);

		player = new Player(ref, spriteWidth, spriteHeight);
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//Note that the following is for clock time display
		// only. It does not effect the animation.

		Input input = gc.getInput();

		areKeysPressed(input);

	}

	public static void main(String[] args) {

		TutorialGame game = new TutorialGame("Jom & Terry");
		AppGameContainer container;
		try {
			container = new AppGameContainer(game);
			container.setDisplayMode(1300, 800, false);
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
			}
			else if (input.isKeyDown(Input.KEY_LEFT))
			{
				player.moveLeft();
			}
			else if (input.isKeyDown(Input.KEY_UP))
			{
//				player.spriteY--;
			}
			else if (input.isKeyDown(Input.KEY_DOWN))
			{
//				player.spriteY++;
			}
			return true;
		}
		else {
			player.stop();
			return false;
		}
			
	}
}
