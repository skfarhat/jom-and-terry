package game.city.person;

import game.Globals;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class PolicemanUser extends Policeman implements Movable{

	private boolean isSelected = false;
	
	public PolicemanUser(Robber robber, Point position, String name,
			double velocity) throws SlickException {
		super(robber, position, name, velocity);

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
		} 
		else {
			stop();
		}		
	}

	
	@Override
	public void draw() {
		super.draw();

		if (isSelected){
			selectedImage.draw(this.position.getX(), this.position.getY()-selectedImage.getHeight());
		}
	}
	// MOVEMENT
	// =====================================================================================================================

	@Override
	public boolean collides() {
		return false;
	}

	@Override
	public void stop() {

	}

	public boolean moveRight() {
		this.currentAnimation.start();

		this.position.setX((float) (this.position.getX()+Globals.VELOCITY_MULTIPLIER*velocity));

		this.rect.setX(this.position.getX());

		if (collides()){
			normalForceLeft();
			return false;
		}
		return true;
	}

	public boolean moveLeft() {
		this.currentAnimation.start();
		this.position.setX((float) (this.position.getX()-Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setX(this.position.getX());
		if (collides()){
			normalForceRight();
			return false; 
		}
		return true; 
	}

	public boolean moveUp() {
		this.position.setY((float) (this.position.getY()-Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
		if (collides()){
			normalForceDown();
			return false; 
		}
		return true;
	}

	public boolean moveDown() {
		this.position.setY((float) (this.position.getY()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
		if (collides()){
			normalForceUp();
			return false; 
		}
		return true; 
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
