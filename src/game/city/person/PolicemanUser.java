package game.city.person;

import game.Globals;
import game.city.building.Building;

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
		} else if (input.isKeyPressed(Input.KEY_SPACE)){
			arrestRobber(robber);
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

		float distance = (float)  Math.sqrt(Math.pow(
				this.position.getX()-this.robber.position.getX(), 2.0) 
				+ Math.pow(this.position.getY()-this.robber.position.getY(), 2.0)
				);
		if (distance < Globals.POLICEMAN_VISION_DISTANCE )
		{
			if (robberIsVisible == false){
				PoliceOffice.robberVisibleCount++; 

				// set robberIsVisible to true
				robberIsVisible = true;			
			}	
		}
		else {
			if (robberIsVisible)
			{
				PoliceOffice.robberVisibleCount--; 

				// set robberIsVisible to false
				robberIsVisible = false; 				
			}	
		}
	}
	// MOVEMENT
	// =====================================================================================================================

	@Override
	public boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		boolean isInCollision = false;

		for (Building bldg: Building.buildings) {
			if (this.rect.intersects(bldg.getRect())) {
				isInCollision = true;
				bldg.setShowBuildingInfo(true);
				break;
			}
		}
		return isInCollision;
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
	
	public void normalForceRight() {
		this.position.setX((float) (this.position.getX()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setX(this.position.getX());
	}
	
	public void normalForceLeft() {
		this.position.setX((float) (this.position.getX()-Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setX(this.position.getX());
	}
	
	public void normalForceUp() {
		this.position.setY((float) (this.position.getY()-Globals.VELOCITY_MULTIPLIER*velocity));
		
		this.rect.setY(this.position.getY());
	}
	
	public void normalForceDown() {
		this.position.setY((float) (this.position.getY()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
