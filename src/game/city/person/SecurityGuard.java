package game.city.person;

import game.Globals;
import game.city.building.Area;
import game.city.building.Bank;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
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

	private String sgImgPath = "res/Security Guard.png";

	private Point destinationPoint;
	private Image image;
	private boolean isFollowingRobber = false;

	public Rectangle rect; // frame of the security guard
	public Vector2f direction; // direction in which the Security is moving

	private Robber robber; 

	// =========================================================================================================
	// PATROL
	private Timer patrolTimer;
	private boolean isPatrolling = false;
	private boolean policeCalled = false; 
	// edges of the bank to be guarded
	private Point topLeftEdge, topRightEdge, bottomRightEdge, bottomLeftEdge;

	/*
	 used to know at which edge the SG is currently
	 we could use the position and compare to the edge, but positions are not always exact
	 so this field is used to avoid interval comparing
	 */
	private Point currentEdge;

	/**
	 * The bank the Security Guard is responsible for guarding
	 */
	Bank guardedBank = null;

	/**
	 * Creates a security guard.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public SecurityGuard(Area area, Point position, String name,
			double velocity, Bank guardedBank) throws SlickException {
		super(null, name, velocity);

		this.area = area; 

		this.guardedBank = guardedBank;

		// Set the image of the policeman
		this.image = new Image(sgImgPath);

		spriteWidth = this.image.getWidth(); 
		spriteHeight= this.image.getHeight(); 

		// Set the rectangle of the player
		this.rect = new Rectangle(position.getX(), position.getY(), spriteWidth,
				spriteHeight);

		// Set the image of the SG
		this.image = new Image(sgImgPath);

		// FIXME: Careful this might work now, but if the position given to the
		// SG by the Bank is changed (right edge aw shi)
		// because the SG patrols around the bank, so would need to change the order 


		// NOTE: Remember that the position of the image is at the top right edge, so we shoouldn't add for getMaxY() or getMaxX()
		this.currentEdge = topLeftEdge = new Point(
				guardedBank.getRect().getMinX() - this.rect.getWidth(),
				guardedBank.getRect().getMinY() - this.rect.getHeight());
		topRightEdge = new Point(
				guardedBank.getRect().getMaxX(),
				guardedBank.getRect().getMinY() - this.rect.getHeight());
		bottomRightEdge = new Point(
				guardedBank.getRect().getMaxX(),
				guardedBank.getRect().getMaxY() ) ;
		bottomLeftEdge = new Point(
				guardedBank.getRect().getMinX() - this.rect.getWidth(),
				guardedBank.getRect().getMaxY());

		this.position = new Point(currentEdge.getX(), currentEdge.getY()); 

		this.destinationPoint = position;

		this.startRoundPatrol();

		// add the created security guard to the security guards array
		//		securityGuards.add(this);
	}

	public void setRobber(Robber robber) {
		this.robber = robber; 
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

				Point tempDestPoint = new Point(0, 0); 

				// if the SG is already moving don't interrupt him
				// wait until he reaches his position
				if (isMoving)
					return;

				if (currentEdge.getX() == topLeftEdge.getX()
						&& currentEdge.getY() == topLeftEdge.getY()) {
					// change the destination point
					tempDestPoint.setLocation(topRightEdge.getX(),
							topRightEdge.getY());
					isPatrolling = true;

				} else if (currentEdge.getX() == topRightEdge.getX()
						&& currentEdge.getY() == topRightEdge.getY()) {
					// change the destination point
					tempDestPoint.setLocation(bottomRightEdge.getX(),
							bottomRightEdge.getY());
					isPatrolling = true;
				} else if (currentEdge.getX() == bottomRightEdge.getX()
						&& currentEdge.getY() == bottomRightEdge.getY()) {
					// change the destination point
					tempDestPoint.setLocation(bottomLeftEdge.getX(),
							bottomLeftEdge.getY());
					isPatrolling = true;

				} else if (currentEdge.getX() == bottomLeftEdge.getX()
						&& currentEdge.getY() == bottomLeftEdge.getY()) {
					// change the destination point
					tempDestPoint.setLocation(topLeftEdge.getX(),
							topLeftEdge.getY());
					isPatrolling = true;
				}

				// move to the destination point
				move(tempDestPoint);
			}
		});
		patrolTimer.start();

	}

	public void startRandomPatrol() {
		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				float x = currentEdge.getX();
				float y = currentEdge.getY();


				//FIXME: wrong
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
		final float distanceToRobber = (float)  Math.sqrt(Math.pow(this.position.getX()-robber.position.getX(), 2.0) + Math.pow(this.position.getY()-robber.position.getY(), 2.0));
		final float distanceToBank = (float) Math.sqrt(Math.pow(this.position.getX()-guardedBank.getRect().getCenterX(), 2.0) 
				+ Math.pow(this.position.getY()-guardedBank.getRect().getCenterY(), 2.0));


		if (distanceToRobber < Globals.SECURITY_GUARD_ARREST_DISTANCE && !robber.isCaught)
		{
			// the Robber has been caught
			// send a message to signal game over
			arrestRobber(robber);
		}

		/*
		 * If the robber is near the security guard and the latter is not very far from the guarded bldg
		 */
		if (distanceToRobber < Globals.SECURITY_GUARD_CHASE_DISTANCE
				&& distanceToBank < Globals.SECURITY_GUARD_MAX_DISTANCE_FROM_BLDG)
			followRobber();
		else
			this.isFollowingRobber = false; 

		/*
		 * If the security guard can see the robber, and he hasn't already called the police then call them
		 */
		if (distanceToRobber < Globals.SECURITY_GUARD_VISION_DISTANCE && !policeCalled)
		{
			Point center = new Point(this.position.getX(), this.position.getY());
			callPolice(center, 800.0f);
		}
		else if (distanceToRobber > Globals.SECURITY_GUARD_VISION_DISTANCE)
			policeCalled = false; 
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
		policeCalled = true; 
		guardedBank.callPolice();
	}

	public boolean arrestRobber(Robber robber) {
		robber.isCaught = true;
		boolean userIsPolice = area.getPoliceOffice().isUserIsPolice();
		// FIXME: find the variable
		Play.getInstance().gameOver(userIsPolice);
		return true;
	}
}