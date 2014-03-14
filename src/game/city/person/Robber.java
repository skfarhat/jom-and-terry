package game.city.person;
import game.city.building.Building;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;


public class Robber extends Person{
	
	private String playerSpriteSheet = "res/SpriteSheets/robber-2.png";
	public boolean isCaught = false; 
	private boolean isUser; 

	public Rectangle rect;
	public float xPos = 0;// sprite drawing location
	public float yPos = 0;	// sprite drawing location

	public Integer money; 

	// Vision Attribute
	private float visionDistance = 100.0f; 

	// Movement attributes
	private float destX, destY;
	private float velocity = 120.0f; 
	public boolean isMoving = false; 
	public Vector2f vectorDirection;


	// =================================================================================================== 
	// ENVIRONMENT AROUND 
	private ArrayList<Policeman> policeForceArray;		// Police Related
	public Building nearByBldg;							// Building
	
	// ===================================================================================================
	// TODO: the following 2 should be 'constants'
	// Animations
	Animation currentAnimation = null; 
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null;
	static Animation downWalkAnimation = null;
	static Animation upWalkAnimation = null;


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

		// set initial position
		this.xPos = 0;
		this.yPos = 0;

		// The robber is initially broke
		this.money = 0; 

		// set the rectangle of the player 
		this.rect = new Rectangle(this.xPos, this.yPos, spriteWidth, spriteHeight);


		// initially the player is moving to the right
		//Create a new animation based on a selection of
		// sprites from the sprite sheet.
		int lastColumn = 3;
		int rightWalkRow = 2; 
		int leftWalkRow = 1; 
		int upWalkRow = 3; 
		int downWalkRow = 0;

		
		int duration = 200;
		currentAnimation =  rightWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				rightWalkRow,//first row
				lastColumn,//last column
				rightWalkRow,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);
		leftWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				leftWalkRow,//first row
				lastColumn,//last column 
				leftWalkRow,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);
		upWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				upWalkRow,//first row
				lastColumn,//last column 
				upWalkRow,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);

		downWalkAnimation = new Animation(this.spriteSheet,
				0,//first column
				downWalkRow,//first row
				lastColumn,//last column 
				downWalkRow,//last row
				true,//horizontal
				duration,//display time
				true//autoupdate
				);

		currentAnimation.stop();  
	}

	private void initSpriteSheet() throws SlickException {

		spritesPerRow = 4; 
		spritesPerColumn = 4;
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


	// MOVEMENT
	//=====================================================================================================
	public void stop() {
		this.currentAnimation.stop();
	}

	public void moveRight() {
		this.currentAnimation.start();
		this.xPos+=0.02*velocity;
		this.rect.setX(this.xPos);
		this.currentAnimation = Robber.rightWalkAnimation;

	}

	public void moveLeft() {
		this.currentAnimation.start();
		this.xPos-=velocity * 0.02f;
		this.rect.setX(this.xPos);
		this.currentAnimation = Robber.leftWalkAnimation;
	}

	public void moveUp() {
		this.yPos-=velocity *0.02f;
		this.rect.setY(this.yPos);
		this.currentAnimation = Robber.upWalkAnimation;
	}

	public void moveDown() {
		this.yPos+=0.02f*velocity;
		this.rect.setY(this.yPos);
		this.currentAnimation = Robber.downWalkAnimation;
	}


	public void normalForceRight() {
		this.xPos+=0.02*velocity;
		this.rect.setX(this.xPos);
	}

	public void normalForceLeft() {
		this.xPos-=velocity * 0.02f;
		this.rect.setX(this.xPos);
	}

	public void normalForceUp() {
		this.yPos-=velocity *0.02f;
		this.rect.setY(this.yPos);
	}

	public void normalForceDown() {
		this.yPos+=0.02f*velocity;
		this.rect.setY(this.yPos);
	}


	public void setCurrentAnimation(Animation animation)
	{
		this.currentAnimation = animation;
	}

	public void draw() {
		this.currentAnimation.draw(this.xPos, this.yPos);

		Policeman policeman; 
		if (!isUser){
			if ((policeman = canSeePolice()) !=null)
			{
				// move in opposite direction
				// get vector to policeman 
				Vector2f directionToPoliceman = new Vector2f(policeman.xPos-this.xPos, policeman.yPos-this.yPos);

				// get the negative vector the one in the opposite direction
				Vector2f negativeVector = directionToPoliceman.negate();


				// flee the policeman only if the user has chosen to play as the police
				this.fleePoliceman(negativeVector);
			}
		}

		// if Robber is moving, change xPos and yPos 
		if (isMoving)
		{
			float speed = (float) (0.04f * velocity);
			this.xPos += speed ;///* Math.cos(Math.toRadians(this.vectorDirection.getTheta()));
			this.yPos += speed ;//* Math.sin(Math.toRadians(this.vectorDirection.getTheta()));

			// 1.0f margin of error
			if (Math.abs(this.xPos-this.destX) <2.0f && Math.abs(this.yPos-this.destY) <2.0f)
			{
				this.isMoving = false; 
			}
		}

	}

	public boolean rob()
	{
		// if there is no nearby buildg then the robber cannot rob anything
		if (this.nearByBldg == null)
			return false; 

		Integer money = this.nearByBldg.money; 

		// add the sum of money to the player's cash
		this.money += money;

		// completely rob the building
		this.nearByBldg.money = 0; 
		return true;
	}

	// VISION - ONLY WHEN USER IS POLICE
	// ====================================================================================================
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
