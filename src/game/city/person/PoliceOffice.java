package game.city.person;

import game.Globals;
import game.city.Camera;
import game.city.building.Building;
import game.menu.PlayerLog;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class PoliceOffice {

	private static final String EMERGENCY_SOUND = "res/Sounds/Emergency.ogg" ;

	private Robber robber; 
	private static final Integer numberOfPolicemen = 3; 
	public static ArrayList<Policeman> policeForceArray = new ArrayList<>(numberOfPolicemen);
	private static Audio sound;
	private static boolean isPlayingSound = false; 
	private static boolean userIsPolice; 

	public PoliceOffice(Robber robber, boolean userIsPolice) throws SlickException {

		// set robber
		this.robber = robber;

		// Initialize the Police Force Array
		policeForceArray = new ArrayList<>(numberOfPolicemen);

		// set the boolean user is police
		PoliceOffice.userIsPolice = userIsPolice; 

		// Create Policemen 
		for (int i=0; i< numberOfPolicemen; i++) {

			// initial position of policemen
			int x = 400, y = 500;

			// Create Policeman
			Policeman police; 
			final String policeName = "Police-1";
			final Point position = new Point(x,y);

			if (userIsPolice)
				police = new PolicemanUser(robber, position, policeName, Globals.POLICEMAN_VELOCITY);
			else
				police = new PolicemanComputer(robber, position, policeName, Globals.POLICEMAN_VELOCITY);
			new Policeman(this.robber, position , "Police-1", Globals.POLICEMAN_VELOCITY);

			// Add Policeman to the Policeman
			policeForceArray.add(police);
		}

		// Initialize the Emergency calling sound
		try {
			sound = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(EMERGENCY_SOUND));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Draw all the policemen
	 */
	public void draw(){ 
		for (Policeman police : policeForceArray)
			police.draw();
	}

	public static void callPolice(Building bldg){
		Point center = bldg.position; 		
		float error = 10.0f; 


		Circle region;
		if (!userIsPolice){
			for (Policeman police: policeForceArray){	
				// set the suspected region for the robber 
				// it is a circle
				region= new Circle(center.getCenterX(), center.getCenterY(), error);

				((PolicemanComputer) police).checkoutRegion(region);
			}
		}

		else {
			// notify on the log
			String message = String.format("%s being robbed!", bldg.getType());
			PlayerLog.setLogText(message);
		}

		playSound();
	}

	public void stopPolicemenPatrols(){

		// stop the police patrols only if the user is the robber
		if (!userIsPolice)
			for (Policeman police: policeForceArray){
				((PolicemanComputer) police).stopPatrol();
			}
	}

	private static void playSound(){
		if (!isPlayingSound)
		{
			sound.playAsSoundEffect(1.0f, 1.0f, false);
			new javax.swing.Timer(3000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isPlayingSound = false; 
				}
			}).start();
		}

		isPlayingSound = true; 
	}

	public void processInput(Input input){
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (userIsPolice){

				int destX = input.getMouseX();
				int destY = input.getMouseY();


				// get the building
				Point pos = new Point(destX, destY);

				Building bldg = selectBuilding(pos);

				if (bldg==null)
					return; 

				// display info for this building
				bldg.setShowBuildingInfo(true);


				// save the previous policeman and deselect him later
				PolicemanUser prevPoliceman = (PolicemanUser) Play.getInstance().getMainCharacter();

				// get the policeman that is selected by the mouse
				PolicemanUser policeman = (PolicemanUser) selectPoliceman(destX, destY);

				// 
				if (policeman == null)
					return; 

				// set the isSelected to true
				policeman.setSelected(true);

				// Deselect the previous policeman
				prevPoliceman.setSelected(false); 

				// set the main character in the Play
				Play.getInstance().setMainCharacter(policeman);
			}
		}
	}

	private Building selectBuilding(Point pnt){
		Camera camera = Play.getInstance().getCamera();
		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + pnt.getX() - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + pnt.getY() - Globals.SELECTION_ERROR/2,
				Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);

		for (Building bldg: Building.buildings){
			if (bldg.rect.intersects(rect))
			{
				return bldg;  
			}
		}
		return null;
	}

	/**
	 * @param destX x-position of the mouse input
	 * @param destY y-position of the mouse input
	 * @return a policeman object if the destX and destY passed are close to a policeman in the map. Returns null otherwise
	 */
	private Policeman selectPoliceman(int destX, int destY){
		Camera camera = Play.getInstance().getCamera();

		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + destX - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + destY - Globals.SELECTION_ERROR/2,
				Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);

		for (Policeman policeman: getPoliceForceArray()){
			if (policeman.rect.intersects(rect))
			{
				return policeman;  
			}
		}
		return null;
	}

	// GETTERS/SETTERS
	// =============================================================================================================================
	/**
	 * Getter function for PoliceForce ArrayList
	 * @return ArrayList<Policeman> 
	 */
	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
	}

}
