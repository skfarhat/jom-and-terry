package game.city.building;

import org.newdawn.slick.geom.Rectangle;

/**
 * A house.
 * 
 * @author sami
 * 
 */
public class House extends Building {
	
	public Rectangle frame; 
	/**
	 * Create a house.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public House(int ID, int positionX, int positionY, float width, float height, Integer money) {
		super(positionX, positionY, width, height, money);
		
		// TODO: delegate this next statement to the constructor
		this.ID = ID; 
		this.frame = new Rectangle(positionX, positionY, width, height);
	}
}
