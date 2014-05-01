package game.city.person;

import game.Globals;
import game.city.building.Area;
import game.city.building.Building;
import game.city.building.Gate;
import game.states.Play;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;

public class RobberUser extends Robber implements Movable {

	public RobberUser(Area area) throws SlickException {
		super(area);
	}

	@Override
	public void processInput(Input input) {

		boolean keyPressed = false; 
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			moveRight();
			keyPressed = true; 
		} else if (input.isKeyDown(Input.KEY_LEFT)) {
			moveLeft();
			keyPressed = true; 
		}
		if (input.isKeyDown(Input.KEY_UP)) {
			moveUp();
			keyPressed = true; 
		} else if (input.isKeyDown(Input.KEY_DOWN)) {
			moveDown();
			keyPressed = true; 
		}
		if (input.isKeyDown(Input.KEY_SPACE)) {
			rob();
			keyPressed = true; 
		} else if (input.isKeyPressed(Input.KEY_F)) {
			if (nearByBldg != null) {
				nearByBldg.nextFlag();
			}
			keyPressed = true; 
		}
		if (!keyPressed)
			stop();

	}

	public boolean moveRight() {
		this.currentAnimation.start();

		this.position
				.setX((float) (this.position.getX() + Globals.VELOCITY_MULTIPLIER
						* velocity));

		this.rect.setX(this.position.getX());
		this.currentAnimation = this.rightWalkAnimation;

		if (collides()) {
			normalForceLeft();
			return false;
		}

		return true;
	}

	public boolean moveLeft() {
		this.currentAnimation.start();
		this.position
				.setX((float) (this.position.getX() - Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setX(this.position.getX());
		this.currentAnimation = this.leftWalkAnimation;
		if (collides()) {
			normalForceRight();
			return false;
		}
		return true;
	}

	public boolean moveUp() {
		this.position
				.setY((float) (this.position.getY() - Globals.VELOCITY_MULTIPLIER
						* velocity));

		this.rect.setY(this.position.getY());
		this.currentAnimation = this.upWalkAnimation;
		if (collides()) {
			normalForceDown();
			return false;
		}
		return true;
	}

	public boolean moveDown() {
		this.position
				.setY((float) (this.position.getY() + Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setY(this.position.getY());
		this.currentAnimation = this.downWalkAnimation;
		if (collides()) {
			normalForceUp();
			return false;
		}
		return true;
	}

	public void normalForceRight() {
		this.position
				.setX((float) (this.position.getX() + Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setX(this.position.getX());
	}

	public void normalForceLeft() {
		this.position
				.setX((float) (this.position.getX() - Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setX(this.position.getX());
	}

	public void normalForceUp() {
		this.position
				.setY((float) (this.position.getY() - Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setY(this.position.getY());
	}

	public void normalForceDown() {
		this.position
				.setY((float) (this.position.getY() + Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setY(this.position.getY());
	}

	public boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		boolean isInCollision = false;
		this.nearByBldg = null;

		// Check if trying to pass through gates
		for (Gate gate : area.getGates()) {
			if (gate.intersects(this.rect))
				return !gate.passThrough(this);
		}

		// Collision with boundaries
		Line[] boundLines = area.getMapBounds();
		for (Line line : boundLines) {
			if (line.intersects(this.rect))
				return true;
		}

		// Collision wiht Buildings
		for (Building bldg : area.getBuildings()) {
			if (this.rect.intersects(bldg.getRect())) {
				this.nearByBldg = bldg;
				isInCollision = true;
				bldg.setShowBuildingInfo(true);
				break;
			}
			if (this.rect.intersects(bldg.robbingRegion)) {
				this.nearByBldg = bldg;
				bldg.setShowBuildingInfo(true);
			}
		}
		return isInCollision;
	}
}
