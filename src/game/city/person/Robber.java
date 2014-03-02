package game.city.person;
import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


// TODO: remove extends replace with instance SpriteSheet. Make it extends Person
public class Robber extends SpriteSheet {

	// Declare Direction enumerator
	enum Direction { 
		UP, 
		DOWN, 
		RIGHT, 
		LEFT
	}
	
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
	public Image spriteSheetImage;

	// TODO: the following 2 should be 'constants'
	// Animations
	Animation currentAnimation = null; 
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null; 	


	//time to display each sprite
	int duration = 200;
	// sprite drawing location
	public float spriteX = 0;
	// sprite drawing location
	public float spriteY = 0;	

	// Dimensions a single sprite
	int spriteWidth;
	int spriteHeight;

	//====================================================================================================
	//SpriteSheet
	//====================================================================================================

	// Dimensions for the whole sheet containing all the sprites
	float spriteSheetWidth;
	float spriteSheetHeight;

	int spritesPerRow = 6;
	int spritesPerColumn = 2;
	//====================================================================================================

	public Robber(String ref, int spriteWidth, int spriteHeight) throws SlickException {    
		super(ref, spriteWidth, spriteHeight);

		// initially the player is moving to the right
		direction = Direction.RIGHT;

		// set initial position
		this.spriteX = 0;
		this.spriteY = 0;

		// set the rectangle of the player 
		this.rect = new Rectangle(this.spriteX, this.spriteY, spriteWidth, spriteHeight);


		// initially the player is moving to the right
		//Create a new animation based on a selection of
		// sprites from the sprite sheet.
		currentAnimation =  rightWalkAnimation = new Animation(this,
				0,//first column
				0,//first row
				4,//last column
				0,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);
		leftWalkAnimation = new Animation(this,
				0,//first column
				1,//first row
				4,//last column 
				1,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);

		currentAnimation.stop();  
	}
	
	public void fleePoliceman(Vector2f direction)
	{

		float deltaX = 0 ,  deltaY = 0;

		// he is closer horizontally to the policeman
		// move away horizontally
		if (Math.abs(direction.x) < Math.abs(direction.y))
		{
			deltaX = direction.x/2; 
		}
		else
			deltaY = direction.y/2;
		
	
		this.move(deltaX+ this.spriteX, this.spriteY + deltaY);

		
//		this.move(direction.x+ this.spriteX, direction.y + this.spriteY);	
	}

	public void move(float destX, float destY)
	{
		// set the Destination coordinates
		this.destX = destX; 
		this.destY = destY; 
		
		// set the direction of the policeman 
		this.vectorDirection = new Vector2f(destX  - this.spriteX, destY - this.spriteY);
		
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
		this.spriteX++;
		this.rect.setX(this.spriteX);
		this.currentAnimation = Robber.rightWalkAnimation;

	}

	public void moveLeft() {
		this.currentAnimation.start();
		this.spriteX--;
		this.currentAnimation = Robber.leftWalkAnimation;
		this.rect.setX(this.spriteX);
		//		this.setCurrentAnimation(Player.leftWalkAnimation);
	}

	public void moveUp() {
		this.spriteY--;
		this.rect.setY(this.spriteY);
	}
	public void moveDown() {
		this.spriteY++;
		this.rect.setY(this.spriteY);
	}

	public void setCurrentAnimation(Animation animation)
	{
		this.currentAnimation = animation;
	}

	@Override
	public void draw() {
		this.currentAnimation.draw(this.getSpriteX(), this.getSpriteY());
		Policeman policeman; 
		
		if ((policeman = canSeePolice()) !=null)
		{
			// move in opposite direction
			// get vector to policeman 
			Vector2f directionToPoliceman = new Vector2f(policeman.xPos-this.spriteX, policeman.yPos-this.spriteY);

			// get the negative vector the one in the opposite direction
			Vector2f negativeVector = directionToPoliceman.negate();

			// flee the policeman 
			this.fleePoliceman(negativeVector);
		}
		
		// if Robber is moving, change xPos and yPos 
		if (isMoving)
		{
			float speed = (float) (0.04f * velocity);
			this.spriteX += speed * Math.cos(Math.toRadians(this.vectorDirection.getTheta()));
			this.spriteY += speed * Math.sin(Math.toRadians(this.vectorDirection.getTheta()));
			
			// 1.0f margin of error
			if (Math.abs(this.spriteX-this.destX) <2.0f && Math.abs(this.spriteY-this.destY) <2.0f)
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
			float distance = (float)  Math.sqrt(Math.pow(policeman.xPos-this.spriteX, 2.0) + Math.pow(policeman.yPos-this.spriteY, 2.0)); 
			if (distance < visionDistance)
				return policeman;
		}

		return null ;  	
	}

	// ========================================================================================================================================================================
	// GETTERS AND SETTERS for Sprite Position 
	// ========================================================================================================================================================================
	public float getSpriteX() {
		return spriteX;
	}
	public float getSpriteY() {
		return spriteY;
	}
}
