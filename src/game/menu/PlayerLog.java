package game.menu;

import game.Game;
import game.city.building.Area;
import game.city.person.Person;
import game.city.person.Policeman;
import game.city.person.Robber;
import game.states.Play;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;


public class PlayerLog  {

	private final String BACKGROUND_PATH_ROBBER = "res/Log.png";
	private final String BACKGROUND_PATH_COP= "res/Log2.png";
	private final int SCORE_X = 0; 
	private final int SCORE_Y = 0;

	private final int MONEY_X = 0;
	private final int MONEY_Y = 35;

	private final int TIMER_X = 0;
	private final int TIMER_Y = 20;
	
	private final int GATHERS_X = 0;
	private final int GATHERS_Y = 35;

	private final int LOG_X = 0;
	private final int LOG_Y = 60;

	private final int ROBBED_BLDGS_X = 0;
	private final int ROBBED_BLDGS_Y = 70;

	private final int FLAGS_X_INTERVAL = 20;
	private final int FLAGS_Y = 100;


	// Used so that the MenuButtons work
	protected boolean leftMouseButtonReleased;
	private static String logText = ""; 

	private Point position; 

	private Graphics g = Game.getInstance().getContainer().getGraphics(); 
	private Person person; 
	private Image background;

	private MenuButton[] buttons; 

	public PlayerLog(Person person, Point position) {

		this.position = position;

		this.person = person; 

		try {
			this.background = new Image((person instanceof Robber)? BACKGROUND_PATH_ROBBER: BACKGROUND_PATH_COP);

			initFlagButtons();
		} catch (SlickException e) {
			e.printStackTrace();
		}


	}

	private void initFlagButtons() throws SlickException{

		final Image redImage = new Image("res/Flags/red.png");
		final Image redImageDeselected = new Image("res/Flags/red-deselected.png");

		final Image violetImage = new Image("res/Flags/violet.png");
		final Image violetImageDeselected = new Image("res/Flags/violet-deselected.png");

		final Image greenImage = new Image("res/Flags/green.png");
		final Image greenImageDeselected = new Image("res/Flags/green-deselected.png");

		final Image darkBlueImage = new Image("res/Flags/dark-blue.png");
		final Image darkBlueImageDeselected = new Image("res/Flags/dark-blue-deselected.png");

		MenuButton redFlagButton = new MenuButton(Game.getInstance()
				.getContainer(), redImage , (int) position.getX() + FLAGS_X_INTERVAL*1, FLAGS_Y) {
			@Override
			public void performAction() {

				// set the flagsShown in the Play State
				// grab the boolean determining whether we need to show the flag or not
				boolean shown = Play.getInstance().flagsShown[1] = !Play.getInstance().flagsShown[1];

				// change the image of the button
				this.setImage((shown)? redImage : redImageDeselected);

			}
		};
		MenuButton greenFlagButton = new MenuButton(Game.getInstance()
				.getContainer(), greenImage , (int) (int)position.getX() + FLAGS_X_INTERVAL*2, FLAGS_Y) {
			@Override
			public void performAction() {

				// set the flagsShown in the Play State
				// grab the boolean determining whether we need to show the flag or not
				boolean shown = Play.getInstance().flagsShown[2] = !Play.getInstance().flagsShown[2];

				// change the image of the button
				this.setImage((shown)? greenImage : greenImageDeselected);
			}
		};
		MenuButton darkBlueFlagButton = new MenuButton(Game.getInstance()
				.getContainer(), darkBlueImage , (int) position.getX() + FLAGS_X_INTERVAL*3 , FLAGS_Y) {
			@Override
			public void performAction() {
				// set the flagsShown in the Play State
				// grab the boolean determining whether we need to show the flag or not
				boolean shown = Play.getInstance().flagsShown[3] = !Play.getInstance().flagsShown[3];

				// change the image of the button
				this.setImage((shown)? darkBlueImage : darkBlueImageDeselected);
			}
		};
		MenuButton violetFlagButton = new MenuButton(Game.getInstance()
				.getContainer(), violetImage ,(int) position.getX() + FLAGS_X_INTERVAL*4, FLAGS_Y) {
			@Override
			public void performAction() {

				// set the flagsShown in the Play State
				// grab the boolean determining whether we need to show the flag or not 
				boolean shown = Play.getInstance().flagsShown[4] = !Play.getInstance().flagsShown[4];

				// change the image of the button
				this.setImage((shown)? violetImage : violetImageDeselected);
			}
		};


		// set the buttons
		setButtons(redFlagButton, greenFlagButton, darkBlueFlagButton, violetFlagButton); 
	}

	public void setButtons(MenuButton ...buttons) {
		this.buttons = buttons;
	}

	public static void setLogText(String logText) {
		PlayerLog.logText = logText;
	}

	public void draw(int timer){ 

		final Area area = Play.getInstance().getArea(); 

		if ((person instanceof Robber)){
			Robber robber = (Robber) person; 
			String scoreString = String.format("Score: %d", (int) robber.getScore());
			String moneyString = String.format("$%d", robber.getMoney());
			String timerString = String.format("%s", getTime(timer));

			g.setColor(Color.white);
			g.drawImage(background, position.getX(), position.getY());
			g.drawString(scoreString, position.getX() + SCORE_X, position.getY()+ SCORE_Y);
			g.drawString(moneyString, position.getX() + MONEY_X, position.getY()+ MONEY_Y);
			g.drawString(timerString, position.getX() + TIMER_X, position.getY()+ TIMER_Y);
			g.drawString(
					String.format("Robbed: %d/%d",
							area.getNumberOfRobbedBuildings(),
							area.getBuildings().size()),
							position.getX() + ROBBED_BLDGS_X, position.getY() + ROBBED_BLDGS_Y);
			g.drawString(logText,position.getX() + LOG_X, position.getY()+ LOG_Y);
		}
		else {
			Policeman policeman = (Policeman) person; 

			String scoreString = String.format("Score: %d",(int) area.getPoliceOffice().getScore());
			String timerString = String.format("%s", getTime(timer));

			g.setColor(Color.white);
			g.drawImage(background, position.getX(), position.getY());
			g.drawString(scoreString, position.getX() + SCORE_X, position.getY()+ SCORE_Y);
			g.drawString(timerString, position.getX() + TIMER_X, position.getY()+ TIMER_Y);
			g.drawString(
					String.format("Gathers:%d", area.getPoliceOffice().getGathersRemaining()),
					position.getX() + GATHERS_X, position.getY()+ GATHERS_Y);
			g.drawString(
					String.format("Robbed: %d/%d",
							area.getNumberOfRobbedBuildings(),
							area.getBuildings().size()),
							position.getX() + ROBBED_BLDGS_X, position.getY() + ROBBED_BLDGS_Y);
			g.drawString(logText,position.getX() + LOG_X, position.getY()+ LOG_Y);
		}

		for (MenuButton button: buttons){
			button.render(g);
		}

		checkForButtonClicks();

	}

	public void processInput(Input input){
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			clickButton();
		}
	}

	private String getTime(int time){
		int mn;
		int sec;

		int rem = (int)(time%3600);
		mn = rem/60;
		sec = rem%60;

		String mnStr = (mn<10 ? "0" : "")+mn;
		String secStr = (sec<10 ? "0" : "")+sec;
		return String.format("%s:%s", mnStr, secStr);

	}
	public void setPerson(Person person) {
		this.person = person;
		try {background = new Image((person instanceof Robber)? BACKGROUND_PATH_ROBBER: BACKGROUND_PATH_COP);}
		catch (SlickException e) {e.printStackTrace();}
	}
	public void clickButton() {
		for (MenuButton b : buttons) {
			if (b.isMouseOver())
				b.performAction();
		}
	}

	protected final void checkForButtonClicks() {
		if (leftMouseButtonReleased) {
			leftMouseButtonReleased = false;
			clickButton();
		}
	}
}
