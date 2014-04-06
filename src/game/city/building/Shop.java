package game.city.building;

import game.city.person.SecurityGuard;

import java.util.ArrayList;

import org.newdawn.slick.geom.Point;

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
	public Shop(int ID, Point position, float width, float height, Integer money) {
		super(ID, position, width, height, money);
	}

}