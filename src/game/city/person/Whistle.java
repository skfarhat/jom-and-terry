package game.city.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import game.AudioGame;
import game.Globals;

import javax.swing.Timer;

import org.newdawn.slick.geom.Point;

public class Whistle {

	public boolean canBeHeard = true; 
	public Point position;
	
	public Whistle(Point pos) {
		this.position = pos; 
		AudioGame.playAsSound("whistle.ogg");
		
		Timer timer = new Timer(Globals.WHISTLE_DURATION, new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				// after some time the whistle can't be heard anymore
				canBeHeard = false; 
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

}
