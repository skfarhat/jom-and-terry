 package game.city.building;

import game.city.person.SecurityGuard;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

/**
 * A bank.
 * 
 * @author sami
 * 
 */
public class Bank extends Building {

	// Each bank has one security guard (for now...)
	int numberOfSecurityGuards = 1;

	public Rectangle frame;
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

		// TODO: make this frame in the super class
		this.frame = new Rectangle(positionX, positionY, width, height);

		this.securityGuards = new ArrayList<>();
		// Create the Security Guards
		for (int i = 0; i < numberOfSecurityGuards; i++) {
			// create a new security guard
			SecurityGuard sg = new SecurityGuard(this.xPos, this.yPos, "SG",
					30.0f, this);

			// add the security guard to the array
			this.securityGuards.add(sg);
		}
	}
}