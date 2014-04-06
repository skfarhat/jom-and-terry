package game.city.person;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class RobberUser extends Robber implements Movable {

	public RobberUser() throws SlickException {
		super();
	}

	@Override
	public void processInput(Input input) {
		// ARROWS: UP DOWN LEFT RIGHT
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			moveRight();
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			moveLeft();
		} else if (input.isKeyDown(Input.KEY_UP)) {
			moveUp();
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			moveDown();
		} else if (input.isKeyDown(Input.KEY_SPACE)) {
			rob();
		}
		else {
			stop();
		}
	}
}
