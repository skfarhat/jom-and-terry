package game.city.building;

import game.city.person.Policeman;
import game.city.person.Robber;
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
	private Audio sound;
	private boolean isPlayingSound = false; 


	public PoliceOffice(Play play, Robber robber) throws SlickException {

		// set robber
		this.robber = robber;

		// init the Police Force Array
		policeForceArray = new ArrayList<>(numberOfPolicemen);


		for (int i=0; i< numberOfPolicemen; i++) {
			int x = 400, y = 500;

			Policeman police = new Policeman(play, this.robber, x, y, "Police-1", 80.0f);

			policeForceArray.add(police);
		}

		try {
			this.sound = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource(EMERGENCY_SOUND));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param center
	 * @param error
	 */
	public void callPolice(Point center, float error){

		Circle region;
		for (Policeman police: policeForceArray){	
			// set the suspected region for the robber 
			// it is a circle
			region= new Circle(center.getCenterX(), center.getCenterY(), error);
			police.checkoutRegion(region);
		}
		playSound();
	}
	/**
	 * Draw all the policemen
	 */
	public void draw(){ 
		for (Policeman police : this.policeForceArray)
			police.draw();
	}
	/**
	 * Getter function for PoliceForce ArrayList
	 * @return ArrayList<Policeman> 
	 */
	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
	}


	private void playSound(){
		if (!isPlayingSound)
		{
			this.sound.playAsSoundEffect(1.0f, 1.0f, false);
			new javax.swing.Timer(3000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					isPlayingSound = false; 
				}
			}).start();
		}

		isPlayingSound = true; 


	}
}
