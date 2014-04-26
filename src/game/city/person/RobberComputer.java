package game.city.person;

import game.Globals;
import game.city.building.Building;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

public class RobberComputer extends Robber {

	private boolean goingToRob = false; 
	private Point destPoint; 
	private Timer robbingTimer; 

	private Building buildingToRob = null;

	public RobberComputer() throws SlickException {
		super();

		// Start the robbing
		startRobbing(); 
	}

	public void startRobbing() {

		robbingTimer = new Timer(10000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// if the robber is not robbing a building
				if (!isRobbing && !goingToRob) 
				{
					// get random number between 0-size
					int randNum = rand.nextInt(Building.buildings.size()); 

					// get building at the random index
					final Building bldg = Building.buildings.get(randNum);

					// if the bldg is not completely robbed 
					if (!bldg.getIsCompletelyRobbed()){
						// go to the building 
						moveAndRob(bldg);

					}
				}
			}
		});

		// start the robbing timer
		robbingTimer.start(); 
	}

	public boolean moveAndRob(Building bldg){
		// If null return false
		if (bldg == null)
			return false;

		this.goingToRob = true; 

		// If is in robbing distance directly rob
		if (bldg.isInRobbingDistance(this.rect))
			return  rob(bldg); 

		// set the building to rob
		this.buildingToRob =bldg;

		// move
		move(bldg.position);

		return true; 

	}

	public void draw(boolean showRobber) {
		super.draw(showRobber);

		if (isMoving) {

			float deltaX = this.position.getX() - this.destPoint.getX(); 
			float deltaY = this.position.getY() - this.destPoint.getY();

			// we want the robber to go all the way horizontally then after arriving to the correct xpos
			// go all the way vertically, so we declare a variable movingHorizontally and we set it to true whenever the policeman is going horizontally
			// if true, the second if statement is not satisfied and the yPos has to wait for the xPos to finish
			boolean movingHorizontally = false;
			if (Math.abs(deltaX) > 2.0f)
			{
				if (deltaX<0)
					moveRight();
				else
					moveLeft();

				movingHorizontally = true;
			}

			// check on movingHorizontally done here
			if (Math.abs(deltaY) > 2.0f && !movingHorizontally)
			{	
				if (deltaY<0)
					moveDown();
				else
					moveUp();
			}

			Vector2f differenceVector = new Vector2f(
					this.position.getX() - this.destPoint.getX(),
					this.position.getY() - this.destPoint.getY()
					); 

			// 2.0f margin of error
			if (differenceVector.length() < 2.0f) {
				goingToRob = false; 
				this.isMoving = false;
				if (buildingToRob !=null)
				{
					rob(buildingToRob);
					buildingToRob = null;
				}
			}
		}
	}

	public void move(Point destPoint)
	{
		// set the Destination Point
		this.destPoint = destPoint; 

		// set the direction of the policeman
		//		this.vectorDirection = new Vector2f(destPoint.getX()  - this.position.getX(), destPoint.getY() - this.position.getY());

		// set the boolean is moving to true
		this.isMoving = true; 

	}	

	public void fleePoliceman(Vector2f direction)
	{
		float deltaX = 0 ,  deltaY = 0;

		// he is closer horizontally to the policeman
		// move away horizontally
		if (Math.abs(direction.x) < Math.abs(direction.y))
			deltaX = direction.x/2; 
		else
			deltaY = direction.y/2;

		// point to go to 
		Point toPoint = new Point(deltaX+ this.position.getX(), this.position.getY()+ deltaY);

		// move there
		this.move(toPoint);

	}

	public Policeman canSeePolice()
	{
		// check that the distance between the robber and all the police force is less than 50.0 
		// If less than 50.0f for some police return true
		for (Policeman policeman : PoliceOffice.policeForceArray)
		{
			float distance = (float)  Math.sqrt(Math.pow(policeman.position.getX()-this.position.getX(), 2.0) 
					+ Math.pow(policeman.position.getY()-this.position.getY(), 2.0));

			if (distance < Globals.ROBBER_VISION_DISTANCE)
				return policeman;
		}

		return null ;  	
	}

	public void stopTimers(){
		robbingTimer.stop();
	}

	// Movement without collisions
	// ==============================================================================================================================
	public boolean moveRight() {
		this.currentAnimation.start();
		this.position.setX((float) (this.position.getX()+Globals.VELOCITY_MULTIPLIER*velocity));		
		this.rect.setX(this.position.getX());
		this.currentAnimation = this.rightWalkAnimation;


		return true; 
	}

	public boolean moveLeft() {
		this.currentAnimation.start();
		this.position.setX((float) (this.position.getX()-Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setX(this.position.getX());
		this.currentAnimation = this.leftWalkAnimation;
		return true; 
	}

	public boolean moveUp() {
		this.position.setY((float) (this.position.getY()-Globals.VELOCITY_MULTIPLIER*velocity));

		this.rect.setY(this.position.getY());
		this.currentAnimation = this.upWalkAnimation;
		return true; 
	}

	public boolean moveDown() {
		this.position.setY((float) (this.position.getY()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
		this.currentAnimation = this.downWalkAnimation;

		return true; 
	}

	public void setBuildingToRob(Building buildingToRob) {
		this.buildingToRob = buildingToRob;
	}

}
