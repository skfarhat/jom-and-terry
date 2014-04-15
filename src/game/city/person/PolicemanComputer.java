package game.city.person;

import game.Globals;
import game.city.building.Building;
import game.city.building.House;
import game.city.road.Highway;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class PolicemanComputer extends Policeman{

	private Timer patrolTimer;

	protected boolean isPatrolling = false;
	protected boolean isFollowingRobber = false;
	protected boolean isCheckingSuspectRegion= false;

	/**
	 * Array that stores the points the Policeman should visit. Each time one is visited it is removed from the ArrayList
	 */
	private ArrayList<Point> nextPoints = new ArrayList<>(5);

	public PolicemanComputer(Robber robber, Point position,
			String name, double velocity)
					throws SlickException {
		super(robber, position, name, velocity);
	}

	public void startPatrol()
	{ 
		isPatrolling = true;

		patrolTimer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer index = (new Random()).nextInt(House.getHouses().size());
				Building bldg = Building.buildings.get(index);

				move(bldg.position);
			}
		});
		patrolTimer.start();

	}

	public void stopPatrol(){
		if (patrolTimer!=null)
			patrolTimer.stop();

		this.isPatrolling = false;
	}

	@Override
	public void draw(){

		// draw the computer police only when he is visible to the robber
		if (robber.canSeePoliceman(this))
			super.draw();

		// if there are points for the policeman to check
		if (nextPoints.size()>0){

			// get the next point
			Point nextDestinationPoint = nextPoints.get(0);

			if (destPoint != nextDestinationPoint){
				// move to the next destination point
				move(nextDestinationPoint);

				this.isCheckingSuspectRegion = true;
			} 
		}

		// if Policeman is moving, change xPos and yPos
		if (isMoving) {
			float speed = (float) (Globals.VELOCITY_MULTIPLIER * velocity);

			float deltaX = this.position.getX() - this.destPoint.getX(); 
			float deltaY = this.position.getY() - this.destPoint.getY();


			// we want the policeman to go all the way horizontally then after arrivign to the correct xpos
			// go all the way vertically, so we declare a variable movingHorizontally and we set it to true whenever the policeman is going horizontally
			// if true, the second if statement is not satisfied and the yPos has to wait for the xPos to finish
			boolean movingHorizontally = false;
			if (Math.abs(deltaX) > 2.0f)
			{
				float newX = (deltaX<0)? this.position.getX() + speed: this.position.getX() + (-1)*speed; 

				this.position.setX(newX);

				this.rect.setX(this.position.getX());

				movingHorizontally = true;
			}

			// check on movingHorizontally done here
			if (Math.abs(deltaY) > 2.0f && !movingHorizontally)
			{	
				float newY = (deltaY<0)? this.position.getY() + speed: this.position.getY() + (-1)*speed;

				this.position.setY(newY);

				this.rect.setY(this.position.getY());
			}

			// 2.0f margin of error
			if (Math.abs(this.position.getX() - this.destPoint.getX()) < 2.0f
					&& Math.abs(this.position.getY() - this.destPoint.getY()) < 2.0f) {

				this.isMoving = false;	
				if (isCheckingSuspectRegion)
				{					
					nextPoints.remove(0);

					this.isCheckingSuspectRegion = false;

					this.suspectRegion = null; 
				}
			}
		}

		// if the Policeman is neither patrolling nor following the robber then he should patrol
		if (!this.isPatrolling && !this.isFollowingRobber && !userIsPolice && !isCheckingSuspectRegion)
			startPatrol();

		// see if the Robber is visible
		lookForRobber();
	}

	public void checkoutRegion(Circle region){
		// to check out a region

		//first stop patroling
		if (isPatrolling)
			stopPatrol();

		// TODO: might be useless
		setSuspectRegion(region);

		// get a random point inside the region
		Point randPoint = randomPointInCircle(suspectRegion);

		nextPoints.add(randPoint);

		// set flag for isCheckingRegion
		this.isCheckingSuspectRegion = true;
	}

	public void checkoutHighway(Highway highway){
		// to check out a highway

		//first stop patroling
		if (isPatrolling)
			stopPatrol();

		// choose two random points in the highway 
		Point firstPoint = randomPointInRect(highway.getRect());
		Point secondPoint = randomPointInRect(highway.getRect());

		// add the points to the array
		nextPoints.add(firstPoint);
		nextPoints.add(secondPoint);

		// set flag for isCheckingRegion
		this.isCheckingSuspectRegion = true;
	}

	public void setSuspectRegion(Circle suspectRegionCircle) {
		this.suspectRegion = suspectRegionCircle;
	}

	private Point randomPointInRect(Rectangle rect){
		Random rand = new Random(); 
		int x = (int) rect.getX() + rand.nextInt((int) rect.getWidth());
		
		int y = (int) rect.getY() + rand.nextInt((int) rect.getHeight());

		return new Point(x,y);

	}

	public boolean lookForRobber()
	{
		float distance = (float)  Math.sqrt(Math.pow(
				this.position.getX()-this.robber.position.getX(), 2.0) 
				+ Math.pow(this.position.getY()-this.robber.position.getY(), 2.0)
				);
		if (distance < 2.0f && !robber.isCaught)
		{
			// the Robber has been caught
			// send a message to signal game over
			arrestRobber(robber);
		}
		if (distance < Globals.POLICEMAN_VISION_DISTANCE)
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

		Play.getInstance().getRobber().addScore(0.1f);
		this.move(robber.position);
	}

	public void move(Point destPoint)
	{
		this.destPoint = destPoint;

		this.direction = new Vector2f(
				destPoint.getX()  - this.position.getX(), 
				destPoint.getY() - this.position.getY()
				);

		this.isMoving = true;
	}


}
