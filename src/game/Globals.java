package game;

import java.util.Random;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public final class Globals {

	public static final Random random = new Random(System.currentTimeMillis());

	public static final String gamename = "Jom & Terry";

	public static final int MAIN_MENU = 0;
	public static final int PLAY = 1;
	public static final int PAUSE = 3;
	public static final int GAME_OVER = 4;
	public static final int LOADING = 5;
	public static final int AREA_PICK = 6;
	public static final int PLAYER_PICK = 7;
	public static final int ACCOUNT_PICK = 8;
	public static final int NEW_ACCOUNT_PICK = 9;
	public static final int STATS_VIEW = 10;

	public static final int APP_WIDTH = 1400;
	public static final int APP_HEIGHT = 800;
	public static final int TILE_SIZE = 16;

	// Directories
	public static final String SAVE_DIRECTORY_PATH = "Save/";

	// Robber
	public static final float ROBBER_VISION_DISTANCE = 500.0f;
	public static final int ROBBER_ROBBING_INTERVAL = 4000; // 4 seconds
	public static final float ROBBER_FLEE_DISTANCE = 200.0f; // 4 seconds

	// Velocity
	public static final float VELOCITY_MULTIPLIER = 0.02f;
	public static final float ROBBER_VELOCITY = 150.0f;
	public static final float POLICEMAN_VELOCITY = 115.0f;
	public static final float SECURITY_GUARD_VELOCITY = 80.0f;

	// Building
	public static final int BUILDING_INFO_DISPLAY_TIMER = 3000;
	public static final float BUILDING_ROBBING_DISTANCE = 35.0f;

	// Font
	public static final String PETITINHO_FONT = "Petitinho.ttf";

	// Save Accounts
	public static final String ROBBER_SCORE = "Score";
	public static final String ROBBER_MONEY = "Money";
	public static final String ROBBER_POSITION_X = "Position_X";
	public static final String ROBBER_POSITION_Y = "Position_Y";

	// Police Office
	public static final int POLICE_OFFICE_GATHER_COUNT = 3;

	// For policeman

	// for when the user is policeman and wants to select other policemen using
	// the mouse
	public static final float SELECTION_ERROR = 50.0f;
	public static final float GATHER_RADIUS = 25.0f;
	public static final float ARREST_DISTANCE = 50.0f;
	public static final float GATHER_SELECTION_ERROR = 30.0f;

	// Security Guards
	public static float SECURITY_GUARD_ARREST_DISTANCE = 25.0f;
	public static float SECURITY_GUARD_VISION_DISTANCE = 180.0f;
	public static float SECURITY_GUARD_CHASE_DISTANCE = 100.0f; // distance for
																// which the
																// security
																// guard starts
																// following the
																// robber
	public static float SECURITY_GUARD_MAX_DISTANCE_FROM_BLDG = 300.0f; // maximum
																		// distance
																		// the
																		// security
																		// guard
																		// takes
																		// from
																		// his
																		// guarded
																		// bldg

	// Camera
	public static final float CAMERA_SCROLL_SPEED = 2.0f;

	// PlayerDialog
	public static final int DIALOG_SHOW_TIME = 5000; // 5sec

	// Whistle
	public static final int WHISTLE_DURATION = 500; // 4 sec
	public static final float WHISTLE_HEAR_DISTANCE = 600.0f; // distance needed
																// to hear the
																// whistle in
																// pixels

	public static final float POLICEMAN_VISION_DISTANCE = 200.0f;
	public static final String POLICEMAN_SCORE = "Score";
	public static final String POLICEMAN_POSITION_X = "Position_X";
	public static final String POLICEMAN_POSITION_Y = "Position_Y";

	public static final String TYPE = "type";
	public static final String HOUSE = "House";
	public static final String SHOP = "Shop";
	public static final String BANK = "Bank";
	public static final String AREA = "Area";
	public static final String BUILDINGS = "Buildings";

	public static final String ROBBER = "Robber";
	public static final String POLICE_OFFICE = "PoliceOffice";
	public static final String POLICEMEN = "Policemen";
	public static final String COUNT = "count";

	public static final String RESUME = "Resume";
	public static final String ID = "id";
	public static final String MONEY = "money";
	public static final String NB_SECURITY_GUARDS = "nbSecurityGuards";
	public static final String ROBBED = "Robbed";
	public static final String FLAG = "Flag";
	public static final String TIME = "Time";
	public static final String LEVEL = "Level";
	public static final String GATHERS_REMAINING = "Gathers_Remaining";
	public static final String WIN = "Win";
	public static final String SCORE = "Score";

	// MAP INDICES
	public static final int HOUSE_OBJECT_INDEX = 0;
	public static final int BANK_OBJECT_INDEX = 1;
	public static final int SHOP_OBJECT_INDEX = 2;
	public static final int HIGHWAY_OBJECT_INDEX = 3;
	public static final int GATES_OBJECT_INDEX = 4;
	public static final int ROAD_OBJECT_INDEX = 5;
	public static final int ROBBER_COLL_OBJECT_INDEX = 6;
	public static final int POLICE_COLL_OBJECT_INDEX = 7;

	public static final int CITY_COUNT = 5;

	// City
	public static final String CITY_1 = "res/city/City1.tmx";
	public static final String CITY_2 = "res/city/City2.tmx";
	public static final String CITY_3 = "res/city/City3.tmx";
	public static final String CITY_4 = "res/city/City4.tmx";
	public static final String CITY_5 = "res/city/City5.tmx";

	public final static String cityTMXPaths[] = { CITY_1, CITY_2, CITY_3,
			CITY_4, CITY_5 };

	// Global functions
	public static final float distance(Point pnt1, Point pnt2) {
		float distance = (float) Math.sqrt(Math.pow(pnt1.getX() - pnt2.getX(),
				2.0) + Math.pow(pnt1.getY() - pnt2.getY(), 2.0));
		return distance;
	}

	/**
	 * 
	 * @param rect
	 * @param x
	 * @param y
	 * @param error
	 * @return
	 */
	public static final boolean rectContainsPoint(Rectangle rect, float x,
			float y, float error) {

		// small rectangle created the test collision
		Rectangle tempRect = new Rectangle(x - error / 2, y - error / 2, error,
				error);

		return tempRect.intersects(rect);
	}

}
