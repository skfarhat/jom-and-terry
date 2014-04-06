package game.city.building;

import game.Globals;
import game.city.person.Policeman;
import game.city.person.Robber;
import game.menu.PlayerLog;
import game.states.Play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class PoliceOffice {

	private static final String EMERGENCY_SOUND = "res/Sounds/Emergency.ogg" ;

	private Robber robber; 
	private static Integer numberOfPolicemen = 3; 
	public static ArrayList<Policeman> policeForceArray = new ArrayList<>(numberOfPolicemen);
	private static Audio sound;
	private static boolean isPlayingSound = false; 
	private static boolean userIsPolice; 

	public PoliceOffice(Play play, Robber robber, boolean userIsPolice) throws SlickException {

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
			Policeman police = new Policeman(play, this.robber, new Point(x,y) , "Police-1", Globals.POLICEMAN_VELOCITY,userIsPolice);
			
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
	/**
	 * Getter function for PoliceForce ArrayList
	 * @return ArrayList<Policeman> 
	 */
	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
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

	public static void callPolice(Building bldg){
		Point center = bldg.position; 		
		float error = 10.0f; 
		

		Circle region;
		for (Policeman police: policeForceArray){	
			// set the suspected region for the robber 
			// it is a circle
			region= new Circle(center.getCenterX(), center.getCenterY(), error);
			
			
			// if user is robber then get the police to check the region
			if (!userIsPolice)
				police.checkoutRegion(region);
			else
			{
				// notify on the log
				String message = String.format("%s being robbed!", bldg.getType());
				PlayerLog.setLogText(message);
				
			}
			
		}
		playSound();
		
		
	}
}
