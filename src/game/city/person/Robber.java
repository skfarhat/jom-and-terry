package game.city.person;

import game.Globals;
import game.city.building.Area;
import game.city.building.Building;
import game.states.Savable;

import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

@SuppressWarnings("unchecked")

public class Robber extends Person implements Savable{

	// TODO: see what to do with this
	Random rand = new Random(); 

	private static String playerSpriteSheet = "res/SpriteSheets/robber-spritesheet.png";
	public boolean isCaught = false;

	protected boolean isRobbing = false; 

	protected float score; 
	protected Integer money;  
 
	// ENVIRONMENT AROUND 
	public Building nearByBldg;													

	/**
	 * 
	 * @param isUser used to indicate if the user has chosen to play with the robber or the police.
	 * @throws SlickException
	 */
	public Robber(Area area) throws SlickException {

		// TODO: set name and velocity somewhere else
		super(area, "Robber", Globals.ROBBER_VELOCITY);

		super.initSpriteSheet(playerSpriteSheet, 4, 4);
		// set the Sprite Sheet

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
		int rightWalkRow = 0; 
		int leftWalkRow = 1; 
		
		int downWalkRow = 2;
		int upWalkRow = 3; 


		int duration = 100;
		System.out.println("spritesheet +" + spriteSheet);
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
	
		currentAnimation.start();  
	}

	// ====================================================================================================
	// GETTERS/SETTERS

	public Integer getMoney() {
		return money;
	}

	public float getScore() {
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
	public void addScore(float addedScore){
		this.score+=addedScore; 
	}

	public void setRobbing(boolean isRobbing) {
		this.isRobbing = isRobbing;
	}


	// MOVEMENT
	//=====================================================================================================
	
	public boolean canSeePoliceman(Policeman policeman){
		
		float distance = (float)  Math.sqrt(Math.pow(
				policeman.position.getX()-this.position.getX(), 2.0) 
				+ Math.pow(policeman.position.getY()-this.position.getY(), 2.0)
				);
		return distance < Globals.ROBBER_VISION_DISTANCE;
	}

	public void stop() {
		this.currentAnimation.stop();
	}

	public void setCurrentAnimation(Animation animation)
	{
		this.currentAnimation = animation;
	}

	public void draw(boolean show) {
		if (show){
			this.currentAnimation.draw(this.position.getX(), this.position.getY());
		}
	}

	public boolean rob()
	{
		// if there is no nearby building then the robber cannot rob anything
		if (this.nearByBldg == null)
		{
			System.out.println("Near building is null cannot rob!");
			return false; 
		}

		// Rob the building and pass parameter to self
		this.nearByBldg.rob(this); 

		return true;
	}

	public boolean rob(Building bldg){

		if (bldg == null)
			return false;

		if (!bldg.isInRobbingDistance(this.rect))
			return false;

		this.setRobbing(true);

		// Rob the building and pass parameter to self
		bldg.rob(this);

		return true;

	}

	@Override
	public JSONObject save() {

		HashMap<String, Object> map = new HashMap<>();
		
		map.put(Globals.ROBBER_MONEY, this.money);							// put money
		map.put(Globals.ROBBER_SCORE, this.score);							// put score
 
		map.put(Globals.ROBBER_POSITION_X, this.position.getX());			// put position x
		map.put(Globals.ROBBER_POSITION_Y, this.position.getY());			// put position y
		

		JSONObject object = new JSONObject(map);
		
		return object; 
	}

	public void load(Object loadObj){
		assert (loadObj!=null): "Object to load is null";
		
		HashMap<Object, Object> map = (HashMap<Object, Object> ) loadObj;
		
		this.money = (Integer) map.get(Globals.ROBBER_MONEY);
		this.score = (float) (double)map.get(Globals.ROBBER_SCORE);
		
		this.position.setX((float) (double)map.get(Globals.ROBBER_POSITION_X));
		this.position.setY((float) (double)map.get(Globals.ROBBER_POSITION_Y));
		
	}
}
