package game.city.building;

import game.AudioGame;
import game.Globals;
import game.city.person.Occupant;
import game.city.person.Robber;
import game.city.person.RobberComputer;
import game.states.Savable;

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
 * @author Sami
 */
public abstract class Building extends Observable implements Savable {
	private final Area area;

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

	// ==============================================================================================================================
	// ROBBING
	private final static int ROBBING_DURATION = 3000;
	private final static int ROBBING_UPDATE = 100;
	private Timer robbingTimer = null;

	private boolean isBeingRobbed;
	private boolean isCompletelyRobbed = false;
	private float robbedPercent = 0.0f;
	// ==============================================================================================================================

	public Rectangle robbingRegion;
	protected Flag flag;
	public Integer ID;
	public Integer money;
	protected Integer score;
	public Point position;
	private Rectangle rect;
	public float width, height;

	/**
	 * CONSTRUCTOR Abstract constructor only called by subclasses.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public Building(int ID, final Area area, Point position, float width,
			float height, Integer money) {
		this.ID = ID;
		this.area = area;
		this.rect = new Rectangle(position.getX(), position.getY(), width,
				height);
		this.position = position;
		this.width = width;
		this.height = height;
		this.money = money;
		this.score = money / 100;

		// Initially the building is not highlighted
		isHighlighted = false;
		flag = new Flag();

		// Initialize the timer for showing the building information
		displayBuildingInfoTimer = new Timer(
				Globals.BUILDING_INFO_DISPLAY_TIMER, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						setShowBuildingInfo(false);
					}
				});
		displayBuildingInfoTimer.setRepeats(false);

		// Initialize the building info
		bldgInfo = new BuildingInfo(this);

		robbingRegion = new Rectangle(position.getX()
				- Globals.BUILDING_ROBBING_DISTANCE, position.getY()
				- Globals.BUILDING_ROBBING_DISTANCE, rect.getWidth() + 2
				* Globals.BUILDING_ROBBING_DISTANCE, rect.getHeight() + 2
				* Globals.BUILDING_ROBBING_DISTANCE);
	}

	/**
	 * Starts the process of robbing the building
	 * 
	 * @param robber
	 */
	public void rob(final Robber robber) {

		if (isBeingRobbed)
			return;

		final Building thisBuilding = this;

		robbingTimer = new Timer(ROBBING_DURATION / ROBBING_UPDATE,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {

						if (isCompletelyRobbed)
							return;

						// make sure the robber is in robbing distance of the
						// building
						if (!isInRobbingDistance(robber.rect)) {
							robbedPercent = 0.0f;

							// set the boolean is robbing to false
							robber.setRobbing(false);

							setBeingRobbed(false);

							// update the robbed percent
							bldgInfo.getFillingBar().update(robbedPercent);

							isCompletelyRobbed = false;

							// stop the robbing timer
							robbingTimer.stop();
							return;
						}

						// the robber is in fact robbing this building
						robber.setRobbing(true);

						// add some percent to the robbed percent of the
						// building
						robbedPercent += (1.0f / ROBBING_UPDATE);

						// keep showing the building information when the robber
						// is robbing the building
						setShowBuildingInfo(true);

						// update the robbed percent
						bldgInfo.getFillingBar().update(robbedPercent);

						// update the filling bar of the building
						if (robbedPercent >= 1.0f) {

							robber.addMoney(thisBuilding.money); // add money to
																	// the
																	// robber
							robber.addScore(thisBuilding.score); // add score to
																	// the
																	// robber
							area.getPoliceOffice().decreaseScoreBy(
									thisBuilding.score);// decrease the score of
														// the police officers
							thisBuilding.money = 0; // set the money of the bank
													// to zero
							robber.setRobbing(false); // set the boolean is
														// robbing to false
							isCompletelyRobbed = true; // set the boolean is
														// completely robbed to
														// avoid robber
														// re-robbing

							if (robber instanceof RobberComputer) {
								((RobberComputer) robber)
										.setBuildingToRob(null);
							}

							area.incrementNumberOfRobbedBuildings();
							robbingTimer.stop(); // stop the robbing timer
						}

					}
				});
		robbingTimer.start();

		AudioGame.play("Glass-Smash.ogg");
		setBeingRobbed(true);
	}

	/**
	 * Draw the building using its BuildingInfo
	 * 
	 * @param showFlag
	 */
	public void draw(boolean showFlag) {

		// Draw the filling bar at the xPos of the building but a bit above
		if (showBuildingInfo)
			bldgInfo.draw(position.getX(), position.getY()
					- BuildingInfo.BUILDING_INFO_HEIGHT);

		if (showFlag && flag.image != null)
			flag.draw(bldgInfo.getFrame().getX(),
					bldgInfo.getFrame().getY() + 25);
	}

	/**
	 * Cycle through flag colors
	 * 
	 * @param message
	 *            string
	 */
	public void nextFlag() {
		flag.nextFlag();
	}

	public void callPolice() {
		area.getPoliceOffice().callPolice(this);
	}

	/**
	 * Get the nearest point accessible to the policeman next to this building
	 * (for patrolling)
	 * 
	 * @return
	 */
	public Point getNearestFreePoint() {
		boolean b = true;
		Point p = null;
		while (b) {

			int x = (int) (robbingRegion.getMinX() + Globals.random
					.nextInt((int) robbingRegion.getWidth()));
			int y = (int) (robbingRegion.getMinY() + Globals.random
					.nextInt((int) robbingRegion.getHeight()));

			p = new Point(x, y);

			// make sure the point does not intersect with the building (it is only near it) 
			// and that it is contained within the map (not outside of the bounds)
			b = rect.includes(x, y) || !area.getCityMap().isWithinBounds(p);
					
		}
		return p;
	}

	// GETTERS/SETTERS
	// ==============================================================================================================================
	/**
	 * Displays the building related information using the BuildingInfo class.
	 * Starts timer that hides BuildingInfo after a certain time interval (3sec)
	 * 
	 * @param showBuildingInfo
	 */
	public void setShowBuildingInfo(boolean showBuildingInfo) {
		this.showBuildingInfo = showBuildingInfo;

		if (showBuildingInfo) {
			if (displayBuildingInfoTimer.isRunning()) {
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

	public boolean getIsCompletelyRobbed() {
		return isCompletelyRobbed;
	}

	public String getType() {
		if (this.getClass().equals(House.class)) {
			return "House";
		} else if (this.getClass().equals(Bank.class)) {
			return "Bank";
		} else if (this.getClass().equals(Shop.class)) {
			return "Shop";
		} else
			return "";
	}

	public boolean isInRobbingDistance(Rectangle rect) {
		Rectangle temprect = new Rectangle(this.position.getX()
				- Globals.BUILDING_ROBBING_DISTANCE, this.position.getY()
				- Globals.BUILDING_ROBBING_DISTANCE, this.rect.getWidth() + 2
				* Globals.BUILDING_ROBBING_DISTANCE, this.rect.getHeight() + 2
				* Globals.BUILDING_ROBBING_DISTANCE);

		return temprect.intersects(rect);
	}

	protected void setCompletelyRobbed(boolean isCompletelyRobbed) {
		this.isCompletelyRobbed = this.isBeingRobbed = isCompletelyRobbed;
		if (isCompletelyRobbed)
			this.bldgInfo.getFillingBar().update(1.0f);
	}

	public Rectangle getRect() {
		return rect;
	}

	public Flag getFlag() {
		return flag;
	}

	public Area getArea() {
		return area;
	}

	// FLAG
	// ==============================================================================================================================

	/**
	 * Add flag without a message
	 */
	protected void addFlag() {
		flag = new Flag();
	}

	/**
	 * Remove flag by setting its value to null
	 */
	protected void removeFlag() {
		flag = null;
	}

}