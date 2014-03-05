package game.city.person;
import java.util.ArrayList;


import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


public class Robber extends Person{

	// Declare Direction enumerator
	enum Direction { 
		UP, 
		DOWN, 
		RIGHT, 
		LEFT
	}

	private boolean isUser; 

	// Vision Attribute
	private float visionDistance = 100.0f; 

	// Movement attributes
	private float destX, destY;
	private float velocity = 50.0f; 
	public boolean isMoving = false; 
	public Vector2f vectorDirection;

	private ArrayList<Policeman> policeForceArray;

	public Rectangle rect; 
	public Direction direction; 

	// TODO: the following 2 should be 'constants'
	// Animations
	Animation currentAnimation = null; 
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null; 	


	//time to display each sprite
	int duration = 200;

	// sprite drawing location
	public float xPos = 0;
	// sprite drawing location
	public float yPos = 0;	



	//====================================================================================================
	//SpriteSheet
	//====================================================================================================
	SpriteSheet spriteSheet; 

	// Path to the Sprite Sheet
	private String playerSpriteSheet = "res/player1.png";

	// Dimensions a single sprite
	int spriteWidth;
	int spriteHeight;

	// Dimensions for the whole sheet containing all the sprites
	float spriteSheetWidth;
	float spriteSheetHeight;

	int spritesPerRow = 6;
	int spritesPerColumn = 2;

	//====================================================================================================

	/**
	 * 
	 * @param isUser used to indicate if the user has chosen to play with the robber or the police.
	 * @throws SlickException
	 */
	public Robber(boolean isUser) throws SlickException {

		// TODO: set anem and velocity somewhere else
		super("Robber", 50.0f);


		this.isUser = isUser; 

		// set the Sprite Sheet
		this.initSpriteSheet();

		// initially the player is moving to the right
		direction = Direction.RIGHT;

		// set initial position
		this.xPos = 0;
		this.yPos = 0;

		// set the rectangle of the player 
		this.rect = new Rectangle(this.xPos, this.yPos, spriteWidth, spriteHeight);


		// initially the player is moving to the right
		//Create a new animation based on a selection of
		// sprites from the sprite sheet.

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
		//Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(playerSpriteSheet);
		int spriteSheetWidth 	= spriteSheetImage.getWidth();
		int spriteSheetHeight 	= spriteSheetImage.getHeight();

		//Compute the width and height of the individual 
		// sprite images.
		spriteWidth = (int)(spriteSheetWidth/spritesPerRow);
		spriteHeight =(int)(spriteSheetHeight/spritesPerColumn);

		this.spriteSheet = new SpriteSheet(spriteSheetImage, spriteWidth, spriteHeight);

	}

	public void fleePoliceman(Vector2f direction)
	{
		float deltaX = 0 ,  deltaY = 0;

		// he is closer horizontally to the policeman
		// move away horizontally
		if (Math.abs(direction.x) < Math.abs(direction.y))
			deltaX = direction.x/2; 
		else
			deltaY = direction.y/2;

		this.move(deltaX+ this.xPos, this.yPos + deltaY);

	}

	public void move(float destX, float destY)
	{
		// set the Destination coordinates
		this.destX = destX; 
		this.destY = destY; 

		// set the direction of the policeman 
		this.vectorDirection = new Vector2f(destX  - this.xPos, destY - this.yPos);

		// set the boolean is moving to true
		this.isMoving = true; 

	}

	public void setPoliceForceArray(ArrayList<Policeman> policeForceArray) {
		this.policeForceArray = policeForceArray;
	}

	public void stop() {
		this.currentAnimation.stop();
	}

	public void moveRight() {
		this.currentAnimation.start();
		this.xPos++;
		this.rect.setX(this.xPos);
		this.currentAnimation = Robber.rightWalkAnimation;

	}

	public void moveLeft() {
		this.currentAnimation.start();
		this.xPos--;
		this.rect.setX(this.xPos);
		this.currentAnimation = Robber.leftWalkAnimation;
	}

	public void moveUp() {
		this.yPos--;
		this.rect.setY(this.yPos);
	}
	public void moveDown() {
		this.yPos++;
		this.rect.setY(this.yPos);
	}

	public void setCurrentAnimation(Animation animation)
	{
		this.currentAnimation = animation;
	}

	public void draw() {
		this.currentAnimation.draw(this.xPos, this.yPos);

		Policeman policeman; 

		if ((policeman = canSeePolice()) !=null)
		{
			// move in opposite direction
			// get vector to policeman 
			Vector2f directionToPoliceman = new Vector2f(policeman.xPos-this.xPos, policeman.yPos-this.yPos);

			// get the negative vector the one in the opposite direction
			Vector2f negativeVector = directionToPoliceman.negate();

			if (!isUser)
				// flee the policeman only if the user has chosen to play as the police
				this.fleePoliceman(negativeVector);
		}

		// if Robber is moving, change xPos and yPos 
		if (isMoving)
		{
			float speed = (float) (0.04f * velocity);
			this.xPos += speed * Math.cos(Math.toRadians(this.vectorDirection.getTheta()));
			this.yPos += speed * Math.sin(Math.toRadians(this.vectorDirection.getTheta()));

			// 1.0f margin of error
			if (Math.abs(this.xPos-this.destX) <2.0f && Math.abs(this.yPos-this.destY) <2.0f)
			{
				this.isMoving = false; 
			}
		}

	}

	public Policeman canSeePolice()
	{
		// check that the distance between the robber and all the police force is less than 50.0 
		// If less than 50.0f for some police return true
		for (Policeman policeman : this.policeForceArray)
		{
			float distance = (float)  Math.sqrt(Math.pow(policeman.xPos-this.xPos, 2.0) + Math.pow(policeman.yPos-this.yPos, 2.0)); 
			if (distance < visionDistance)
				return policeman;
		}

		return null ;  	
	}
}
