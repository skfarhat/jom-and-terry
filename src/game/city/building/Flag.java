package game.city.building;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;

/**
 * Marker flags
 * 
 * @author michael
 */
public class Flag implements Renderable {

	/**
	 * Flag color
	 */
	enum FlagType {
		NONE, RED, DARK_BLUE, VIOLET, GREEN
	};

	// Static fields
	static private String FLAG_IMAGE_PATH_RED = "res/Flags/red.png";
	static private String FLAG_IMAGE_PATH_GREEN = "res/Flags/green.png";
	static private String FLAG_IMAGE_PATH_DARK_BLUE = "res/Flags/dark-blue.png";
	static private String FLAG_IMAGE_PATH_VIOLET = "res/Flags/violet.png";

	// static private int FLAGS_COUNT = 4;

	static private Image redFlag;
	static private Image greenFlag;
	static private Image violetFlag;
	static private Image darkBlueFlag;

	/**
	 * Initialize Images from files
	 */
	static {
		try {
			redFlag = new Image(FLAG_IMAGE_PATH_RED);
			greenFlag = new Image(FLAG_IMAGE_PATH_GREEN);
			darkBlueFlag = new Image(FLAG_IMAGE_PATH_DARK_BLUE);
			violetFlag = new Image(FLAG_IMAGE_PATH_VIOLET);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	// Fields
	public int flagID = 0;
	public FlagType flagType;
	protected Image image;

	/**
	 * default CONSTRUCTOR
	 */
	public Flag() {
		this.flagType = FlagType.NONE;
		this.image = null;
	}

	public Flag(int flagID) {

		// set the flag id one less, then call nextFlag()
		this.flagID = flagID - 1;

		// this will increment the flagID back
		nextFlag();
	}

	@Override
	public void draw(float x, float y) {
		// draw image
		image.draw(x, y);
	}

	/**
	 * Toggles flag color
	 */
	protected void nextFlag() {
		flagID++;
		switch (flagID) {
		case 0:
			this.image = null;
			this.flagType = FlagType.NONE;
			break;
		case 1:
			this.image = redFlag;
			this.flagType = FlagType.RED;
			break;
		case 2:
			this.image = greenFlag;
			this.flagType = FlagType.GREEN;
			break;
		case 3:
			this.image = darkBlueFlag;
			this.flagType = FlagType.DARK_BLUE;
			break;
		case 4:
			this.image = violetFlag;
			this.flagType = FlagType.VIOLET;
			break;
		default:
			this.flagID = 0;
			this.image = null;
			this.flagType = FlagType.NONE;
			break;
		}
	}
}
