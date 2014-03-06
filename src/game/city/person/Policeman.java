package game.city.person;

import game.Play;
import game.city.building.House;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public class Policeman extends Person {

	private Play play; 
	private String policemanImgPath = "res/police1.png";

	public float xPos, yPos;

	private float destX, destY; 
	private Image image;

	public boolean isMoving = false;
	private boolean isPatrolling = false;
	private boolean isFollowingRobber = false;
	public Vector2f direction; 

	private Timer patrolTimer; 
	public Robber robber; 

	// TODO: the following 2 should be 'constants'
	// Animations
	Animation currentAnimation = null; 
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null;

	//time to display each sprite
	int duration = 200;


	public Rectangle rect; 

	//====================================================================================================
	//SpriteSheet
	//====================================================================================================
	SpriteSheet spriteSheet; 

	// Path to the Sprite Sheet
	private String policeSpriteSheet = "res/Spritesheets/police.png";

	// Dimensions a single sprite
	int spriteWidth;
	int spriteHeight;

	// Dimensions for the whole sheet containing all the sprites
	float spriteSheetWidth;
	float spriteSheetHeight;

	int spritesPerRow = 6;
	int spritesPerColumn = 2;

	//====================================================================================================

	// Vision Attribute
	private float visionDistance = 130.0f;

	/**
	 * Creates a policeman.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 * @throws SlickException
	 */
	public Policeman(Play play,  Robber robber,  float positionX, float positionY, String name,
			double velocity) throws SlickException {

		// call superclass constructor (Person)
		super(name, velocity);

		this.play = play;

		// set the Sprite Sheet
		this.initSpriteSheet();

		// set the rectangle of the player 
		this.rect = new Rectangle(this.xPos, this.yPos, spriteWidth, spriteHeight);

		// Set the position of the policeman
		this.xPos = positionX;
		this.yPos = positionY;

		// Set the image of the policeman
		this.image = new Image(policemanImgPath);

		this.robber = robber;

		this.patrol();

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

	/**
	 * Warns the police if a robber is near.
	 * 
	 * @param robber
	 * @return
	 */
	public boolean warnPolice(Robber robber) {
		// return (calculateDistance(robber) < 40.0);
		return false;
	}

	/**
	 * Attempt to arrest a robber.
	 * 
	 * @param robber
	 * @return whether the arrest was successful.
	 */

	public boolean arrestRobber(Robber robber) {
		robber.isCaught = true; 
		this.play.gameOver();
		return true;
	}

	public void move(float destX, float destY)
	{
		this.destX = destX; 
		this.destY = destY;
		this.direction = new Vector2f(destX  - this.xPos, destY - this.yPos);

		this.isMoving = true;
	}

	public void draw() {
		// if Policeman is moving, change xPos and yPos
		if (isMoving) {
			float speed = (float) (0.04f * velocity);

			float deltaX = this.xPos - this.destX; 
			float deltaY = this.yPos - this.destY;


			// we want the policeman to go all the way horizontally then after arrivign to the correct xpos
			// go all the way vertically, so we declare a variale movingHorizontally and we set it to true whenever the policeman is going horizontally
			// if true, the second if statement is not satisfied and the yPos has to wait for the xPos to finish
			boolean movingHorizontally = false;
			if (Math.abs(deltaX) > 2.0f)
			{
				this.xPos += (deltaX<0)?speed: (-1)*speed; 	
				movingHorizontally = true;
			}

			// check on movingHorizontally done here
			if (Math.abs(deltaY) > 2.0f && !movingHorizontally)
			{	
				this.yPos += (deltaY<0)?speed: (-1)*speed; 
			}

			// 2.0f margin of error
			if (Math.abs(this.xPos - this.destX) < 2.0f
					&& Math.abs(this.yPos - this.destY) < 2.0f) {
				this.isMoving = false;	
			}
		}
		
		// if the Policeman is neither patrolling nor following the robber then he should patrol
		if (!this.isPatrolling && !this.isFollowingRobber)
			patrol();

		// see if the Robber is visible
		lookForRobber();

		// draw the image at the positon of the policeman
		this.image.draw(this.xPos, this.yPos);
	}

	public void patrol()
	{ 
		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Random rand = new Random(); 

				Integer index = rand.nextInt(play.getHouses().size());
				House house = play.getHouses().get(index);
				move(house.xPos, house.yPos);
			}
		});
		patrolTimer.start();
		
		this.isPatrolling = true;
	}

	public boolean lookForRobber()
	{
		float distance = (float)  Math.sqrt(Math.pow(this.xPos-this.robber.xPos, 2.0) + Math.pow(this.yPos-this.robber.yPos, 2.0));
		if (distance < 2.0f && !robber.isCaught)
		{
			// the Robber has been caught
			// send a message to signal game over
			arrestRobber(robber);
		}
		if (distance < visionDistance)
		{
			followRobber();
			return true;
		}
		else
		{
			this.isFollowingRobber  = false; 
		}
		return false;
	}

	private void followRobber()
	{
		patrolTimer.stop();
		this.isPatrolling = false;
		this.isFollowingRobber = true;
		this.move(robber.xPos, robber.yPos);
	}

}