package game.city.person;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.Timer;

import game.city.building.Building;

public class Occupant implements Observer{
	
	// 5-15 sec
	private static int VACATION_TIME_INITIAL_DELAY		= 5000;
	private static int VACATION_TIME_INTERVAL 	 		= 15000;
		
	private Building building;
	
	private boolean isOnVacation = false;
	
	private Timer vacationTimer; 

	/**
	 * When creating a HouseOccupant associate it with a House
	 * @param bldg
	 */
	public Occupant(Building bldg) {
		this.building = bldg;
		
		// set on Vacation
		// At the beginning 30% are on vacation
		Random rand = new Random(); 

		// initially before starting the timer
		if (rand.nextInt(10) <= 3)
			goingOnVacation();
		
		
		vacationTimer = new Timer(VACATION_TIME_INTERVAL, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				// if he is on vacation let him come back and vice versa 
				
				if (isOnVacation)
					returningFromVacation();
				else
					goingOnVacation();
				
			}
		});
		vacationTimer.setInitialDelay(VACATION_TIME_INITIAL_DELAY);

		vacationTimer.start(); 
	}

	public void callPolice() {
		// call Police
		PoliceOffice.callPolice(building);

	}

	/**
	 * Method that sends the occupant on vacation, thus vacating his house
	 */
	public void goingOnVacation(){
		isOnVacation = true; 
		building.occupantsOnVacation++; 
	}
	public void returningFromVacation(){
		isOnVacation = false; 
		building.occupantsOnVacation--; 
	}

	@Override
	public void update(Observable o, Object arg) {
		
		// call the police only if he is not on vacation
		if (!isOnVacation)
			callPolice();
	}
}
