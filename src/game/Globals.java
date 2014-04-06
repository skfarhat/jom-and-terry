package game;

public final class Globals {
	public static final String gamename = "Jom & Terry";
	public static final int MAIN_MENU = 0;
	public static final int PLAY = 1;
	public static final int PAUSE = 3;
	public static final int GAME_OVER = 4;
	public static final int LOADING = 5;
	public static final int AREA_PICK = 6;
	public static final int PLAYER_PICK= 7;
	public static final int ACCOUNT_PICK= 8;
	public static final int NEW_ACCOUNT_PICK= 9;
	public static final int STATS_VIEW = 10; 
	
	public static final int APP_WIDTH = 1400;
	public static final int APP_HEIGHT = 800;
	public static final int TILE_SIZE = 16;

	
	// Directories
	public static final String SAVE_DIRECTORY_PATH		= "Save/";
	
	// For policeman
	public static final float SELECTION_ERROR = 50.0f; 
	
	// Robber
	public static final float ROBBER_VISION_DISTANCE = 100.0f; 
	
	// Velocity
	
	public static final float VELOCITY_MULTIPLIER = 0.02f; 
	public static final float ROBBER_VELOCITY = 130.0f;
	public static final float POLICEMAN_VELOCITY = 80.0f;
	public static final float SECURITY_GUARD_VELOCITY = 60.0f; 
	
	// Building
	public static final int BUILDING_INFO_DISPLAY_TIMER = 3000;
	public static final float BUILDING_ROBBING_DISTANCE = 5.0f;
}
