package game.city.building;

import game.city.person.SecurityGuard;

import java.util.ArrayList;

/**
 * A bank.
 * 
 * @author sami
 * 
 */
public class Bank extends Building {
	/**
	 * Security guards in this bank.
	 */
	public ArrayList<SecurityGuard> securityGuards;

	/**
	 * Creates a bank.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 * @param securityGuards
	 */
	public Bank(double positionX, double positionY, Integer money,
			ArrayList<SecurityGuard> securityGuards) {
		super(positionX, positionY, money);
		this.securityGuards = securityGuards;
	}
}