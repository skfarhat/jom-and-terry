package game.states;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Score {

	private final static String WON = "Won";
	private final static String LOST = "Lost";
	private Integer score;  
	private Date date; 
	private Boolean isWinner; 
	
	public Score() {
	}
	public Score(Date date, int score, boolean isWinner) {
		this.date = date; 
		this.score = score;  
		this.isWinner = isWinner; 
	}
	public int getScore() {
		return score;
	}
	public Boolean getIsWinner() {
		return isWinner;
	}
	public Date getDate() {
		return date;
	}
	@Override
	public String toString() {
		String wonLost = (isWinner)? WON : LOST; 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm"); 
		String dateString = sdf.format(date);
		String print = String.format("%s: Score: %d (%s)\n", dateString, score, wonLost);
		
		return print; 
	}
	
}
