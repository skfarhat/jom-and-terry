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
	public Shop(double positionX, double positionY, Integer money,
			ArrayList<SecurityGuard> securityGuards) {
		super(positionX, positionY, money);
		this.securityGuards = securityGuards;
	}
}