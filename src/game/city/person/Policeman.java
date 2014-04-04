package game.city.person;

import game.Globals;
import game.city.building.Building;
import game.city.building.House;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
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
public class Policeman extends Person implements Movable{

	private Play play; 
	private String policemanImgPath 			= "res/police1.png";
	private String policeSpriteSheet 			= "res/Spritesheets/police.png";
	//=================================================================
	private Image image;

	private Integer score = 0; 
	
	private float destX, destY; 
	public Vector2f direction; 
	private float visionDistance = 130.0f;	// Vision Attribute
	//=================================================================
	// Behavior
	public boolean isSelected = false; 
	public boolean isMoving = false;
	private boolean isPatrolling = false;
	private boolean isFollowingRobber = false;
	private boolean isCheckingSuspectRegion= false;
	//=================================================================
	// 
	private Timer patrolTimer; 
	public Robber robber; 

	private Circle suspectRegion; 


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
	public Policeman(Play play,  Robber robber,  float positionX, float positionY, String name,
			double velocity) throws SlickException {

		// call superclass constructor (Person)
		super(name, velocity);

		this.play = play;

		// set the Sprite Sheet
		this.initSpriteSheet();

		// Set the position of the policeman
		this.xPos = positionX;
		this.yPos = positionY;
		
		// set the rectangle of the player 
		this.rect = new Rectangle(this.xPos, this.yPos, spriteWidth, spriteHeight);

		// Set the image of the policeman
		this.image = new Image(policemanImgPath);

		this.robber = robber;

		//this.startPatrol();

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

	public Integer getScore() {
		return score;
	}

	public void setSuspectRegion(Circle suspectRegionCircle) {
		this.suspectRegion = suspectRegionCircle;
	}

	public void move(float destX, float destY)
	{
		this.destX = destX; 
		this.destY = destY;
		this.direction = new Vector2f(destX  - this.xPos, destY - this.yPos);

		this.isMoving = true;
	}

	private Point randomPointInCircle(Circle circle){
		Random rand = new Random(); 

		float randAngle =  (float) (rand.nextFloat() * (Math.PI * 2.0f)); 		// random angle 
		float randRadius = (float)  (rand.nextFloat()%circle.radius); 			// random radius

		float x = circle.getCenterX() + (float) (randRadius * Math.cos(randAngle));
		float y = circle.getCenterY() + (float) (randRadius* Math.sin(randAngle));

		return new Point(x,y);
	}

	public void draw() {

		// if Policeman is moving, change xPos and yPos
		if (isMoving) {
			float speed = (float) (Globals.VELOCITY_MULTIPLIER * velocity);

			float deltaX = this.xPos - this.destX; 
			float deltaY = this.yPos - this.destY;


			// we want the policeman to go all the way horizontally then after arrivign to the correct xpos
			// go all the way vertically, so we declare a variale movingHorizontally and we set it to true whenever the policeman is going horizontally
			// if true, the second if statement is not satisfied and the yPos has to wait for the xPos to finish
			boolean movingHorizontally = false;
			if (Math.abs(deltaX) > 2.0f)
			{
				this.xPos += (deltaX<0)?speed: (-1)*speed; 	
				this.rect.setX(this.xPos);
				movingHorizontally = true;
			}

			// check on movingHorizontally done here
			if (Math.abs(deltaY) > 2.0f && !movingHorizontally)
			{	
				this.yPos += (deltaY<0)?speed: (-1)*speed;
				this.rect.setY(this.yPos);
			}

			// 2.0f margin of error
			if (Math.abs(this.xPos - this.destX) < 2.0f
					&& Math.abs(this.yPos - this.destY) < 2.0f) {

				this.isMoving = false;	
				if (isCheckingSuspectRegion)
				{
					this.isCheckingSuspectRegion = false;
					this.suspectRegion = null; 
				}
			}
		}

		// if the Policeman is neither patrolling nor following the robber then he should patrol
//		if (!this.isPatrolling && !this.isFollowingRobber)
//			startPatrol();

		// see if the Robber is visible
		lookForRobber();

		// draw the image at the positon of the policeman
		this.image.draw(this.xPos, this.yPos);
		
		if (isSelected){
			selectedImage.draw(this.xPos, this.yPos-selectedImage.getHeight());
		}

	}

	public void startPatrol()
	{ 
		isPatrolling = true;

		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer index = (new Random()).nextInt(House.getHouses().size());
				Building bldg = Building.buildings.get(index);

				move(bldg.xPos, bldg.yPos);
			}
		});
		patrolTimer.start();

	}

	private void stopPatrol(){
		patrolTimer.stop();
		this.isPatrolling = false;
	}

	public void checkoutRegion(Circle region){
		// to check out a region

		//first stop patroling
		if (isPatrolling)
			stopPatrol();

		setSuspectRegion(region);

		// get a random point inside the region
		Point randPoint = randomPointInCircle(suspectRegion);

		move(randPoint.getX(), randPoint.getY());

		// set flag for isCheckingRegion
		this.isCheckingSuspectRegion = true;
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
		this.isFollowingRobber = true;
		this.move(robber.xPos, robber.yPos);
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

	@Override
	public void processInput(Input input) {
		// ARROWS: UP DOWN LEFT RIGHT
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			moveRight();
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			moveLeft();
		} else if (input.isKeyDown(Input.KEY_UP)) {
			moveUp();
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			moveDown();
		} 
		else {
			stop();
		}		
	}
	

	public void moveRight() {
		this.currentAnimation.start();
		this.xPos+=Globals.VELOCITY_MULTIPLIER*velocity;
		this.rect.setX(this.xPos);

		if (collides()){
			normalForceLeft();
		}
	}

	public void moveLeft() {
		this.currentAnimation.start();
		this.xPos-=velocity * Globals.VELOCITY_MULTIPLIER;
		this.rect.setX(this.xPos);
		if (collides()){
			normalForceRight();
		}
	}

	public void moveUp() {
		this.yPos-=velocity *Globals.VELOCITY_MULTIPLIER;
		this.rect.setY(this.yPos);
		if (collides()){
			normalForceDown();
		}
	}

	public void moveDown() {
		this.yPos+=Globals.VELOCITY_MULTIPLIER*velocity;
		this.rect.setY(this.yPos);
		if (collides()){
			normalForceUp();
		}
	}

	public void normalForceRight() {
		this.xPos+=Globals.VELOCITY_MULTIPLIER*velocity;
		this.rect.setX(this.xPos);
	}

	public void normalForceLeft() {
		this.xPos-=velocity * Globals.VELOCITY_MULTIPLIER;
		this.rect.setX(this.xPos);
	}

	public void normalForceUp() {
		this.yPos-=velocity *Globals.VELOCITY_MULTIPLIER;
		this.rect.setY(this.yPos);
	}

	public void normalForceDown() {
		this.yPos+=Globals.VELOCITY_MULTIPLIER*velocity;
		this.rect.setY(this.yPos);
	}

	@Override
	public boolean collides() {
		return false;
	}

	@Override
	public void stop() {
		
	}

}