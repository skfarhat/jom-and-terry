package game.city.person;

import game.Game;
import game.Globals;
import game.city.building.Area;
import game.city.building.Building;
import game.city.road.Road;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;

public class PolicemanComputer extends Policeman implements Mover {

	private Timer patrolTimer;

	enum PoliceState{
		PATROLLING, 
		FOLLOWING_ROBBER, 
		CHECKING_SUSPECT_REGION
	};

	protected PoliceState policeState; 

	protected Whistle lastWhistleHeard;
	/// 
	private Path pathToTarget;
	private Step nextStep; 
	private Step currentStep;
	private int pathToTargetIndex = 0;
	Timer moveTimer; 

	AStarPathFinder pathFinder = new AStarPathFinder(area.getCityMap() , Integer.MAX_VALUE, true);

	/**
	 * Array that stores the points the Policeman should visit. Each time one is
	 * visited it is removed from the ArrayList
	 */
	private ArrayList<Point> nextPoints = new ArrayList<>(5);
	protected Point nextPoint; 

	public PolicemanComputer(Area area, Robber robber, Point position,
			String name, double velocity) throws SlickException {
		super(area, robber, position, name, velocity);


		// keep changing the position of the policemen until he doesn't collide with anything
		while (collides()){
			position.setX(Globals.random.nextInt(800));
			position.setY(Globals.random.nextInt(800));
			rect.setX(position.getX());
			rect.setY(position.getY());
		}

		stop(); 

		this.userIsPolice = false;
	}

	public void startPatrol() {
		policeState = PoliceState.PATROLLING; 

		if (moveTimer !=null)
		{			
			moveTimer.stop();
			moveTimer = null;
		}

		patrolTimer = new Timer(2000, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Integer index = (new Random()).nextInt(area.getHouses().size());
				Building bldg = area.getBuildings().get(index);

				Point pnt = bldg.getNearestFreePoint();
				move(pnt);
			}
		});
		//		patrolTimer.setRepeats(false);
		patrolTimer.start();


	}

	public void stopPatrol() {
		if (patrolTimer != null)
			patrolTimer.stop();
	}
	
	public void stopMovingTimer() {
		if (moveTimer != null)
			moveTimer.stop(); 
	}

	public void heardWhistle(Whistle whistle){
		if (whistle !=null && whistle != lastWhistleHeard)
			if (whistle.canBeHeard){
				lastWhistleHeard = whistle;
				if(Globals.distance(whistle.position, position) < Globals.WHISTLE_HEAR_DISTANCE){
					System.out.println("Heard whistle");
					Circle region = new Circle (whistle.position.getX(), whistle.position.getY(), 50.0f); 
					checkoutRegion(region);
				}
			}
	}

	@Override
	public void draw() {
		// check if heard whistle
		heardWhistle(robber.getWhistle());

		// draw the computer police only when he is visible to the robber
		if (robber.canSeePoliceman(this))
			super.draw();

		//				//
//						// if there are points for the policeman to check
//						if (nextPoints.size() > 0) {
//				
//							// get the next point
//							Point nextDestinationPoint = nextPoints.get(0);
//				
//							if (destPoint != nextDestinationPoint) {
//								// move to the next destination point
//								move(nextDestinationPoint);
//				
//								this.isCheckingSuspectRegion = true;
//							}
//						}

//		// Print: squares
//		if (pathToTarget != null)
//			for (int i=0; i < pathToTarget.getLength(); i++)
//			{
//				Step step = pathToTarget.getStep(i);
//				Rectangle rect = new Rectangle(
//						step.getX()*Globals.TILE_SIZE,  
//						step.getY()*Globals.TILE_SIZE,
//						10,10);
//				Game.getInstance().getContainer().getGraphics().draw(rect);
//			}

		// see if the Robber is visible
		lookForRobber();
	}

	public void checkoutRegion(Circle region) {

		if (policeState == PoliceState.FOLLOWING_ROBBER)
			return;

		if (policeState == PoliceState.PATROLLING)
			stopPatrol();

		if (suspectRegion == region)
			return; 

		policeState = PoliceState.CHECKING_SUSPECT_REGION;
		suspectRegion = region;		

		Point randPoint = randomPointInCircle(suspectRegion);
		move(randPoint);
//		nextPoints.add(randPoint);
	}

	public void checkoutHighway(Road road) {
//		// to check out a highway
//
//		// first stop patroling
//		if (policeState == PoliceState.PATROLLING)
//			stopPatrol();
//
//		// choose two random points in the highway
//		Point firstPoint = randomPointInRect(road.getRect());
//		Point secondPoint = randomPointInRect(road.getRect());
//
//		// add the points to the array
//		nextPoints.add(firstPoint);
//		nextPoints.add(secondPoint);
//
//		// set flag for isCheckingRegion
//		policeState = PoliceState.CHECKING_SUSPECT_REGION; 
		
		if (policeState == PoliceState.FOLLOWING_ROBBER)
			return;

		if (policeState == PoliceState.PATROLLING)
			stopPatrol();

		policeState = PoliceState.CHECKING_SUSPECT_REGION;;		

		Point randPoint = randomPointInRect(road.getRect());
		move(randPoint);
	}

	private Point randomPointInRect(Rectangle rect) {
		Random rand = new Random();
		int x = (int) rect.getX() + rand.nextInt((int) rect.getWidth());

		int y = (int) rect.getY() + rand.nextInt((int) rect.getHeight());

		return new Point(x, y);

	}

	public boolean lookForRobber() {
		float distance = (float) Math.sqrt(Math.pow(this.position.getX()
				- this.robber.position.getX(), 2.0)
				+ Math.pow(this.position.getY() - this.robber.position.getY(),
						2.0));

		//FIXME: make general the erorr 16.0f
		if (distance < 16.0f && !robber.isCaught) {
			// the Robber has been caught
			// send a message to signal game over
			arrestRobber(robber);
		}
		if (distance < Globals.POLICEMAN_VISION_DISTANCE) {
			followRobber();
			return true;
		} 
		else if (policeState == PoliceState.CHECKING_SUSPECT_REGION){
			policeState = PoliceState.CHECKING_SUSPECT_REGION;

		}

		else { 
			if (policeState != PoliceState.PATROLLING){
				startPatrol();
			}
		}

		return false;
	}

	private void followRobber() {
		if (patrolTimer !=null){
			stopPatrol();
		}

		policeState = PoliceState.FOLLOWING_ROBBER; 
		Play.getInstance().getRobber().addScore(0.1f);

		move(robber.position);
	}

	public void move (final Point destPos){
		// Timer Task declaration
		final int period = 80; 
		final int interval = Globals.TILE_SIZE; 
		final PolicemanComputer thisPC = this;

		final ActionListener moveActionListener = new ActionListener() {
			int count = 0;	

			@Override
			public void actionPerformed(ActionEvent e) {

				// Source position (rounded)
				int posX = (int) position.getX()/Globals.TILE_SIZE;
				int posY = (int) position.getY()/Globals.TILE_SIZE;

				// Target position (rounded)
				int targetX = (int) destPos.getX()/Globals.TILE_SIZE;
				int targetY = (int) destPos.getY()/Globals.TILE_SIZE;


				pathToTarget = pathFinder.findPath((Mover) thisPC, posX, posY, targetX, targetY);
				pathToTargetIndex = 0; 

				if (pathToTarget == null)
				{
					arrived(); 
					moveTimer.stop();
					moveTimer = null; 
					return; 
				}

				// After here pathToTarget cannot be equal to null

				if (pathToTargetIndex < pathToTarget.getLength()-1) {
					currentStep = pathToTarget.getStep(pathToTargetIndex);
					nextStep 	= pathToTarget.getStep(pathToTargetIndex+1);
				}
				else {
					moveTimer.stop();
					moveTimer = null;
					return; 
				}

				if (count==interval){
					count = 0; // reset count

					// make sure there are more steps to get
					if (pathToTargetIndex < pathToTarget.getLength() - 1){
						pathToTargetIndex++;
						currentStep = nextStep;
						nextStep = pathToTarget.getStep(pathToTargetIndex);
					}
					// if there are no more steps cancel the timer
					else{
						moveTimer.stop();
						moveTimer = null; 		
						return; // Testing..
					}
				}

				int stepX = (nextStep.getX() - currentStep.getX()) * interval;
				int stepY = (nextStep.getY() - currentStep.getY()) * interval;				

				// compute delta of prev and next steps
				float newX = position.getX() + stepX/interval; 
				float newY = position.getY() + stepY/interval; 				

				position.setX(newX);
				position.setY(newY);

				count++; 
			}				
		};

		// Get Path
		if (moveTimer == null)
		{
			// Source position (rounded)
			int posX = (int) position.getX()/Globals.TILE_SIZE;
			int posY = (int) position.getY()/Globals.TILE_SIZE;

			// Target position (rounded)
			int targetX = (int) destPos.getX()/Globals.TILE_SIZE;
			int targetY = (int) destPos.getY()/Globals.TILE_SIZE;

			pathToTarget = pathFinder.findPath((Mover) this, posX, posY, targetX, targetY);
			pathToTargetIndex = 0;
			moveTimer = new Timer(period/interval, moveActionListener);
			moveTimer.start();
		}
	}
	public void arrived(){
//		System.out.println("Arrived");
//
		if (policeState == PoliceState.CHECKING_SUSPECT_REGION) {
			policeState = PoliceState.PATROLLING; 

		}
	}

	public boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)


		// Collision with boundaries
		Line[] boundLines = area.getMapBounds();
		for (Line line : boundLines) {
			if (line.intersects(this.rect))
				return true;
		}

		nearByBldg = null; 
		boolean isInCollision = false;
		for (Building bldg: area.getBuildings()) {
			if (rect.intersects(bldg.getRect())) {
				isInCollision = true;
				nearByBldg = bldg; 
				bldg.setShowBuildingInfo(true);
				break;
			}
			if (this.rect.intersects(bldg.robbingRegion)) {
				this.nearByBldg = bldg;
				bldg.setShowBuildingInfo(true);
			}
		}
		//		final int x = (int)position.getX() / Globals.TILE_SIZE;
		//		final int y = (int)position.getY() / Globals.TILE_SIZE;

		return area.getCityMap().blocked(this) || isInCollision; 
	}

}
