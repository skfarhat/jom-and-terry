package game.menu;

import game.Game;
import game.city.person.Person;
import game.city.person.Policeman;
import game.city.person.Robber;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class PlayerLog {

	private final String BACKGROUND_PATH_ROBBER = "res/Log.png";
	private final String BACKGROUND_PATH_COP= "res/Log2.png";
	private final int SCORE_X = 0; 
	private final int SCORE_Y = 0;

	private final int MONEY_X = 0;
	private final int MONEY_Y = 30;

	private final int TIMER_X = 0;
	private final int TIMER_Y = 55;

	private final int LOG_X = 0;
	private final int LOG_Y = 60;

	private static String logText = ""; 
	private int xPos, yPos; 

	private Graphics g = Game.getInstance().getContainer().getGraphics(); 
	private Person person; 
	private Image background;

	public PlayerLog(Person person, int xPos,int yPos) {
		this.xPos = xPos; 
		this.yPos = yPos; 

		this.person = person; 

		try {
			this.background = new Image((person instanceof Robber)? BACKGROUND_PATH_ROBBER: BACKGROUND_PATH_COP);
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	public static void setLogText(String logText) {
		PlayerLog.logText = logText;
	}

	public void draw(int timer){ 

		if (Game.getInstance().getAccount().getIsRobber()){
			Robber robber = (Robber) person; 
			String scoreString = String.format("Score: %d", robber.getScore());
			String moneyString = String.format("$%d", robber.getMoney());
			String timerString = String.format("%s", getTime(timer));

			g.setColor(Color.white);
			g.drawImage(background, xPos, yPos);
			g.drawString(scoreString, xPos + SCORE_X, yPos+ SCORE_Y);
			g.drawString(moneyString, xPos + MONEY_X, yPos+ MONEY_Y);
			g.drawString(timerString, xPos + TIMER_X, yPos+ TIMER_Y); 
			g.drawString(logText,xPos + LOG_X, yPos+ LOG_Y);
		}
		else {
			Policeman policeman = (Policeman) person; 
			
			String scoreString = String.format("Score: %d", policeman.getScore());
			String timerString = String.format("%s", getTime(timer));

			g.setColor(Color.white);
			g.drawImage(background, xPos, yPos);
			g.drawString(scoreString, xPos + SCORE_X, yPos+ SCORE_Y);
			g.drawString(timerString, xPos + TIMER_X, yPos+ TIMER_Y); 
			g.drawString(logText,xPos + LOG_X, yPos+ LOG_Y);
			
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
}
