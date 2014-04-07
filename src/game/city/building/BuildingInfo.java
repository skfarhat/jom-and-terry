package game.city.building;

import game.Game;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Renderable;
import org.newdawn.slick.geom.Rectangle;

public class BuildingInfo implements Renderable{

	public static int BUILDING_INFO_HEIGHT = 50; 

	private Rectangle frame;  
	private Flag flag;

	/**
	 * Graphics instance of the container 
	 */
	private Graphics g = Game.getInstance().getContainer().getGraphics();

	/**
	 * Building for which the information is being displayed
	 */
	private Building bldg; 

	/**
	 * Filling bar that shows how far the building is in the process of being robbed
	 */
	private FillingBar fillingBar;

	// can be accessed through the building field but why add a layer of indirection when it won't change
	/**
	 * String containing the building type. Can access it through the building field
	 */
	private String buildingType = null; 


	/**
	 * 
	 * @param bldg Building for which the information is being displayed
	 */
	public BuildingInfo(Building bldg) {

		this.frame = new Rectangle(bldg.position.getX(), bldg.position.getY(), bldg.width, BUILDING_INFO_HEIGHT);

		// set the building field
		this.bldg = bldg; 

		// set the building type
		if (bldg instanceof House){
			this.buildingType = "House";
		}
		else if (bldg instanceof Shop){
			this.buildingType = "Shop";
		}
		else if (bldg instanceof Bank){
			this.buildingType = "Bank";
		}
		this.flag = new Flag(); 

		// initialize the filling bar 
		fillingBar = new FillingBar(frame.getX(), frame.getY(), frame.getWidth());
	}

	@Override
	public void draw(float x, float y) {
		// draw the filling bar
		fillingBar.draw(frame.getX(), frame.getY() - 50);

		// the type of the building
		g.drawString(buildingType, frame.getX(), frame.getY()-35);

		g.drawString(String.format("%d/%d",bldg.occupants.size()-bldg.occupantsOnVacation, bldg.occupants.size()), frame.getX(), frame.getY()-25);

		g.drawString(String.format("$%d", bldg.money), frame.getX(), frame.getY() -12);
		
		if (flag.image != null)
			flag.draw(frame.getX(), frame.getY()+25);
	}

	public FillingBar getFillingBar() {
		return fillingBar;
	}

	// FLAG
	// ==============================================================================================================================
	
	/**
	 * Add flag without a message
	 */
	protected void addFlag(){
		flag = new Flag(); 
	}
	/**
	 * Remove flag by setting its value to null
	 */
	protected void removeFlag(){
		flag = null; 
	}
	/**
	 * Change the message string of the flag
	 * @param message string
	 */
	protected void nextFlag(){
		flag.nextFlag();
	}
}
