package game.city.building;

import game.AudioGame;
import game.Globals;
import game.city.person.Occupant;
import game.city.person.Robber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Timer;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * Buildings abstract class.
 * 
 * @author sami
 * 
 */
public abstract class Building extends Observable{

	/**
	 * The amount of money a building has.
	 */
	public static ArrayList<Building> buildings = new ArrayList<>(20); 

	// Observers
	public ArrayList<Observer> observers = new ArrayList<>(3);

	// Occupants
	protected ArrayList<Occupant> occupants = new ArrayList<>(3);
	public int occupantsOnVacation = 0; 

	// Building Information
	private BuildingInfo bldgInfo; 
	protected boolean showBuildingInfo = false;
	public boolean isHighlighted;
	private Timer displayBuildingInfoTimer;

	//==============================================================================================================================
	// ROBBING
	// FIXME: ROBBING DURATION modify for each building
	private final static int ROBBING_DURATION = 3000;
	private final static int ROBBING_UPDATE = 100;
	private Timer robbingTimer = null;

	private boolean isBeingRobbed;
	private boolean isCompletelyRobbed = false; 
	private float robbedPercent = 0.0f; 
	//==============================================================================================================================


	public Integer ID; 
	public Integer money;
	public Integer score; 

	public Point position; 
	public Rectangle rect;

	public float width, height;


	/**
	 * CONSTRUCTOR
	 * Abstract constructor only called by subclasses.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public Building(int ID, Point position, float width, float height, Integer money) {

		this.rect = new Rectangle(position.getX(), position.getY(), width, height);

		this.position = position; 

		this.width = width; 
		this.height = height; 
		this.money = money;
		this.score = money/1000; 

		// Initially the building is not highlighted
		this.isHighlighted = false;

		// Initialize the timer for showing the building information 
		displayBuildingInfoTimer = new Timer(Globals.BUILDING_INFO_DISPLAY_TIMER, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setShowBuildingInfo(false);
			}
		});
		displayBuildingInfoTimer.setRepeats(false);

		// Initialize the building info
		this.bldgInfo = new BuildingInfo(this);

		buildings.add(this);
	}

	public void rob(final Robber robber){

		if (isBeingRobbed)
			return; 

		final Building thisBuilding = this; 

		robbingTimer = new Timer(ROBBING_DURATION/ROBBING_UPDATE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (isCompletelyRobbed)
					return;  

				// the robber is in fact robbing this building
				
				robber.setRobbing(true);
				
				// add 1% (or some other percent) to the robbed percent of the building
				robbedPercent+= (1.0f/ROBBING_UPDATE);

				// keep showing the building information when the robber is robbing the building
				setShowBuildingInfo(true);

				// update the filling bar of the building
				if (robbedPercent >= 1.0f){

					// robber finished robbing this bank
					robber.addMoney(thisBuilding.money);

					//
					robber.addScore(thisBuilding.score);

					// set the money of the bank to zero
					thisBuilding.money = 0; 

					// set the boolean is completely robbed to avoid robber re-robbing
					isCompletelyRobbed = true; 

					// set the boolean is robbing to false
					robber.setRobbing(false);

					// stop the robbing timer
					robbingTimer.stop();
				}
				// update the robbed percent
				bldgInfo.getFillingBar().update(robbedPercent);

			}
		});

		// start the timer
		robbingTimer.start();
		AudioGame.play("Glass-Smash.ogg");
		setBeingRobbed(true); 
	}

	public void draw(){
		// Draw the filling bar at the xPos of the building but a bit above
		if (showBuildingInfo)
			bldgInfo.draw(position.getX(), position.getY()- BuildingInfo.BUILDING_INFO_HEIGHT);
	}

	
	public void nextFlag(){
		bldgInfo.nextFlag();
	}
	// GETTERS/SETTERS
	//==============================================================================================================================
	/**
	 * Displays the building related information using the BuildingInfo class. 
	 * Starts timer that hides BuildingInfo after a certain time interval (3sec)
	 * @param showBuildingInfo
	 */
	public void setShowBuildingInfo(boolean showBuildingInfo) {
		this.showBuildingInfo = showBuildingInfo;

		if (showBuildingInfo){
			if (displayBuildingInfoTimer.isRunning())
			{
				displayBuildingInfoTimer.stop();
			}

			displayBuildingInfoTimer.start();
		}

	}

	public void setBeingRobbed(boolean isBeingRobbed) {
		this.isBeingRobbed = isBeingRobbed;
		setChanged();
		notifyObservers();
	}

	public ArrayList<Occupant> getOccupants() {
		return occupants;
	}

	public boolean getIsCompletelyRobbed(){
		return isCompletelyRobbed; 
	}

	public String getType(){
		if (this.getClass().equals(House.class)){
			return "House"; 
		}
		else if (this.getClass().equals(Bank.class)){
			return "Bank";
		}
		else if(this.getClass().equals(Shop.class)){
			return "Shop";
		}
		else return ""; 
	}

	public boolean isInRobbingDistance(Point point){
		Rectangle rect = new Rectangle(
				this.position.getX() - Globals.BUILDING_ROBBING_DISTANCE,
				this.position.getY() - Globals.BUILDING_ROBBING_DISTANCE, 
				this.rect.getWidth() + Globals.BUILDING_ROBBING_DISTANCE, 
				this.rect.getHeight()+ Globals.BUILDING_ROBBING_DISTANCE);

		return (rect.intersects(point));
	}

}