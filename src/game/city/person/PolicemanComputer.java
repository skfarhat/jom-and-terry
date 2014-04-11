package game.city.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.Timer;

import game.Globals;
import game.city.building.Building;
import game.city.building.House;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

public class PolicemanComputer extends Policeman{

	private Timer patrolTimer;
	
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
		super.draw();
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
					this.isCheckingSuspectRegion = false;
					this.suspectRegion = null; 
				}
			}
		}
		
		// if the Policeman is neither patrolling nor following the robber then he should patrol
		if (!this.isPatrolling && !this.isFollowingRobber && !userIsPolice)
			startPatrol();
		

		if (!userIsPolice)
			// see if the Robber is visible
			lookForRobber();
	}

	public void checkoutRegion(Circle region){
		// to check out a region

		//first stop patroling
		if (isPatrolling)
			stopPatrol();

		setSuspectRegion(region);

		// get a random point inside the region
		Point randPoint = randomPointInCircle(suspectRegion);

		move(randPoint);

		// set flag for isCheckingRegion
		this.isCheckingSuspectRegion = true;
	}
	
	private Point randomPointInCircle(Circle circle){
		Random rand = new Random(); 

		float randAngle =  (float) (rand.nextFloat() * (Math.PI * 2.0f)); 		// random angle 
		float randRadius = (float)  (rand.nextFloat()%circle.radius); 			// random radius

		float x = circle.getCenterX() + (float) (randRadius * Math.cos(randAngle));
		float y = circle.getCenterY() + (float) (randRadius* Math.sin(randAngle));

		return new Point(x,y);
	}

}
