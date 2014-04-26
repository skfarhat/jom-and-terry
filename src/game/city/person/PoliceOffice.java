package game.city.person;

import game.Globals;
import game.city.Camera;
import game.city.building.Building;
import game.city.road.Highway;
import game.menu.PlayerLog;
import game.states.Play;
import game.states.Savable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

@SuppressWarnings("unchecked")

public class PoliceOffice implements Savable{

	private static final String EMERGENCY_SOUND = "res/Sounds/Emergency.ogg" ;

	private static final Integer numberOfPolicemen = 3; 
	public static ArrayList<Policeman> policeForceArray = new ArrayList<>(numberOfPolicemen);
	private static Audio sound;
	private static boolean isPlayingSound = false; 
	private static boolean userIsPolice; 

	protected static int gatherCount = 0; 
	public static Integer robberVisibleCount = 0; 

	private Robber robber; 
	public PoliceOffice(Robber robber, boolean userIsPolice) throws SlickException {

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
		for (Policeman policeman : policeForceArray){
			policeman.draw();
		}
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

	public static void callPolice(Highway highway){ 		

		if (!userIsPolice){
			for (Policeman police: policeForceArray){	

				((PolicemanComputer) police).checkoutHighway(highway);
			}
		}

		else {
			// notify on the log
			String message = String.format("Passing by highway!");
			PlayerLog.setLogText(message);
		}

		playSound();
	}

	public void gatherAll(Point position){
		final float regionRadius = 25.0f; 
		for (Policeman police: policeForceArray){	
			((PolicemanUser) police).gather(new Circle(position.getX(), position.getY(), regionRadius));
		}
		gatherCount++;
			
		if (gatherCount==3){
			
			// TODO: Make clearer and comment
			new Thread((new Runnable() {
				@Override
				public void run() {
					boolean notArrived = false;
					do{
						notArrived = false; 
						for (Policeman police: policeForceArray){	
							notArrived = notArrived || police.isMoving;
						}
					}
					while(notArrived);
					
					
					if (!robber.isCaught){
						// you lose
						Play.getInstance().gameOver(false);
					}
					
				}
			})).start();;
		}
		
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

		// TODO: Remove this from here
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			if (userIsPolice){

				int destX = input.getMouseX();
				int destY = input.getMouseY();


				// get the building
				Point pos = new Point(destX, destY);

				Building bldg = selectBuilding(pos);

				// display info for this building
				if (bldg != null) 
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
		else if (input.isKeyPressed(Input.KEY_G)) {
			if (userIsPolice){
				Camera camera = Play.getInstance().getCamera();

				float x = input.getMouseX() + camera.getCameraX();
				float y = input.getMouseY() + camera.getCameraY(); 

				gatherAll(new Point(x,y));
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
			if (bldg.getRect().intersects(rect))
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


	@Override
	public JSONObject save() {

		JSONObject policemenObj = new JSONObject(); 

		for (Policeman policeman: PoliceOffice.policeForceArray)
		{
			JSONObject policemanObj= policeman.save();
			policemenObj.put(policeman.ID, policemanObj);
		}

		JSONObject policeOfficeObj = new JSONObject();

		// save all the policemen
		policeOfficeObj.put(Globals.POLICEMEN, policemenObj);

		// save the number of policemen
		policeOfficeObj.put(Globals.COUNT, policeForceArray.size());

		return policeOfficeObj;
	}

	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object> ) loadObj;


		// get the Policemen map <ID, PolicemanObj>
		HashMap<Object, Object> policemenMap = (HashMap<Object, Object>) map.get(Globals.POLICEMEN);
		System.out.println("PolicemenMap: " + policemenMap);

		for (Policeman policeman : PoliceOffice.policeForceArray){

			// get the policeman map 
			HashMap<Object, Object> policemanMap = (HashMap<Object, Object>) policemenMap.get("" + policeman.ID);

			// load it into the policeman
			policeman.load(policemanMap);

		}
	}

}
