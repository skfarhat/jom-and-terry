package game.city.person;

import java.util.HashMap;

import game.Globals;
import game.states.Play;
import game.states.Savable;

import org.json.simple.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

@SuppressWarnings("unchecked")

/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public class Policeman extends Person implements Savable{

	private static String policemanImgPath 				= "res/police1.png";
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

	@Override
	public JSONObject save() {

		HashMap<String, Object> map = new HashMap<>();

		map.put(Globals.ID, this.ID);
		map.put(Globals.POLICEMAN_SCORE, this.score);							// put score
		map.put(Globals.POLICEMAN_POSITION_X, this.position.getX());			// put position x
		map.put(Globals.POLICEMAN_POSITION_Y, this.position.getY());			// put position y

		JSONObject object = new JSONObject(map);

		return object;
	}


	@Override
	public void load(Object loadObj) {
		assert (loadObj!=null): "Object to load is null";

		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;

		this.position.setX((float) (double) map.get(Globals.POLICEMAN_POSITION_X));
		this.position.setY((float) (double) map.get(Globals.POLICEMAN_POSITION_Y));
		this.score = (float) (double) map.get(Globals.POLICEMAN_SCORE);
	}

}