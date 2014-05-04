package game.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import game.Game;
import game.Globals;
import game.city.person.Person;
import game.city.person.Policeman;
import game.city.person.Robber;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Point;

public class PlayerDialog {
	private Graphics g = Game.getInstance().getContainer().getGraphics();
	private final String BACKGROUND_IMAGE_PATH= "res/PlayerDialog.png";
	private Point position; 
	private Person person;
	private Image background;

	private final int LOG_X = 0;
	private final int LOG_Y = 5;

	protected boolean isShowing = false; 
	protected String message ;

	Timer showTimer; 
	public PlayerDialog(Person person, Point position) {
		this.person = person; 
		this.position = position;

		// Background Image
		try { background = new Image(BACKGROUND_IMAGE_PATH);}
		catch (Exception exc) {exc.printStackTrace();}
	}

	public void draw(){ 
		if (isShowing && message !=null){
			if ((person instanceof Robber)){
				Robber robber = (Robber) person; 

				g.setColor(Color.white);
				g.drawImage(background, position.getX(), position.getY());
				drawString(g, message, (int) position.getX() + LOG_X, (int) position.getY()+ LOG_Y);
			}
			else {
				Policeman policeman = (Policeman) person; 

				g.setColor(Color.white);
				g.drawImage(background, position.getX(), position.getY());
				drawString(g, message, (int) position.getX() + LOG_X, (int) position.getY()+ LOG_Y);
			}
		}
	}
	public void drawString(Graphics g, String text, int x, int y) {
	    for (String line : text.split("\n"))
	        g.drawString(line, x, y += g.getFont().getLineHeight());
	}

	public void setMessage(String message) {
		this.message = message;
	}
	public void show(String message){
		this.message = message; 
		isShowing = true; 

		if (showTimer !=null)
			if (showTimer.isRunning())
				showTimer.stop();

		showTimer = new Timer(Globals.DIALOG_SHOW_TIME, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				isShowing = false;  
			}
		});
		showTimer.setRepeats(false);
		showTimer.start();
	}

}
