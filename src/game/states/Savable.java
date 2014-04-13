package game.states;

import org.json.simple.JSONObject;

public interface Savable {

	public JSONObject save();
	public void load(Object loadObj);
	
}
