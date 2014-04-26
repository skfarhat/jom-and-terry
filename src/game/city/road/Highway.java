package game.city.road;

import game.city.person.PoliceOffice;
import game.city.person.Robber;
import game.states.Play;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

/**
 * A highway class
 * 
 * @author sami
 * 
 */
public class Highway extends Road {

	private static int notifyPolice = 3; 
	private int countRobberPassed = 0; 

	private boolean intersectRoad = false; 
	
	public static ArrayList<Highway> highways = new ArrayList<>(5);
	
	
	/**
	 * Create a highway.
	 * 
	 * @param positionX
	 * @param positionY
	 */
	public Highway(Point position, Rectangle rect) {
		super(position,rect);
		
		// add the created highway to the array list 
		highways.add(this);
	}
	
	public void draw() {
		final Robber robber = Play.getInstance().getRobber(); 
		
		// if the robber intersect the highway
		// the boolean is needed so that
		
		if (robber.rect.intersects(getRect()) && !intersectRoad){
			intersectRoad = true; 
			
			// increment the number of times the robber passed by the highway
			countRobberPassed++; 
			if (countRobberPassed == notifyPolice){

				// notify the police office if the robber passes several times 
				notifiyPoliceOffice();
				
				// reset the counter
				countRobberPassed = 0; 
			}
		}
		else if (!robber.rect.intersects(getRect())){
			intersectRoad = false; 
		}
	}
	
	public void notifiyPoliceOffice(){
		PoliceOffice.callPolice(this);
	}

}
