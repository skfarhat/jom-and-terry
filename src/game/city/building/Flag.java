package game.city.building;

import org.newdawn.slick.Image;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.SlickException;

public class Flag implements Renderable{
	
	// Static fields
	static private String FLAG_IMAGE_PATH_RED = "res/Flags/red.png";
	static private String FLAG_IMAGE_PATH_GREEN = "res/Flags/green.png";
	static private String FLAG_IMAGE_PATH_DARK_BLUE = "res/Flags/dark-blue.png";
	static private String FLAG_IMAGE_PATH_VIOLET = "res/Flags/violet.png";
	
//	static private int FLAGS_COUNT = 4;
	
	static private Image redFlag;
	static private Image greenFlag;
	static private Image violetFlag;
	static private Image darkBlueFlag;

 
	// Static block
	static{
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
	protected Image image; 

	/**
	 * default CONSTRUCTOR
	 */
	public Flag() {
		this.image = null;
	}

	@Override
	public void draw(float x, float y) {
		// draw image
		image.draw(x,y);
	}

	protected void nextFlag(){
		flagID++; 
		switch (flagID) {
		case 0: 
			this.image = null; 
			break;
		case 1:
			this.image = redFlag; 
			break;
		case 2:
			this.image = greenFlag;
			break;
		case 3:
			this.image = darkBlueFlag; 
			break;
		case 4:
			this.image = violetFlag;
			break;
		default:
			this.flagID = 0; 
			this.image = null; 
			break;
		} 
	}
}
