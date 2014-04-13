package game.city.building;

import game.Globals;
import game.city.person.SecurityGuard;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

@SuppressWarnings("unchecked")

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
	public Bank(int ID, Point position, float width, float height, Integer money) throws SlickException {
		super(ID, position, width, height, money);

		this.securityGuards = new ArrayList<>();
		this.position = position; 
		
		// Create the Security Guards
		for (int i = 0; i < numberOfSecurityGuards; i++) {
			// create a new security guard
			SecurityGuard sg = new SecurityGuard(position, "SG",
					Globals.SECURITY_GUARD_VELOCITY, this);

			// add the security guard to the array
			this.securityGuards.add(sg);
			
			// add the created bank to the static banks array
			banks.add(this);
		}
	}

	@Override
	public JSONObject save() {
		JSONObject obj = new JSONObject(); 
		obj.put(Globals.ID, this.ID);
		obj.put(Globals.TYPE, "Bank");
		obj.put(Globals.MONEY, this.money);
		obj.put(Globals.NUMBER_SECURITY_GUARDS, this.numberOfSecurityGuards);
		obj.put(Globals.ROBBED, getIsCompletelyRobbed());
		obj.put(Globals.FLAG, this.getFlag().flagID);
		
		return obj;
	}

	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;
		
		this.money = (int) map.get(Globals.MONEY); 
		this.numberOfSecurityGuards = (int) map.get(Globals.NUMBER_SECURITY_GUARDS);
		this.setCompletelyRobbed((Boolean) map.get(Globals.ROBBED));
		this.getFlag().flagID = (int) map.get(Globals.FLAG); 
		
	}

}