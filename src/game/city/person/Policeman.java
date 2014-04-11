package game.city.person;

import game.Globals;
import game.states.Play;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public class Policeman extends Person{

	
	// TODO: Put in globals
	protected static float visionDistance = 130.0f;	// Vision Attribute

	private static String policemanImgPath 			= "res/police1.png";
	private static String policeSpriteSheet 			= "res/Spritesheets/police.png";
	//=================================================================
	private Image image;

	protected float score = 0; 
	protected Point destPoint; 

	public Vector2f direction; 
	//=================================================================
	// Behavior
	protected boolean userIsPolice; 
	protected boolean isMoving = false;
	protected boolean robberIsVisible = false;
	//=================================================================
	//  
	public Robber robber; 

	protected Circle suspectRegion; 		

	// TODO: the following 2 should be 'constants'
	// Animations
	Animation currentAnimation = null; 
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null;
	//=================================================================

	/**
	 * CONSTRUCTOR
	 * Creates a policeman.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 * @throws SlickException
	 */
	public Policeman(Robber robber,  Point position, String name,
			double velocity) throws SlickException {

		// call superclass constructor (Person)
		super(name, velocity);

		// set the Sprite Sheet
		this.initSpriteSheet();

		// Set the position of the policeman
		this.position= position; 

		// set the rectangle of the player 
		this.rect = new Rectangle(position.getX(), position.getY(), spriteWidth, spriteHeight);

		// Set the image of the policeman
		this.image = new Image(policemanImgPath);

		this.robber = robber;

		// initially the player is moving to the right
		//Create a new animation based on a selection of
		// sprites from the sprite sheet.
		int duration = 200;
		currentAnimation =  rightWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				0,//first row
				5,//last column
				0,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);
		leftWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				1,//first row
				5,//last column 
				1,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);

		currentAnimation.stop();  
	}

	private void initSpriteSheet() throws SlickException {

		spritesPerRow = 6;
		spritesPerColumn = 2;

		//Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(policeSpriteSheet);
		int spriteSheetWidth 	= spriteSheetImage.getWidth();
		int spriteSheetHeight 	= spriteSheetImage.getHeight();

		//Compute the width and height of the individual 
		// sprite images.
		spriteWidth = (int)(spriteSheetWidth/spritesPerRow);
		spriteHeight =(int)(spriteSheetHeight/spritesPerColumn);

		this.spriteSheet = new SpriteSheet(spriteSheetImage, spriteWidth, spriteHeight);

	}

	/**
	 * Returns the image of the policeman
	 * 
	 * @return image
	 * 
	 */
	public Image getImage() {
		return image;
	}

	public float getScore() {
		return score;
	}

	public void draw() {
		// draw the image at the positon of the policeman
		this.image.draw(this.position.getX(), this.position.getY());

	}

	/**
	 * Attempt to arrest a robber.
	 * 
	 * @param robber
	 * @return whether the arrest was successful.
	 */
	public boolean arrestRobber(Robber robber) {
		
		float distance = (float)  Math.sqrt(Math.pow(
				this.position.getX()-this.robber.position.getX(), 2.0) 
				+ Math.pow(this.position.getY()-this.robber.position.getY(), 2.0)
				);
		
		// arrest only if he is less than some distance away
		if (distance < Globals.ARREST_DISTANCE)
		{
			robber.isCaught = true;
			Play.getInstance().gameOver();
			return true;			
		}
		else return false; 

	}

}