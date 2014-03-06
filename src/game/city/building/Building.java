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
	
	public Integer ID; 
	public Integer money;
	public int xPos, yPos; 
	public float width, height;
	public boolean isHighlighted;
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

		// initially the building is not highlighted
		this.isHighlighted = false;
		
	}

	/**
	 * Checks whether a robber is near
	 * 
	 * @param robber
	 * @return
	 */
	
	//TODO: FIX
	public boolean robberNearBuilding(Robber robber) {
		return false;
	}

	/**
	 * Output the info of a building when the robber is near
	 */
	public void displayBuildingInfo(Person person) {
		
	}
}