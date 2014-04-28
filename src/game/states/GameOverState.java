package game.states;


import game.Game;
import game.Globals;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState {

	private static String RobberGameOver 	= "res/GameOver/Robber-GameOver.png";
	private static String RobberWin			= "res/GameOver/Robber-Win.png";

	private static String PoliceWin 		= "res/GameOver/Police-Win.png";
	private static String PoliceGameOver 	= "res/GameOver/Police-GameOver.png";

	private GameContainer gc;
	private Image gameOverImage;

	protected boolean youWin = false;  

	public GameOverState() {
	}
	
	public void set(boolean userIsRobber, boolean youWin){
		try{
			if (youWin)
				this.gameOverImage = new Image((userIsRobber)?RobberWin: PoliceWin);
			else
				this.gameOverImage = new Image((userIsRobber)?RobberGameOver: PoliceGameOver);
	
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	public GameOverState(boolean userIsRobber, boolean youWin) {
		try{
			if (youWin)
				this.gameOverImage = new Image((userIsRobber)?RobberWin: PoliceWin);
			else
				this.gameOverImage = new Image((userIsRobber)?RobberGameOver: PoliceGameOver);
		}
		catch (Exception exc){
			exc.printStackTrace();
		}
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.gc = gc;


	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		g.setColor(Color.white);

		g.drawImage(this.gameOverImage, 0, 0);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {

		Input input = gc.getInput();

		if (input.isKeyDown(Input.KEY_ENTER) || input.isKeyDown(Input.KEY_ESCAPE) || input.isKeyDown(Input.KEY_SPACE))
		{
			gc.reinit();
			Game.getInstance().enterState(Globals.AREA_PICK);
		}
	}

	@Override
	public int getID() {
		return 4;
	}

	public void setYouWin(boolean youWin) {
		this.youWin = youWin;
	}
}
