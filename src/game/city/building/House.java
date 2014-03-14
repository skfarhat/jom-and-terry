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
		super(ID, positionX, positionY, width, height, money);
		
		this.frame = new Rectangle(positionX, positionY, width, height);
	}
}
