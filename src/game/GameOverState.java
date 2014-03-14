package game;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class GameOverState extends BasicGameState {
	
	private String gameOverPath = "res/GameOver/GameOver.png";
	private GameContainer gc;
	
	private Image gameOverImage; 
	
	public GameOverState(int state, StateBasedGame sbg){
	}

	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		this.gc = gc;
		
		try{
			this.gameOverImage = new Image(gameOverPath);
		}
		catch(SlickException sexc)
		{
			sexc.printStackTrace();
		}
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
			this.gc.reinit();
		}
	}

	@Override
	public int getID() {
		return 4;
	}
	
}
