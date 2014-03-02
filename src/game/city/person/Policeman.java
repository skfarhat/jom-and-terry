package game.city.person;

/**
 * A policeman.
 * 
 * @author sami
 * 
 */
public class Policeman extends Person {
	/**
	 * Creates a policeman.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public Policeman(double positionX, double positionY, String name,
			double velocity) {
		super(positionX, positionY, name, velocity);
	}

	/**
	 * Warns the police if a robber is near.
	 * 
	 * @param robber
	 * @return
	 */
	public boolean warnPolice(Robber robber) {
//		return (calculateDistance(robber) < 40.0);
		return false; 
	}

	/**
	 * Attempt to arrest a robber.
	 * 
	 * @param robber
	 * @return whether the arrest was successful.
	 */
	//TODO FIX
	public boolean arrestRobber(Robber robber) {
		// TODO implement
		return true;
	}
}