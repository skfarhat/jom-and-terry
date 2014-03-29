package game;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Apple
 * 
 */
public class Account {


	// Constants 
	public static final String HIGHSCORE 				= "highscore"; 
	public static final String LEVEL_REACHED			= "LReached"; 
	public static final String USERNAME 				= "username"; 
	public static final String TIME_PLAYING				= "TimePlaying";
	public static final String SCORES					= "Scores";
	public static final String IS_ROBBER				= "isRobber";
	public static final String PAST_SCORES				= "PastScores";
	public static final String SAVE_DIRECTORY_PATH		= "Save/";


	// Class Fields
	private String username;
	private Integer highscore = 0;
	private Integer highestLevelReached = 0; 
	private Integer timeSpentPlaying= 0;
	private ArrayList<HashMap<Date, Integer>> pastScores; 
	boolean isRobber;

	// JSON file to save the data to 
	File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");

	/**
	 * Method used to save game to a json file with path $DIRECTORY_PATH/$USERNAME
	 * Data saved includes highscore, time spent playing the game, the highest level reached... 
	 * 
	 * @throws Exception
	 */
	public void save(Integer newScore) throws Exception{

		// path 
		File directory = new File("Save");
		
		// if first time playing 
		// create directory and save
		if (directory.createNewFile())
		{	
			HashMap<Date, Integer> pastScoresMap = new HashMap<>();
			pastScoresMap.put(new Date(), newScore);					// put the new score
			pastScores.add(pastScoresMap);
			
			HashMap<String, Object> map = new HashMap<>();
			map.put(HIGHSCORE, highscore.toString());
			map.put(LEVEL_REACHED, highestLevelReached.toString());
			map.put(TIME_PLAYING, timeSpentPlaying.toString());
			map.put(USERNAME, username);
			map.put(IS_ROBBER, isRobber + "");
			map.put(PAST_SCORES, pastScores);
			
			JSONObject jsonObj = new JSONObject(map);

			FileWriter writer = new FileWriter(accountFile);
			jsonObj.writeJSONString(writer);  

		}
		else {
			// directory already exists 

			// get data + modify what is necessary
			
			// save

		}
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public JSONObject load() throws Exception {

		JSONParser parser = new JSONParser();

		FileReader reader = new FileReader(accountFile); 

		JSONObject obj = (JSONObject) parser.parse(reader);
		
		return obj; 
	}

}

