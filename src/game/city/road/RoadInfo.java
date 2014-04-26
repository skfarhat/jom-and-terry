package game.city.road;

import game.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

public class RoadInfo {

	protected final static int ROAD_INFO_HEIGHT = 50; 
	private Rectangle frame;
	private Road road; 
	
	/**
	 * Graphics instance of the container 
	 */
	private final Graphics g = Game.getInstance().getContainer().getGraphics();
	
	public RoadInfo(Road road) {
		this.frame = new Rectangle(road.position.getX(), road.position.getY(), road.getRect().getWidth(), ROAD_INFO_HEIGHT);

		this.road = road; 
	} 

	public void draw(float x, float y) {
		
		// the type of the road

		g.drawString(String.format("Lights: %s", (road.isLighted)? "ON": "OFF"), frame.getX(), frame.getY() -10);
		
		g.drawString(String.format("Monitored: %s", (road.isMonitored)? "YES": "NO"), frame.getX(), frame.getY() -25);
		
	}

	public Rectangle getFrame() {
		return frame;
	}

}
