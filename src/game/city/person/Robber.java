package game.city.person;

import java.util.ArrayList;

/**
 * A robber.
 * 
 * @author sami
 * 
 */
public class Robber extends Person {
	/**
	 * Creates a robber.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public Robber(double positionX, double positionY, String name,
			double velocity) {
		super(positionX, positionY, name, velocity);
	}

	/**
	 * Updates robber's location. Called when the robber moves.
	 * 
	 * @param newX
	 * @param newY
	 */
	public void locationChanged(double newX, double newY) {
//		this.positionX = newX;
//		this.positionY = newY;
		// TODO update robberNearBuilding and robberNearRoad for buildings and
		// roads

	}

	/**
	 * Warns robber if any policeman is nearby.
	 * 
	 * @param policemen
	 * @return
	 */
	public boolean warnRobber(ArrayList<Policeman> policemen) {
//		for (Policeman policeman : policemen) {
//			if (calculateDistance(policeman) < 40.0)
//				return true;
//		}
		return false; 
	}
}