package game.city.road;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * A road, which may or may not be lighted.
 * 
 * @author sami
 * 
 */
public class Road {
	/**
	 * Indicates whether the road is lighted.
	 */
	public boolean isLighted;

	/**
	 * The frame of the highway to delimit the road
	 */
	protected Rectangle rect; 
	
	protected Point position;  
	
	/**
	 * Creates a road.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param isLighted
	 */
	public Road(Point position, Rectangle rect) {
		// set the position
		this.position = position; 
		
		// set the rectangle
		this.rect = rect; 
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

}
