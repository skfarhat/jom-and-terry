package game.city.building;

import game.Globals;
import game.city.person.Occupant;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

@SuppressWarnings("unchecked")
/**
 * A House on the map
 * 
 * @author michael
 */
public class House extends Building {

	public Rectangle frame;

	/**
	 * Create a house.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public House(int ID, Area area, Point position, float width, float height,
			Integer money) {
		super(ID, area, position, width, height, money);

		this.frame = new Rectangle(position.getX(), position.getY(), width,
				height);

		// create HouseOccupant and pass reference to the building they are
		// occupying (this)
		Occupant occupant = new Occupant(this);

		// add the occupant to the observers of this class
		// when the house starts getting robbed, the observers will be notified
		// (code in Building class)
		addObserver(occupant);

		// add the occupant to the house occupants array
		occupants.add(occupant);

	}

	@Override
	public JSONObject save() {
		JSONObject obj = new JSONObject();
		obj.put(Globals.ID, this.ID);
		obj.put(Globals.TYPE, "House");
		obj.put(Globals.MONEY, this.money);
		obj.put(Globals.ROBBED, getIsCompletelyRobbed());
		obj.put(Globals.FLAG, this.getFlag().flagID);

		return obj;
	}

	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;

		int flagId = (int) map.get(Globals.FLAG);
		flag = new Flag(flagId);

		this.money = (int) map.get(Globals.MONEY);
		this.setCompletelyRobbed((Boolean) map.get(Globals.ROBBED));
	}

}
