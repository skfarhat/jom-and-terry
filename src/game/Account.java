package game;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Apple
 * 
 */
public class Account {

	// Constants 
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
	private Integer highestLevelReached = 0; 
	private Integer timePlaying = 0; 
	private Boolean isRobber = true;
	private ArrayList<HashMap<Date, Integer>> pastScores = null; 

	// Constructor
	public Account() {
	}
	
	public Account(String username) {
		System.out.println("Account-constructor");
		this.username = username; 
		this.pastScores = new ArrayList<>(); 
	}

	public boolean create() {
		System.out.print("Create Account");
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

	public static Account load(String username) {

		try{
			ObjectMapper mapper = new ObjectMapper();
			File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");

			// read JSON from a file 
			Account account = mapper.readValue(accountFile, Account.class);
		
			if (account == null){
				System.out.println("account is null");
				return null; 
			}
			else{
				System.out.println("Loaded, the username is :" + account.getUsername());
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

}

