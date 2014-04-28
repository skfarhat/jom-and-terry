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
	
	// Robber
	public static final float ROBBER_VISION_DISTANCE = 400.0f; 
	public static final float ARREST_DISTANCE = 50.0f;  

	// Velocity
	public static final float VELOCITY_MULTIPLIER = 0.02f; 
	public static final float ROBBER_VELOCITY = 130.0f;
	public static final float POLICEMAN_VELOCITY = 115.0f;
	public static final float SECURITY_GUARD_VELOCITY = 80.0f; 
	
	// Building
	public static final int BUILDING_INFO_DISPLAY_TIMER = 3000;
	public static final float BUILDING_ROBBING_DISTANCE = 30.0f;
	
	// Font
	public static final String PETITINHO_FONT = "Petitinho.ttf";
	
	// Save Accounts
	public static final String ROBBER_SCORE = "Score";
	public static final String ROBBER_MONEY = "Money";
	public static final String ROBBER_POSITION_X= "Position_X";
	public static final String ROBBER_POSITION_Y= "Position_Y";
	
	// For policeman
	// for when the user is policeman and wants to select other policemen using the mouse
	public static final float SELECTION_ERROR = 50.0f;		
	
	// Camera
	public static final float CAMERA_SCROLL_SPEED= 2.0f;
	
	
	public static final float POLICEMAN_VISION_DISTANCE = 100.0f; 
	public static final String POLICEMAN_SCORE = "Score";
	public static final String POLICEMAN_POSITION_X= "Position_X";
	public static final String POLICEMAN_POSITION_Y= "Position_Y";

	public static final String TYPE 		= "type";
	public static final String HOUSE 		= "House";
	public static final String SHOP 		= "Shop";
	public static final String BANK 		= "Bank";
	public static final String AREA			= "Area";
//	public static final String BUILDINGS 	= "Buildings";
	
	public static final String ROBBER 		= "Robber";
	public static final String POLICE_OFFICE= "PoliceOffice";
	public static final String POLICEMEN	= "Policemen";
	public static final String COUNT		= "count";
	
	public static final String RESUME 	= "Resume";
	public static final String ID 		= "id";
	public static final String MONEY 	= "money";
	public static final String NUMBER_SECURITY_GUARDS 	= "nbSecurityGuards";
	public static final String ROBBED = "Robbed";
	public static final String FLAG = "Flag";
	public static final String TIME = "Time";
	
	
	
	// MAP INDICES
	public static final int HOUSE_OBJECT_INDEX 		= 0; 
	public static final int BANK_OBJECT_INDEX 		= 1; 
	public static final int SHOP_OBJECT_INDEX 		= 2;
	public static final int HIGHWAY_OBJECT_INDEX 	= 3;
	public static final int GATES_OBJECT_INDEX 		= 4;
	public static final int ROAD_OBJECT_INDEX 		= 5;
	
	public static final int CITY_COUNT = 5; 
	
	// City
	public static final String  CITY_1 = 	"res/city/City1.tmx";
	public static final String  CITY_2 = 	"res/city/City2.tmx";
	public static final String  CITY_3 = 	"res/city/City3.tmx";
	public static final String  CITY_4 = 	"res/city/City4.tmx";
	public static final String  CITY_5 = 	"res/city/City5.tmx";
}
