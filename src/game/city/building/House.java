package game.city.building;

import game.city.person.Occupant;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

/**
 * A house.
 * 
 * @author sami
 * 
 */
public class House extends Building {
	
	/**
	 * Static array of all the houses created
	 */
	private static ArrayList<House> houses = new ArrayList<>(10);
	
	private ArrayList<Occupant> houseOccupants = new ArrayList<>(3);
	public Rectangle frame;
	
	/**
	 * Create a house.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public House(int ID, int positionX, int positionY, float width, float height, Integer money) {
		super(ID, positionX, positionY, width, height, money);
		
		this.frame = new Rectangle(positionX, positionY, width, height);
		
		// create HouseOccupant and pass reference to the building they are occupying (this) 
		Occupant occupant = new Occupant(this);
		
		// add the occupant to the observers of this class
		// when the house starts getting robbed, the observers will be notified (code in Building class)
		addObserver(occupant);
		
		// add the occupant to the house occupants array 
		houseOccupants.add(occupant);
		
		// add this house to the static array list containing all the houses
		houses.add(this);
	}
	
	public static ArrayList<House> getHouses() {
		return houses;
	}


}
