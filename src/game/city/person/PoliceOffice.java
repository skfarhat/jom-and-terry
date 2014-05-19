package game.city.person;

import game.AudioGame;
import game.Globals;
import game.city.Camera;
import game.city.building.Area;
import game.city.building.Building;
import game.city.road.Road;
import game.menu.PlayerLog;
import game.states.Play;
import game.states.Savable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.Timer;

import org.json.simple.JSONObject;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

@SuppressWarnings("unchecked")
/**
 * The collective pollice officers
 * 
 * @author Sami
 *
 */
public class PoliceOffice implements Savable {

	final Random rand = new Random(System.currentTimeMillis());

	private Integer numberOfPolicemen = 0;
	private Integer score = 0;
	public ArrayList<Policeman> policeForceArray;
	private boolean userIsPolice;

	private int policemanSelectionIndex = 0;
	protected int gathersRemaining = Globals.POLICE_OFFICE_GATHER_COUNT;
	public static Integer robberVisibleCount = 0;

	private boolean isGameOver = false;

	private Area area;
	private Robber robber;
	private Policeman selectedPoliceman;

	public PoliceOffice(Area area, Robber robber, boolean userIsPolice)
			throws SlickException {

		this.userIsPolice = userIsPolice;

		this.area = area;
		this.robber = robber;

		policeForceArray = new ArrayList<>(numberOfPolicemen);
		numberOfPolicemen = area.getBuildings().size() / 5; // 1 policeman for 5
		// buildings

		// initially the police has maximum score, but it decreases as Robber
		// robs buildings
		score = area.getTotalPossibleScore();

		// Create Policemen
		for (int i = 0; i < numberOfPolicemen; i++) {

			// random first position for the policeman
			final int x = rand.nextInt(800);
			final int y = rand.nextInt(800);

			final Point position = new Point(x, y);
			final String policeName = "Police-1";

			// Create Policeman
			Policeman police;

			if (userIsPolice)
				police = new PolicemanUser(area, robber, position, policeName,
						Globals.POLICEMAN_VELOCITY);
			else
				police = new PolicemanComputer(area, robber, position,
						policeName, Globals.POLICEMAN_VELOCITY);

			// Add Policeman to the array
			policeForceArray.add(police);
		}

		if (policeForceArray.size() > 0)
			selectedPoliceman = policeForceArray.get(0); // selected policeman
		// is the first on
		// the list

	}

	/**
	 * Draw all the policemen
	 */
	public void draw() {
		if (isGameOver) {
			Play.getInstance().gameOver(false);
			return;
		}

		for (Policeman policeman : policeForceArray) {
			policeman.draw();
		}
	}

	public void callPolice(Building bldg) {

		System.out.println("call police");
		AudioGame.playAsSound("Emergency.ogg");

		Point center = bldg.position;
		float error = 10.0f;

		Circle region;
		if (!userIsPolice) {
			for (Policeman police : policeForceArray) {
				// set the suspected region for the robber
				// it is a circle
				region = new Circle(center.getCenterX(), center.getCenterY(),
						error);

				((PolicemanComputer) police).checkoutRegion(region);
			}
		}

		else {
			// notify on the log
			String message = String
					.format("%s being \nrobbed!", bldg.getType());
			area.getCamera().getPlayerDialog().show(message);
		}

	}

	public void callPolice(Road road) {
		AudioGame.playAsSound("Emergency.ogg");

		if (!userIsPolice) {
			for (Policeman police : policeForceArray) {

				((PolicemanComputer) police).checkoutHighway(road);
			}
		}

		else {
			// notify on the log
			String message = String.format("Passing by highway!");
			PlayerLog.setLogText(message);
		}

	}

	public void gatherAll(Point position) {

		// gather all the policemen at the position
		for (Policeman police : policeForceArray) {
			((PolicemanUser) police).gather1(new Circle(position.getX(),
					position.getY(), Globals.GATHER_RADIUS));
		}

		gathersRemaining--;

		/*
		 * When the user uses the 'G' button 3 times gathering the police at one
		 * spot wait for all the policemen to arrive to the desired location and
		 * give them 3 seconds to capture the robber. If time is up, Game Over
		 * and the Police have lost
		 */
		if (gathersRemaining == 0) {
			/*
			 * Need a loop to wait for all the Policemen to arrive but can't
			 * have it blocking the main thread so we create a new one. Once all
			 * the policemen arrive, start a timer that ends the game with Game
			 * Over after 3 seconds
			 */
			Thread t = new Thread((new Runnable() {
				@Override
				public void run() {
					boolean notArrived = false;
					do {
						notArrived = false;
						for (Policeman police : policeForceArray) {
							notArrived = notArrived || police.isMoving;
						}
					} while (notArrived);

					if (!robber.isCaught) {
						// you lose after 3 sec
						Timer timer = new Timer(3000, new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								isGameOver = true;
							}
						});

						timer.setRepeats(false);
						timer.start();
					}
				}
			}));

			t.start();
		}
	}

	public void gameOver() {
		Play.getInstance().gameOver(false);
	}

	public void stopPolicemenPatrols() {

		// stop the police patrols only if the user is the robber
		if (!userIsPolice){
			System.out.println("Stopping policemen patrols");
			for (Policeman police : policeForceArray) {
				((PolicemanComputer) police).stopPatrol();
				((PolicemanComputer) police).stopMovingTimer();
			}
		}
	}

	public void processInput(Input input) {
		if (userIsPolice) {
			// TODO: Remove this from here
			if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {

				System.out.println("here in process input of police");
				int destX = input.getMouseX();
				int destY = input.getMouseY();

				// get the building
				Point pos = new Point(destX, destY);

				Building bldg = selectBuilding(pos);

				// display info for this building
				if (bldg != null)
					bldg.setShowBuildingInfo(true);

			} else if (input.isKeyPressed(Input.KEY_G)) {

				Camera camera = area.getCamera();

				// mouse coordinates
				final float x = input.getMouseX() + camera.getCameraX();
				final float y = input.getMouseY() + camera.getCameraY();

				// check if the mouse position intersects with any building
				boolean intersects = false;
				for (Building bldg : area.getBuildings())
					intersects = intersects
					|| Globals.rectContainsPoint(bldg.getRect(), x, y,
							Globals.GATHER_SELECTION_ERROR);

				// gather only if the mouse position does not intersect with any
				// building
				if (!intersects)
					gatherAll(new Point(x, y));
			} else if (input.isKeyPressed(Input.KEY_Q)) {

				// if there is only once policeman return
				if (getPoliceForceArray().size() <= 1)
					return;

				policemanSelectionIndex++;

				// if we exceeded the size of the array set it to 0
				if (policemanSelectionIndex >= getPoliceForceArray().size())
					policemanSelectionIndex = 0;

				Policeman newSelectedPoliceman = getPoliceForceArray().get(
						policemanSelectionIndex);

				/*
				 * The user can select policeman by mouse, or by iterating
				 * through them using Q. If the user changes the selected
				 * policeman by mouse, then it can happen that pressing Q will
				 * select the already selected Policeman. While this is not such
				 * a bad issue, we prefer to avoid it by incrementing
				 * policemanSelectionIndex twice in that specific case
				 */
				if (newSelectedPoliceman == selectedPoliceman)
					if (policemanSelectionIndex + 1 >= getPoliceForceArray()
					.size())
						policemanSelectionIndex = 0;
					else
						policemanSelectionIndex++;

				// get the new policeman to select
				newSelectedPoliceman = getPoliceForceArray().get(
						policemanSelectionIndex);

				// select the new policeman
				setSelectedPoliceman(newSelectedPoliceman);
				Play.getInstance().setMainCharacter();
			}
		}
	}

	private Building selectBuilding(Point pnt) {
		// create a rectangle and use the intersect method to check whether
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(area.getCamera().getCameraX()
				+ pnt.getX() - Globals.SELECTION_ERROR / 2, area.getCamera()
				.getCameraY() + pnt.getY() - Globals.SELECTION_ERROR / 2,
				Globals.SELECTION_ERROR, Globals.SELECTION_ERROR);

		for (Building bldg : area.getBuildings()) {
			if (bldg.getRect().intersects(rect)) {
				return bldg;
			}
		}
		return null;
	}

	// GETTERS/SETTERS
	// =============================================================================================================================
	/**
	 * Getter function for PoliceForce ArrayList
	 * 
	 * @return ArrayList<Policeman>
	 */
	public ArrayList<Policeman> getPoliceForceArray() {
		return policeForceArray;
	}

	public int getGathersRemaining() {
		return gathersRemaining;
	}

	public Policeman getSelectedPoliceman() {
		return selectedPoliceman;
	}

	public Integer getScore() {
		return score;
	}

	public void decreaseScoreBy(int score) {
		this.score -= score;
		if (this.score < 0)
			this.score = 0;
	}

	public boolean isUserIsPolice() {
		return userIsPolice;
	}

	// / TODO: could add Play.getinstance.setMainCharacter() at the end of it
	public void setSelectedPoliceman(Policeman selectedPoliceman) {

		// deselect the previously selected policemen
		((PolicemanUser) this.selectedPoliceman).setSelected(false);
		((PolicemanUser) this.selectedPoliceman).stop();

		// set the new selected policeman to the passed one
		this.selectedPoliceman = selectedPoliceman;

		// make the new policeman selected
		((PolicemanUser) this.selectedPoliceman).setSelected(true);
	}

	public void setArea(Area area) {
		for (Policeman policeman : policeForceArray) {
			policeman.setArea(area);
		}
		this.area = area;
	}

	@Override
	public JSONObject save() {

		JSONObject policemenObj = new JSONObject();

		for (Policeman policeman : policeForceArray) {
			JSONObject policemanObj = policeman.save();
			policemenObj.put(policeman.ID, policemanObj);
		}

		JSONObject policeOfficeObj = new JSONObject();

		// save all the policemen
		policeOfficeObj.put(Globals.POLICEMEN, policemenObj);

		// save the number of gathers remaining
		policeOfficeObj.put(Globals.GATHERS_REMAINING, gathersRemaining);

		// save the number of policemen
		policeOfficeObj.put(Globals.COUNT, policeForceArray.size());

		policeOfficeObj.put(Globals.SCORE, score);

		return policeOfficeObj;
	}

	@Override
	public void load(Object loadObj) {
		HashMap<Object, Object> map = (HashMap<Object, Object>) loadObj;

		// get the Policemen map <ID, PolicemanObj>
		HashMap<Object, Object> policemenMap = (HashMap<Object, Object>) map
				.get(Globals.POLICEMEN);

		gathersRemaining = (int) map.get(Globals.GATHERS_REMAINING);

		score = (int) map.get(Globals.SCORE);

		for (Policeman policeman : policeForceArray) {

			// get the policeman map
			HashMap<Object, Object> policemanMap = (HashMap<Object, Object>) policemenMap
					.get("" + policeman.ID);

			// load it into the policeman
			policeman.load(policemanMap);

		}
	}

}
