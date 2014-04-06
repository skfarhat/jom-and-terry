package game.city.person;

import game.Globals;
import game.city.building.Bank;
import game.city.building.PoliceOffice;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 * A security guard.
 * 
 * @author sami
 * 
 */
public class SecurityGuard extends Person {

	private String sgImgPath = "res/bouncer.png";

	private Point destinationPoint;

	private Image image;

	private boolean isMoving = false;
	private boolean isFollowingRobber = false;
	private Play play; 

	public Rectangle rect; // frame of the security guard
	public Vector2f direction; // direction in which the Security is moving

	private Robber robber; 
	// Vision Attribute
	private float visionDistance = 130.0f;
	private float chaseDistance = 70.0f;

	// =========================================================================================================
	// PATROL
	private Timer patrolTimer;
	private boolean isPatrolling = false;

	// edges of the bank to be guarded
	private Point topLeftEdge, topRightEdge, bottomRightEdge, bottomLeftEdge;

	// used to know at which edge the SG is currently
	// we could use the position and compare to the edge, but positions are not
	// always exact
	// so this field is used to avoid interval comparing
	private Point currentEdge;

	/**
	 * The bank the Security Guard is responsible for guarding
	 */
	Bank guardedBank = null;

	// ====================================================================================================
	// SpriteSheet
	// ====================================================================================================
	SpriteSheet spriteSheet;

	// Dimensions a single sprite
	int spriteWidth;
	int spriteHeight;

	// Dimensions for the whole sheet containing all the sprites
	float spriteSheetWidth;
	float spriteSheetHeight;

	int spritesPerRow = 4;
	int spritesPerColumn = 4;

	/**
	 * Creates a security guard.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public SecurityGuard(Point position, String name,
			double velocity, Bank guardedBank) throws SlickException {
		super(name, velocity);


		this.guardedBank = guardedBank;

		// Set the image of the policeman
		this.image = new Image(sgImgPath);

		// set the rectangle of the player
		this.rect = new Rectangle(position.getX(), position.getY(), spriteWidth,
				spriteHeight);

		// Set the position of the SG
//		this.position = position; 

		// Set the image of the SG
		this.image = new Image(sgImgPath);

		// FIXME: Careful this might work now, but if the position given to the
		// SG by the Bank is changed (right edge aw shi)
		// because the SG patrols around the bank, so would need to change the order 
//		this.currentEdge = position;

		this.currentEdge = this.position = topLeftEdge = new Point(guardedBank.rect.getMinX(),
				guardedBank.rect.getMinY());
		topRightEdge = new Point(guardedBank.rect.getMaxX(),
				guardedBank.rect.getMinY());
		bottomRightEdge = new Point(guardedBank.rect.getMaxX(),
				guardedBank.rect.getMaxY());
		bottomLeftEdge = new Point(guardedBank.rect.getMinX(),
				guardedBank.rect.getMaxY());

		this.destinationPoint = position;

		this.startRoundPatrol();
	}

	/*
	private final void initSpriteSheet() throws SlickException {
		// Get, save, and display the width and the height
		// of the sprite sheet.
		Image spriteSheetImage = new Image(sgSpriteSheet);
		int spriteSheetWidth = spriteSheetImage.getWidth();
		int spriteSheetHeight = spriteSheetImage.getHeight();

		// Compute the width and height of the individual
		// sprite images.
		spriteWidth = (int) (spriteSheetWidth / spritesPerRow);
		spriteHeight = (int) (spriteSheetHeight / spritesPerColumn);

		this.spriteSheet = new SpriteSheet(spriteSheetImage, spriteWidth,
				spriteHeight);
	}
	 */

	public void setRobber(Robber robber) {
		this.robber = robber; 
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	public void setPoliceOffice(PoliceOffice policeOffice) {
	}

	public void move(Point destPnt) {
		this.direction = new Vector2f(destPnt.getX() - this.position.getX(),
				destPnt.getY() - this.position.getY());

		this.destinationPoint = destPnt;
		this.isMoving = true;
	}

	public void draw() {
		// if SG is moving, change xPos and yPos
		if (isMoving) {

			float speed = (float) (Globals.VELOCITY_MULTIPLIER * velocity);

			float deltaX = this.position.getX() - this.destinationPoint.getX();
			float deltaY = this.position.getY() - this.destinationPoint.getY();

			if (Math.abs(deltaX) > 2.0f) {
				float x = (deltaX < 0) ? this.position.getX() + speed : this.position.getX() + (-1) * speed;
				this.position.setX(x);
			}
			if (Math.abs(deltaY) > 2.0f) {
				float y = (deltaY < 0) ? this.position.getY() + speed : this.position.getY() + (-1) * speed;
				this.position.setY(y);
			}

			// 2.0f margin of error
			if (Math.abs(this.position.getX() - this.destinationPoint.getX()) < 2.0f
					&& Math.abs(this.position.getY() - this.destinationPoint.getY()) < 2.0f) {
				if (isPatrolling)
					currentEdge = destinationPoint;
				this.isMoving = false;
			}
		}
		// if the Policeman is neither patrolling nor following the robber then he should patrol
		if (!this.isPatrolling && !this.isFollowingRobber)
			startRoundPatrol();

		lookForRobber();

		// draw the image at the Position of the SG
		this.image.draw(this.position.getX(), this.position.getY());
	}

	//====================================================================================================
	// PATROL
	public void startRoundPatrol() {

		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// if the SG is already moving don't interrupt him
				// wait until he reaches his position
				if (isMoving)
					return;

				if (currentEdge.getX() == topLeftEdge.getX()
						&& currentEdge.getY() == topLeftEdge.getY()) {
					// change the destination point
					destinationPoint.setLocation(topRightEdge.getX(),
							topRightEdge.getY());
					isPatrolling = true;

				} else if (currentEdge.getX() == topRightEdge.getX()
						&& currentEdge.getY() == topRightEdge.getY()) {
					// change the destination point
					destinationPoint.setLocation(bottomRightEdge.getX(),
							bottomRightEdge.getY());
					isPatrolling = true;
				} else if (currentEdge.getX() == bottomRightEdge.getX()
						&& currentEdge.getY() == bottomRightEdge.getY()) {
					// change the destination point
					destinationPoint.setLocation(bottomLeftEdge.getX(),
							bottomLeftEdge.getY());
					isPatrolling = true;

				} else if (currentEdge.getX() == bottomLeftEdge.getX()
						&& currentEdge.getY() == bottomLeftEdge.getY()) {
					// change the destination point
					destinationPoint.setLocation(topLeftEdge.getX(),
							topLeftEdge.getY());
					isPatrolling = true;
				}

				// move to the destination point
				move(destinationPoint);
			}
		});
		patrolTimer.start();

	}

	public void startRandomPatrol() {
		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				float x = currentEdge.getX();
				float y = currentEdge.getY();

				Point nextPoint = (new Random().nextBoolean()) ? new Point(x
						* -1, y) : new Point(x, y * -1);
				move(nextPoint);

			}
		});
		patrolTimer.start();

		this.isPatrolling = true;
	}
	//====================================================================================================

	public void lookForRobber(){
		float distance = (float)  Math.sqrt(Math.pow(this.position.getX()-robber.position.getX(), 2.0) + Math.pow(this.position.getY()-robber.position.getY(), 2.0));

		float distanceToBank = (float) Math.sqrt(Math.pow(this.position.getX()-guardedBank.rect.getCenterX(), 2.0) + Math.pow(this.position.getY()-guardedBank.rect.getCenterY(), 2.0));
		if (distance < 2.0f && !robber.isCaught)
		{
			// the Robber has been caught
			// send a message to signal game over
			arrestRobber(robber);
		}

		// TODO: recheck the boolean
		if (distance < chaseDistance && distanceToBank < 200.0f )
		{
			followRobber();
			return;
		}
		if (distance < visionDistance)
		{
			// TODO: implement
			Point center = new Point(this.position.getX(), this.position.getY()); 
			callPolice(center, 800.0f);
		}
		else
		{
			this.isFollowingRobber  = false; 
		}
	}

	public void followRobber(){
		patrolTimer.stop();
		this.isPatrolling = false;
		this.isFollowingRobber = true;
		move(new Point(robber.position.getX(), robber.position.getY()));
	}

	/**
	 * Call police if a nearby robber is detected.
	 * 
	 * @return
	 */
	public void callPolice(Point center, float error) {

		PoliceOffice.callPolice(this.guardedBank);
	}

	public boolean arrestRobber(Robber robber) {
		robber.isCaught = true; 
		this.play.gameOver();
		return true;
	}
}