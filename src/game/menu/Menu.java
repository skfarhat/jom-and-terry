package game.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public abstract class Menu extends BasicGameState {

	protected boolean leftMouseButtonReleased;
	protected MenuButton[] buttons;
	protected Image background;


	public abstract int getID();
	public abstract void initButtons();  

	public Menu(int state, StateBasedGame sbg){
		super();
	}

	public final void setButtons(MenuButton... buttons) {
		this.buttons = buttons;
	}

	private void clickButton() {
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

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			leftMouseButtonReleased = true;
		}
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		initButtons();
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2)
			throws SlickException {
		this.checkForButtonClicks();

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		for (MenuButton b : buttons) {
			b.render(g);
		}
	}



}
