package game.city.person;

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
	public String name;
	public double velocity;	
	public Point position; 
	
	public Rectangle rect;

	private static String selectedImgPath 		= "res/selection.png";
	public static Image selectedImage;

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
	}
	
}