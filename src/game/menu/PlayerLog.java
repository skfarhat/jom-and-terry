package game.menu;

import game.city.person.Robber;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;


public class PlayerLog {

	private final String BACKGROUND_PATH = "res/Log.png";
	private final int SCORE_X = 0; 
	private final int SCORE_Y = 0;

	private final int MONEY_X = 0;
	private final int MONEY_Y = 30;
	
	private final int LOG_X = 0;
	private final int LOG_Y = 60;
	
	private String logText = ""; 
	
	private int xPos, yPos; 
	
	private Graphics g; 
	private Robber robber; 
	private Image background; 
	
	public PlayerLog(Graphics g, Robber robber, int xPos,int yPos) {
		this.g = g; 
		this.xPos = xPos; 
		this.yPos = yPos; 
		
		this.robber = robber;  	
		try {
			this.background = new Image(BACKGROUND_PATH);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}

	public void setLogText(String logText) {
		this.logText = logText;
	}
	
	
	public void draw(){ 

		String scoreString = String.format("Score: %d", robber.getScore());
		String moneyString = String.format("$%d", robber.getMoney());
		g.setColor(Color.white);
		g.drawImage(background, xPos, yPos);
		g.drawString(scoreString, xPos + SCORE_X, yPos+ SCORE_Y);
		g.drawString(moneyString, xPos + MONEY_X, yPos+ MONEY_Y);
		g.drawString(logText,xPos + LOG_X, yPos+ LOG_Y);
			
	}
}
