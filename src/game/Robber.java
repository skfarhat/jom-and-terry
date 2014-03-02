package game;
import game.city.person.Policeman;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Robber extends SpriteSheet {

	// Declare Direction enumerator
	enum Direction { 
		UP, 
		DOWN, 
		RIGHT, 
		LEFT
	}

	private ArrayList<Policeman> policeForceArray;

	public Rectangle rect; 
	public Direction direction; 
	public Image spriteSheetImage;

	// TODO: the following 2 should be 'constants'
	// Animations
	static Animation rightWalkAnimation = null;
	static Animation leftWalkAnimation = null; 	

	Animation currentAnimation = null; 

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
		if (canSeePolice())
//			this.getGraphics().drawString("Can see Police", 0, 0);
					System.out.println("Can see police");

	}

	public boolean canSeePolice()
	{
		// check that the distance between the robber and all the police force is less than 50.0 
		// If less than 50.0f for some police return true
		for (Policeman police : this.policeForceArray)
		{
			float distance = (float)  Math.sqrt(Math.pow(police.xPos-this.spriteX, 2.0) + Math.pow(police.yPos-this.spriteY, 2.0)); 
			if (distance < 50.0f)
				return true;
		}

		// return false if there is no nearby Police
		return false; 	
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
	//	public void decrementY(){
	//		this.spriteY--; 
	//		this.rect.setY(this.spriteY);
	//	}
	//	public void incrementY(){
	//		this.spriteY++; 
	//		this.rect.setY(this.spriteY);
	//	}
	//	
	//	public void decrementX(){
	//		this.spriteX--; 
	//		this.rect.setX(this.spriteX);
	//		this.rect = new Rectangle(this.spriteX, this.spriteY, this.spriteWidth, this.spriteHeight);
	//	}
	//	public void incrementX(){
	//		this.spriteX++; 
	//		this.rect.setX(this.spriteX);
	//	}

	//	// Setters for xPos and yPos
	//	// Needed because we need to change the position of the rect whenever the positions of the sprite are changed
	//	public void setSpriteX(float spriteX) {
	//		this.spriteX = spriteX;
	//		System.out.println("Set Sprite X");
	//		this.rect.setX(this.spriteX);
	//	}
	//	public void setSpriteY(float spriteY) {
	//		this.spriteY = spriteY;
	//		System.out.println("Set Sprite Y");
	//		this.rect.setY(this.spriteY);
	//	}
	// ========================================================================================================================================================================
	// ========================================================================================================================================================================

}
