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
	 * static ArrayList contains all the created shops
	 */
	public static ArrayList<Shop> shops = new ArrayList<>(10); 
	
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
	public Shop(int ID, int positionX, int positionY, float width, float height, Integer money) {
		super(ID, positionX, positionY, width, height, money);
	}

}