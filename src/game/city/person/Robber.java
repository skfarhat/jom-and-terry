package game.city.person;
import game.Globals;
import game.city.building.Building;

import java.util.Random;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Robber extends Person{

	Random rand = new Random(); 

	private String playerSpriteSheet = "res/SpriteSheets/robber-2.png";
	public boolean isCaught = false;
		
	protected boolean isRobbing = false; 

	protected Integer score; 
	protected Integer money; 

	// Movement attributes
	public boolean isMoving = false; 
//	public Vector2f vectorDirection;

	// =================================================================================================== 
	// ENVIRONMENT AROUND 
	public Building nearByBldg;															// Building

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
	public Robber() throws SlickException {

		// TODO: set name and velocity somewhere else
		super("Robber", Globals.ROBBER_VELOCITY);

		// set the Sprite Sheet
		this.initSpriteSheet();

		// set initial position
		this.position = new Point(0,0);
		
		// The robber is initially broke
		this.money = 0; 
		this.score = 0;

		// set the rectangle of the player 
		this.rect = new Rectangle(this.position.getX(), this.position.getY(), spriteWidth, spriteHeight);

		
		// initially the player is moving to the right
		// Create a new animation based on a selection of
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

	
	// ====================================================================================================
	// GETTERS/SETTERS
	
	public Integer getMoney() {
		return money;
	}

	public Integer getScore() {
		return score;
	}

	/**
	 * Money is set to private for encapsulation
	 * Other classes can only add to the amount the robber has
	 * @param addedAmount
	 */
	public void addMoney(Integer addedAmount){
		// 
		this.money+=addedAmount;
	}

	/**
	 * Increase the score by some amount
	 * @param addedScore amount to add to the score of the robber
	 */
	public void addScore(Integer addedScore){
		this.score+=addedScore; 
	}

	public void setRobbing(boolean isRobbing) {
		this.isRobbing = isRobbing;
	}
	
	
	// MOVEMENT
	//=====================================================================================================
	public void stop() {
		this.currentAnimation.stop();
	}

	public void setCurrentAnimation(Animation animation)
	{
		this.currentAnimation = animation;
	}

	public void draw() {
		this.currentAnimation.draw(this.position.getX(), this.position.getY());		
	}
	
	public boolean rob()
	{
		// if there is no nearby building then the robber cannot rob anything
		if (this.nearByBldg == null)
		{
			System.out.println("Near building is null cannot rob!");
			return false; 
		}

		// TODO: Remove
		// Might be redundant 
		this.setRobbing(true);

		// Rob the building and pass parameter to self
		this.nearByBldg.rob(this); 

		return true;
	}

	public boolean rob(Building bldg){
		
		if (bldg == null)
			return false;
		
		if (bldg.isInRobbingDistance(this.position))
			return false;

		this.setRobbing(true);

		// Rob the building and pass parameter to self
		bldg.rob(this);

		return true;

	}
	
}
