package game.city.person;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * An abstract class for the Person/Player in the game.
 *
 * @author sami
 * 
 */
public abstract class Person {
	public static Integer PersonCount = 0 ; 
	public Integer ID; 
	public String name;
	public double velocity;	
	public Point position; 

	public Rectangle rect;

	private static String selectedImgPath 		= "res/selection.png";
	public static Image selectedImage;

	protected boolean isMoving = false;
	
	// Initialize the selection image
	static {
		try {
			selectedImage = new Image(selectedImgPath);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}


	//====================================================================================================
	//SpriteSheet
	//====================================================================================================
	Animation currentAnimation = null; 
	Animation rightWalkAnimation = null;
	Animation leftWalkAnimation = null;
	Animation downWalkAnimation = null;
	Animation upWalkAnimation = null;
	
	SpriteSheet spriteSheet; 
	// Dimensions a single sprite
	int spriteWidth;
	int spriteHeight;
	// Dimensions for the whole sheet containing all the sprites
	float spriteSheetWidth;
	float spriteSheetHeight;

	int spritesPerRow;
	int spritesPerColumn;
	//====================================================================================================

	/**
	 * Creates a person.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public Person (String name, double velocity) {
		this.name = name;
		this.velocity = velocity;
		this.ID = ++PersonCount; 		
	}

	protected void initSpriteSheet(String spriteSheetName, int spritesPerRow, int spritesPerColumn)  {
		try{
			//Get, save, and display the width and the height
			// of the sprite sheet.
			Image spriteSheetImage = new Image(spriteSheetName);
			int spriteSheetWidth 	= spriteSheetImage.getWidth();
			int spriteSheetHeight 	= spriteSheetImage.getHeight();

			//Compute the width and height of the individual 
			// sprite images.
			spriteWidth = (int)(spriteSheetWidth/spritesPerRow);
			spriteHeight =(int)(spriteSheetHeight/spritesPerColumn);

			this.spriteSheet = new SpriteSheet(spriteSheetImage, spriteWidth, spriteHeight);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
	}

	 public boolean isMoving() {
		return isMoving;
	}
}