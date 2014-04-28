package game.city.road;

import game.Globals;
import game.city.building.Area;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * A road, which may or may not be lighted.
 * 
 * @author sami
 * 
 */
public class Road {
	
	protected Area area; 
	
	/**
	 * Indicates whether the road is lighted.
	 */
	public boolean isLighted = false;
	/**
	 * Indicates whether the road is monitored or not. 
	 */
	protected boolean isMonitored = false; 

	/**
	 * The frame of the highway to delimit the road
	 */
	protected Rectangle rect;
	protected Point position;

	// ROAD INFO
	protected RoadInfo roadInfo; 
	private Timer displayRoadInfoTimer;
	protected boolean showRoadInfo = false;
	// ==================================================================

	/**
	 * Creates a road.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param isLighted
	 */
	public Road(Area area, Point position, Rectangle rect) {
		// set the position
		this.position = position; 

		// set the rectangle
		this.rect = rect;

		// create road info
		this.roadInfo = new RoadInfo(this);

		// Initialize the timer for showing the building information 
		displayRoadInfoTimer = new Timer(Globals.BUILDING_INFO_DISPLAY_TIMER, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setShowRoadInfo(false);
			}
		});
		displayRoadInfoTimer.setRepeats(false);
	}


	public void callPolice(){
		area.getPoliceOffice().callPolice(this);
	}
	
	public void drawRoadInfo(){
		if (showRoadInfo)
			roadInfo.draw(position.getX(), position.getY()- RoadInfo.ROAD_INFO_HEIGHT);
	}
	/**
	 * Display's a road's info.
	 */
	public void displayRoadInfo() {
		//		String info = String.format("Road position (%d,%d). Lights:%s",
		//				positionX, positionY, (isLighted) ? "ON" : "OFF");
		//		System.out.println(info);
	}

	public Rectangle getRect() {
		return rect;
	}
	public Point getPosition() {
		return position;
	}

	public void setShowRoadInfo(boolean showRoadInfo) {
		this.showRoadInfo = showRoadInfo;

		if (showRoadInfo){
			if (displayRoadInfoTimer.isRunning())
			{
				displayRoadInfoTimer.stop();
			}

			displayRoadInfoTimer.start();
		}
	}
}
