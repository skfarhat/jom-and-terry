package game.city.building;

import game.Play;
import game.city.person.Policeman;
import game.city.person.Robber;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

public class PoliceOffice {

	private Robber robber; 
	private Integer numberOfPolicemen = 2; 
	private ArrayList<Policeman> policeForceArray;
	
	public PoliceOffice(Play play, Robber robber) throws SlickException {

		// set robber
		this.robber = robber;
		
		// init the Police Force Array
		policeForceArray = new ArrayList<>(numberOfPolicemen);
	
		
		for (int i=0; i< numberOfPolicemen; i++) {
			int x = 400, y = 500;

			Policeman police = new Policeman(play, this.robber, x, y, "Police-1", 50.0f);
			
			policeForceArray.add(police); // add Police1 to the force
		}
	}

	/**
	 * 
	 * @param center
	 * @param error
	 */
	public void suspectedRegion(Point center, float error){
		Circle region;
		for (Policeman police: policeForceArray){	
			// set the suspected region for the robber 
			// it is a circle
			region= new Circle(center.getCenterX(), center.getCenterY(), error);
			police.checkoutRegion(region);
		}
	}
	/**
	 * Draw all the policemen
	 */
	public void draw(){ 
		for (Policeman police : this.policeForceArray)
			police.draw();
	}
	/**
	 * Getter function for PoliceForce ArrayList
	 * @return ArrayList<Policeman> 
	 */
	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
	}
}
