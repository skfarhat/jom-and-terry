package game.city.person;

import game.Globals;
import game.city.building.Area;
import game.city.building.Building;
import game.states.Play;
import game.states.Savable;

import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.Mover;

@SuppressWarnings("unchecked")
/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public abstract class Policeman extends Person implements Savable, Mover {

	static Random rand = new Random(System.currentTimeMillis());
	private static String policeSpriteSheet = "res/Spritesheets/Police.png";
	// =================================================================
	private Image image;
	protected Point destPoint;

	protected Building nearByBldg;
	// =================================================================
	// Behavior
	protected boolean userIsPolice;
	protected boolean robberIsVisible = false;
	// =================================================================
	public Robber robber;
	protected Circle suspectRegion;

	// =================================================================

	/**
	 * CONSTRUCTOR Creates a policeman.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 * @throws SlickException
	 */
	public Policeman(Area area, Robber robber, Point position, String name,
			double velocity) throws SlickException {

		// call superclass constructor (Person)
		super(area, name, velocity);

		super.initSpriteSheet(policeSpriteSheet, 4, 2);
		// set the Sprite Sheet

		// Set the position of the policeman
		this.position = position;

		// set the rectangle of the player
		this.rect = new Rectangle(position.getX(), position.getY(),
				spriteWidth, spriteHeight);

		// Set the image of the policeman
		this.robber = robber;

		int lastColumn = 3;
		int rightWalkRow = 0;
		int leftWalkRow = 1;
		int downWalkRow = 0;
		int upWalkRow = 1;

		int duration = 100;
		currentAnimation = rightWalkAnimation = new Animation(this.spriteSheet,
				0,// first column
				rightWalkRow,// first row
				lastColumn,// last column
				rightWalkRow,// last row
				true,// horizontal
				duration,// display time
				true// autoupdate
		);
		leftWalkAnimation = new Animation(this.spriteSheet, 0,// first column
				leftWalkRow,// first row
				lastColumn,// last column
				leftWalkRow,// last row
				true,// horizontal
				duration,// display time
				true// autoupdate
		);
		upWalkAnimation = new Animation(this.spriteSheet, 0,// first column
				upWalkRow,// first row
				lastColumn,// last column
				upWalkRow,// last row
				true,// horizontal
				duration,// display time
				true// autoupdate
		);

		downWalkAnimation = new Animation(this.spriteSheet, 0,// first column
				downWalkRow,// first row
				lastColumn,// last column
				downWalkRow,// last row
				true,// horizontal
				duration,// display time
				true// autoupdate
		);
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

	public void draw() {
		// draw the image at the positon of the policeman
		this.currentAnimation.draw(this.position.getX(), this.position.getY());

	}

	public void stop() {
		this.currentAnimation.stop();
	}

	/**
	 * Attempt to arrest a robber.
	 * 
	 * @param robber
	 * @return whether the arrest was successful.
	 */
	public boolean arrestRobber(Robber robber) {

		float distance = (float) Math.sqrt(Math.pow(this.position.getX()
				- this.robber.position.getX(), 2.0)
				+ Math.pow(this.position.getY() - this.robber.position.getY(),
						2.0));

		// arrest only if he is less than some distance away
		if (distance < Globals.ARREST_DISTANCE) {
			// if userIsPolice = true then arrest implies youWin
			// otherwise it implies you lose
			Play.getInstance().gameOver(userIsPolice);
			return true;
		} else
			return false;

	}

	/**
	 * Returns a random point inside the circle (for gathering)
	 * 
	 * @param circle
	 * @return
	 */
	public static Point randomPointInCircle(Circle circle) {

		float randAngle = (float) (rand.nextFloat() * (Math.PI * 2.0f)); // random
																			// angle
		float randRadius = (float) (rand.nextFloat() * circle.radius); // random
																		// radius

		float x = circle.getCenterX()
				+ (float) (randRadius * Math.cos(randAngle));
		float y = circle.getCenterY()
				+ (float) (randRadius * Math.sin(randAngle));

		return new Point(x, y);
	}

	@Override
	public JSONObject save() {

		HashMap<String, Object> map = new HashMap<>();

		map.put(Globals.ID, this.ID);
		// map.put(Globals.POLICEMAN_SCORE, this.score); // put score
		map.put(Globals.POLICEMAN_POSITION_X, this.position.getX()); // put
																		// position
																		// x
		map.put(Globals.POLICEMAN_POSITION_Y, this.position.getY()); // put
																		// position
																		// y

		JSONObject object = new JSONObject(map);

		return object;
	}

	@Override
	public void load(Object loadObj) {
		assert (loadObj != null) : "Object to load is null";

		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;

		final float x = (float) (double) map.get(Globals.POLICEMAN_POSITION_X);
		final float y = (float) (double) map.get(Globals.POLICEMAN_POSITION_Y);

		position.setX(x);
		position.setY(y);

		rect.setX(x);
		rect.setY(y);

		// score = (float) (double) map.get(Globals.POLICEMAN_SCORE);
	}

}