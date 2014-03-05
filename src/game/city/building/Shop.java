package game.city.building;

import game.city.person.SecurityGuard;

import java.util.ArrayList;

/**
 * A shop.
 * 
 * @author sami
 * 
 */
public class Shop extends Building {
	/**
	 * Security guards in this shop.
	 */
	public ArrayList<SecurityGuard> securityGuards;

	/**
	 * Creates a shop.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 * @param securityGuards
	 */
	public Shop(int positionX, int positionY, float width, float height, Integer money, ArrayList<SecurityGuard> securityGuards) {
		super(positionX, positionY, width, height, money);
		this.securityGuards = securityGuards;
	}
}