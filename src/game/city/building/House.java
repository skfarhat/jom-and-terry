package game.city.building;

import java.util.ArrayList;

import org.newdawn.slick.geom.Rectangle;

/**
 * A house.
 * 
 * @author sami
 * 
 */
public class House extends Building {
	
	private static ArrayList<House> houses = new ArrayList<>(10);
	
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
		
		houses.add(this);
	}
	
	public static ArrayList<House> getHouses() {
		return houses;
	}
}
