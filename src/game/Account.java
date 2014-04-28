package game;

import game.states.Play;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
/**
 * @author Apple
 * 
 */
public class Account {

	// Constants 
	public static final String RESUME_GAME				= "resumeGame";
	public static final String HIGHSCORE 				= "highscore"; 
	public static final String LEVEL_REACHED			= "highestLevelReached"; 
	public static final String USERNAME 				= "username"; 
	public static final String TIME_PLAYING				= "timePlaying";
	public static final String SCORES					= "scores";
	public static final String IS_ROBBER				= "isRobber";
	public static final String PAST_SCORES				= "pastScores";
	public static final String SAVE_DIRECTORY_PATH		= "Save/";

	// Class Fields
	private String username;
	private Integer highscore = 0;
	private Integer highestLevelReached = 1; 
	private Integer timePlaying = 0; 
	private Boolean isRobber = true;
	private ArrayList<HashMap<Date, Integer>> pastScores = null; 
	private HashMap<Object, Object> resumeGame = null; 


	// Constructor
	public Account() {
	}

	public Account(String username, boolean isRobber) {
		this.username = username; 
		this.pastScores = new ArrayList<>();
		this.isRobber = isRobber;
	}

	public boolean create() {
		try{
			File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");

			// if the account already exists 
			if (!accountFile.createNewFile())
				return false; 

			HashMap<Date, Integer> pastScoresMap = new HashMap<>();
			pastScores.add(pastScoresMap);

			HashMap<String, Object> map = new HashMap<>();
			map.put(HIGHSCORE, highscore.toString());
			map.put(LEVEL_REACHED, highestLevelReached.toString());
			map.put(TIME_PLAYING, timePlaying.toString());
			map.put(IS_ROBBER, isRobber.toString());
			map.put(USERNAME, username);
			map.put(PAST_SCORES, pastScores);

			ObjectMapper mapper = new ObjectMapper(); 

			mapper.writeValue(accountFile, map);

			return true; 
		}
		catch (Exception exc){
			exc.printStackTrace();
			return false; 
		}
	}

	public void save(){
		try {

			final File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");
			ObjectMapper mapper = new ObjectMapper(); 
			JSONObject mainObj = new JSONObject(); 

			JSONObject resumeObj = Play.getInstance().save();
			mainObj.put(HIGHSCORE, highscore.toString());
			mainObj.put(LEVEL_REACHED, highestLevelReached.toString());
			mainObj.put(TIME_PLAYING, timePlaying.toString());
			mainObj.put(IS_ROBBER, isRobber.toString());
			mainObj.put(USERNAME, username);
			mainObj.put(PAST_SCORES, pastScores);
			mainObj.put(RESUME_GAME, resumeObj);		// Resume 

			mapper.writeValue(accountFile, mainObj);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}

	}

	public static Account load(String username) {

		try{
			ObjectMapper mapper = new ObjectMapper();
			File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");

			// read JSON from a file 
			Account account = mapper.readValue(accountFile, Account.class);

			if (account == null){
				return null; 
			}
			else{				
				return account; 
			}
		}
		catch (Exception exc){
			exc.printStackTrace();
			return null; 
		}
	}

	// Getters
	public String getUsername() {
		return username;
	}
	public Integer getHighscore() {
		return highscore;
	}
	public Integer getHighestLevelReached() {
		return highestLevelReached;
	}
	public Integer getTimePlaying() {
		return timePlaying;
	}
	public Boolean getIsRobber() {
		return isRobber;
	}
	public ArrayList<HashMap<Date, Integer>> getPastScores() {
		return pastScores;
	}
	public HashMap<Object, Object> getResumeGame() {
		return resumeGame;
	}

	
	public void setHighestLevelReached(Integer highestLevelReached) {
		if (highestLevelReached > this.highestLevelReached)
			this.highestLevelReached = highestLevelReached;			
	}
	public void setIsRobber(Boolean isRobber) {
		this.isRobber = isRobber;
	}

	/**
	 * Override toString() method to print the filed attributes of the Account class
	 */
	@Override
	public String toString() {
		String str = String.format("Username:%s\nHighscore:%d\nHighestLevelReached:%d\nTimePlaying:%d\nIsRobber:%b",
				username, highscore, highestLevelReached, timePlaying, isRobber);
		return str; 
	}
}

