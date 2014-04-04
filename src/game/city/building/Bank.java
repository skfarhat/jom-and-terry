package game.city.building;

import game.Globals;
import game.city.person.SecurityGuard;

import java.util.ArrayList;

import org.newdawn.slick.SlickException;

/**
 * A bank.
 * 
 * @author sami
 * 
 */
public class Bank extends Building {

	/**
	 * ArrayList that contains all the created banks
	 */
	public static ArrayList<Bank> banks = new ArrayList<>(10);
	
	/**
	 * The number of security guards this bank has
	 */
	int numberOfSecurityGuards = 1;

	/**
	 * Security guards in this bank.
	 */
	private ArrayList<SecurityGuard> securityGuards;


	/**
	 * Getter function for Security Guards
	 * @return
	 */
	public ArrayList<SecurityGuard> getSecurityGuards() {
		return securityGuards;
	}

	/**
	 * Creates a bank.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 * @throws SlickException
	 */
	public Bank(int ID, int positionX, int positionY, float width, float height, Integer money) throws SlickException {
		super(ID, positionX, positionY, width, height, money);

		this.securityGuards = new ArrayList<>();
		// Create the Security Guards
		for (int i = 0; i < numberOfSecurityGuards; i++) {
			// create a new security guard
			SecurityGuard sg = new SecurityGuard(this.xPos, this.yPos, "SG",
					Globals.SECURITY_GUARD_VELOCITY, this);

			// add the security guard to the array
			this.securityGuards.add(sg);
			
			// add the created bank to the static banks array
			banks.add(this);
		}
	}

}