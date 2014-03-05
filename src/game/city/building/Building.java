package game.city.building;

import game.city.person.Person;
import game.city.person.Robber;

/**
 * Buildings abstract class.
 * 
 * @author sami
 * 
 */
public abstract class Building  {
	/**
	 * The amount of money a building has.
	 */
	public Integer money;
	public int xPos, yPos; 
	public float width, height;
	
	/**
	 * Abstract constructor only called by subclasses.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public Building(int positionX, int positionY, float width, float height, Integer money) {
		
		this.xPos = positionX; 
		this.yPos = positionY;
		this.width = width; 
		this.height = height; 
		
		this.money = money;
		
		
	}

	/**
	 * Checks whether a robber is near
	 * 
	 * @param robber
	 * @return
	 */
	
	//TODO: FIX
	public boolean robberNearBuilding(Robber robber) {
//		return (calculateDistance(robber) < 40.0);
		return false;
	}

	/**
	 * Output the info of a building when the robber is near
	 */
	public void displayBuildingInfo(Person person) {
		// TODO complete implementation
//		if (person.getClass() == Robber.class) {
//			if (!robberNearBuilding((Robber) person))
//				return;
//		}
//		String info = String.format("Building position (%d,%d). Money:%d",
//				positionX, positionY, money);
//		System.out.println(info);
//	}
	}
}