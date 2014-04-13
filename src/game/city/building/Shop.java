package game.city.building;

import game.Globals;
import game.city.person.SecurityGuard;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.newdawn.slick.geom.Point;

@SuppressWarnings("unchecked")

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

	@Override
	public JSONObject save() {
		JSONObject obj = new JSONObject(); 
		obj.put(Globals.ID, this.ID);
		obj.put(Globals.MONEY, this.money);
		obj.put(Globals.ROBBED, getIsCompletelyRobbed());
		obj.put(Globals.FLAG, this.getFlag().flagID);
		obj.put(Globals.TYPE, "Shop");

		return obj;	
		
	}

	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;
		
		this.money = (int) map.get(Globals.MONEY); 
		this.setCompletelyRobbed((Boolean) map.get(Globals.ROBBED));
		this.getFlag().flagID = (int) map.get(Globals.FLAG); 
				
	}

}