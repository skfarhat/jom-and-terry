package game.city.person;

import org.newdawn.slick.SpriteSheet;

/**
 * An abstract class for the Person/Player in the game.
 * 
 * @author sami
 * 
 */
public abstract class Person {
	public String name;
	public double velocity;

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