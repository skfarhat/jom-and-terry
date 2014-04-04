package game.city.building;

import game.AudioGame;
import game.Game;
import game.city.person.Robber;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Timer;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Buildings abstract class.
 * 
 * @author sami
 * 
 */
public abstract class Building  {
	/**
	 * The amount of money a building has.
	 */

	public static ArrayList<Building> buildings = new ArrayList<>(20); 
	
	//==============================================================================================================================
	// ROBBING
	// FIXME: ROBBING DURATION modify for each building
	private final static int ROBBING_DURATION = 3000;
	private final static int ROBBING_UPDATE = 100;
	private Timer robbingTimer = null;

	private boolean isBeingRobbed;
	private boolean isCompletelyRobbed = false; 
	private float robbedPercent = 0.0f; 
	private FillingBar fillingBar; 
	//==============================================================================================================================
	
	
	public Integer ID; 
	public Integer money;
	public int xPos, yPos; 
	public float width, height;
	public boolean isHighlighted;
	public Rectangle rect;

	/**
	 * Abstract constructor only called by subclasses.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param money
	 */
	public Building(int ID, int positionX,  int positionY, float width, float height, Integer money) {

		this.rect = new Rectangle(positionX, positionY, width, height);

		this.xPos = positionX; 
		this.yPos = positionY;
		this.width = width; 
		this.height = height; 
		this.money = money;

		// initially the building is not highlighted
		this.isHighlighted = false;
		

		// init the filling bar 
		fillingBar = new FillingBar(xPos, yPos-20, width);

		buildings.add(this);
	}

	/**
	 * Checks whether a robber is near
	 * @param robber
	 * @return
	 */

	/**
	 * Output the info of a building when the robber is near
	 */
	public void displayBuildingInfo() {
		
	}

	public void rob(final Robber robber){
		
		if (isBeingRobbed)
			return; 

		AudioGame.play("Glass-Smash.ogg");
		try {
			Audio oggStream = AudioLoader.getStreamingAudio("OGG", ResourceLoader.getResource("res/Sounds/Glass-Smash.ogg"));
			oggStream.playAsMusic(1.0f, 1.0f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		final Building thisBuilding = this; 
		final Building nearByBldg = robber.nearByBldg; 
		
		robbingTimer = new Timer(ROBBING_DURATION/ROBBING_UPDATE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (isCompletelyRobbed)
					return;  

				// the robber is in fact robbing this bank
				if (nearByBldg == thisBuilding){

					// add 10% to the robbed percent of the bank
					robbedPercent+= (1.0f/ROBBING_UPDATE);

					// update the filling bar of the building

					if (robbedPercent >= 1.0f){

						// robber finished robbing this bank
						robber.addMoney(thisBuilding.money);

						// set the money of the bank to zero
						thisBuilding.money = 0; 

						// set the boolean is completely robbed to avoid robber re-robbing
						isCompletelyRobbed = true; 
						
						// stop the robbing timer
						robbingTimer.stop();
					}
					fillingBar.update(robbedPercent);
				}
				else return; 
			}
		});

		// start the timer
		robbingTimer.start(); 
		isBeingRobbed = true; 
	}


	public void draw(){
		// Get Graphics Instance 
		Graphics g = Game.getInstance().getContainer().getGraphics();
		
		// Draw the filling bar at the xPos of the building but a bit above 
		fillingBar.draw(0,0);
				
		g.drawString(String.format("$%d", money), xPos, yPos-40);
	}
}