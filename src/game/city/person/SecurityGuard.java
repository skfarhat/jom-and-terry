package game.city.person;

/**
 * A security guard.
 * 
 * @author sami
 * 
 */
public class SecurityGuard extends Person {
	/**
	 * Creates a security guard.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public SecurityGuard(double positionX, double positionY, String name,
			double velocity) {
		super(name, velocity);
	}

	/**
	 * Call police if a nearby robber is detected.
	 * 
	 * @return
	 */
	public boolean callPolice() {
		// TODO implement
		return false;
	}
}