package game;

import game.states.Play;
import game.states.Score;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("unchecked")
/**
 * @author Sami
 * 
 */
public class Account {

	// Constants 
	public static final String RESUME_GAME				= "resumeGame";
	public static final String HIGHSCORE 				= "highscore"; 
	public static final String LEVEL_REACHED			= "highestLevelReached"; 
	public static final String USERNAME 				= "username"; 
	public static final String TIME_PLAYING				= "timeSpentPlaying";
	public static final String SCORES					= "scores";
	public static final String IS_ROBBER				= "isRobber";
	public static final String PAST_SCORES				= "pastScores";
	public static final String SAVE_DIRECTORY_PATH		= "Save/";

	// Class Fields
	private String username;
	private Integer highscore = 0;
	private Integer highestLevelReached = 1; 
	private Integer timeSpentPlaying = 0; 
	private Boolean isRobber = true;
	private ArrayList<Score> pastScores = null; 
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

			HashMap<String, Object> map = new HashMap<>();
			map.put(HIGHSCORE, highscore.toString());
			map.put(LEVEL_REACHED, highestLevelReached.toString());
			map.put(TIME_PLAYING, timeSpentPlaying.toString());
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
	public void removeResumeGame(){
		try {

			final File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");
			ObjectMapper mapper = new ObjectMapper(); 
			JSONObject mainObj = new JSONObject(); 

			mainObj.put(HIGHSCORE, highscore.toString());
			mainObj.put(LEVEL_REACHED, highestLevelReached.toString());
			mainObj.put(TIME_PLAYING, timeSpentPlaying.toString());
			mainObj.put(IS_ROBBER, isRobber.toString());
			mainObj.put(USERNAME, username);
			mainObj.put(PAST_SCORES, pastScores); 

			mapper.writeValue(accountFile, mainObj);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}

	}

	public void save(Integer playTime){
		try {

			final File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");
			ObjectMapper mapper = new ObjectMapper(); 
			JSONObject mainObj = new JSONObject(); 

			timeSpentPlaying+= playTime;

			mainObj.put(HIGHSCORE, highscore.toString());
			mainObj.put(LEVEL_REACHED, highestLevelReached.toString());
			mainObj.put(TIME_PLAYING, timeSpentPlaying.toString());
			mainObj.put(IS_ROBBER, isRobber.toString());
			mainObj.put(USERNAME, username);
			mainObj.put(PAST_SCORES, pastScores);		 

			mapper.writeValue(accountFile, mainObj);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}

	}

	public void saveWithResumeGame(Integer playTime){
		final File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");
		ObjectMapper mapper = new ObjectMapper(); 
		JSONObject mainObj = new JSONObject(); 

		timeSpentPlaying+= playTime; 

		JSONObject resumeObj = Play.getInstance().save();
		mainObj.put(HIGHSCORE, highscore.toString());
		mainObj.put(LEVEL_REACHED, highestLevelReached.toString());
		mainObj.put(TIME_PLAYING, timeSpentPlaying);
		mainObj.put(IS_ROBBER, isRobber.toString());
		mainObj.put(USERNAME, username);
		mainObj.put(PAST_SCORES, pastScores);
		mainObj.put(RESUME_GAME, resumeObj);		 

		try {
			mapper.writeValue(accountFile, mainObj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveScore(int gameTime, int score, boolean youWin, boolean isRobber) {
		ArrayList<Score> pastScores = Game.getInstance().getAccount().getPastScores();

		if (score > highscore)
		{
			highscore = score; 
		}
		Score score1 = new Score(new Date(), score, youWin);
		pastScores.add(score1);
		save(gameTime); 
	}

	public static Account load(String username) {

		try{
			ObjectMapper mapper = new ObjectMapper();
			File accountFile = new File(SAVE_DIRECTORY_PATH + username + ".json");

			// read JSON from a file 
			Account account = mapper.readValue(accountFile, Account.class);

			return account;
		}
		catch (Exception exc){
			exc.printStackTrace();
			return null; 
		}
	}

	// Getters
	public Integer getTimeSpentPlaying() {
		return timeSpentPlaying;
	}
	public String getUsername() {
		return username;
	}
	public Integer getHighscore() {
		return highscore;
	}
	public Integer getHighestLevelReached() {
		return highestLevelReached;
	}
	public Boolean getIsRobber() {
		return isRobber;
	}
	public ArrayList<Score> getPastScores() {
		return pastScores;
	}
	public HashMap<Object, Object> getResumeGame() {
		return resumeGame;
	}
	public void setResumeGame(HashMap<Object, Object> resumeGame) {
		this.resumeGame = resumeGame;
	}
	public void setHighestLevelReached(Integer highestLevelReached) {
		if (highestLevelReached > this.highestLevelReached)
			this.highestLevelReached = highestLevelReached;			
	}
	public void setIsRobber(Boolean isRobber) {
		this.isRobber = isRobber;
	}
	public void setPastScores(ArrayList<Score> pastScores) {
		this.pastScores = pastScores;
	}
	public void setHighscore(Integer highscore) {
		this.highscore = highscore;
	}
	public void setTimeSpentPlaying(Integer timeSpentPlaying) {
		this.timeSpentPlaying = timeSpentPlaying;
	}
	/**
	 *  * Override toString() method to print the filed attributes of the Account class
	 */
	@Override
	public String toString() {
		String str = String.format("Username:%s\nHighscore:%d\nHighestLevelReached:%d\nTimePlaying:%d\nIsRobber:%b",
				username, highscore, highestLevelReached, timeSpentPlaying, isRobber);
		return str; 
	}
}

