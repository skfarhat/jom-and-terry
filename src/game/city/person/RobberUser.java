package game.city.person;

import game.Globals;
import game.city.Camera;
import game.city.building.Building;
import game.states.Play;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class RobberUser extends Robber implements Movable {

	public RobberUser() throws SlickException {
		super();
	}

	@Override
	public void processInput(Input input) {
		
		if (input.isMousePressed(Input.MOUSE_LEFT_BUTTON)) {
			System.out.println("Process Input");
			
			// get the building
			Point pnt = new Point (input.getMouseX(), input.getMouseY());
			
			Building bldg = selectBuilding(pnt);
					
			if (bldg==null)
				return; 
			
			// display info for this building
			bldg.setShowBuildingInfo(true);
			
		}
		else if (input.isKeyDown(Input.KEY_RIGHT)) {
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
		else if (input.isKeyPressed(Input.KEY_F)){
			if (nearByBldg !=null){
				nearByBldg.nextFlag();
			}
		}
		else {
			stop();
		}
	}
	
	public boolean moveRight() {
		this.currentAnimation.start();
		
		this.position.setX((float) (this.position.getX()+Globals.VELOCITY_MULTIPLIER*velocity));
		
		this.rect.setX(this.position.getX());
		this.currentAnimation = Robber.rightWalkAnimation;

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
		this.currentAnimation = Robber.leftWalkAnimation;
		if (collides()){
			normalForceRight();
			return false;
		}
		return true; 
	}

	public boolean moveUp() {
		this.position.setY((float) (this.position.getY()-Globals.VELOCITY_MULTIPLIER*velocity));

		this.rect.setY(this.position.getY());
		this.currentAnimation = Robber.upWalkAnimation;
		if (collides()){
			normalForceDown();
			return false; 
		}
		return true; 
	}

	public boolean moveDown() {
		this.position.setY((float) (this.position.getY()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
		this.currentAnimation = Robber.downWalkAnimation;
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

	public boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		boolean isInCollision = false;
		this.nearByBldg = null;

		for (Building bldg: Building.buildings) {
			if (this.rect.intersects(bldg.rect)) {
				this.nearByBldg = bldg;
				isInCollision = true;
				bldg.setShowBuildingInfo(true);
				break;
			}
		}
		return isInCollision;
	}

	
	private Building selectBuilding(Point pnt){
		Camera camera = Play.getInstance().getCamera();
		// create a rectangle and use the intersect method to check whether 
		// the policeman rect intersects with the mouse click
		Rectangle rect = new Rectangle(
				camera.getCameraX() + pnt.getX() - Globals.SELECTION_ERROR/2, 
				camera.getCameraY() + pnt.getY() - Globals.SELECTION_ERROR/2,
				Globals.SELECTION_ERROR,
				Globals.SELECTION_ERROR);
		
		for (Building bldg: Building.buildings){
			if (bldg.rect.intersects(rect))
			{
				return bldg;  
			}
		}
		return null;
	}
}
