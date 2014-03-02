package game.city.person;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public class Policeman extends Person {
	private String policemanImgPath = "res/police1.png";

	public float xPos, yPos;

	private float destX, destY; 
	private Image image;

	public boolean isMoving = false; 
	public Vector2f direction; 

	public Robber robber; 


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
	public Policeman(Robber robber,  float positionX, float positionY, String name,
			double velocity) throws SlickException {

		// call superclass constructor (Person)
		super(positionX, positionY, name, velocity);


		// Set the position of the policeman
		this.xPos = positionX;
		this.yPos = positionY;

		// Set the image of the policeman
		this.image = new Image(policemanImgPath);

		this.robber = robber;

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
	// TODO FIX
	public boolean arrestRobber(Robber robber) {
		// TODO implement
		return true;
	}

	public void move(float destX, float destY)
	{
		// set the Destination coordinates
		this.destX = destX; 
		this.destY = destY; 

		// set the direction of the policeman 
		this.direction = new Vector2f(destX  - this.xPos, destY - this.yPos);

		// set the boolean is moving to true
		this.isMoving = true; 

	}

	public void draw() {
		// if Policeman is moving, change xPos and yPos
		if (isMoving) {
			float speed = (float) (0.04f * velocity);
			
			this.xPos += speed
					* Math.cos(Math.toRadians(this.direction.getTheta()));
			this.yPos += speed
					* Math.sin(Math.toRadians(this.direction.getTheta()));


			// 1.0f margin of error
			if (Math.abs(this.xPos - this.destX) < 2.0f
					&& Math.abs(this.yPos - this.destY) < 2.0f) {
				this.isMoving = false;
			}
		}
		
		
		// see if the Robber is visible
		lookForRobber();
		
		// draw the image at the positon of the policeman
		this.image.draw(this.xPos, this.yPos);
	}


	public boolean lookForRobber()
	{
		float distance = (float)  Math.sqrt(Math.pow(this.xPos-this.robber.spriteX, 2.0) + Math.pow(this.yPos-this.robber.spriteY, 2.0)); 
		if (distance < visionDistance)
		{
			followRobber();
			return true;
		}
		return false;
	}
	private void followRobber()
	{
		this.move(robber.spriteX, robber.spriteY);
	}

}