package game.city.person;

import game.Globals;
import game.city.building.Area;
import game.city.building.Building;
import game.states.Play;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Vector2f;

public class PolicemanUser extends Policeman implements Movable{

	/**
	 * Used only in the case where the user presses 'G' and wants all the Policemen to automatically gather somewhere. 
	 * This can be used only 3 times
	 */
	private boolean isGathering = false; 
	private boolean isSelected = false;
	private boolean robberArrested = false; 
	
	public PolicemanUser(Area area, Robber robber, Point position, String name,
			double velocity) throws SlickException {
		super(area, robber, position, name, velocity);

		stop(); 
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
			System.out.println("here");
			if (!isGathering)
				robberArrested = arrestRobber(robber);
		}
		else {
			stop();
		}		
	}

	public void gather(Circle region){
		this.destPoint = randomPointInCircle(region);

		this.direction = new Vector2f(
				destPoint.getX()  - this.position.getX(), 
				destPoint.getY() - this.position.getY()
				);
		this.isGathering = true;
		
		// TODO: probably remove this
		this.isMoving = true;
	}

	@Override
	public void draw() {
		super.draw();

		// if Policeman is moving, change xPos and yPos
		if (isGathering) {
			float speed = (float) (Globals.VELOCITY_MULTIPLIER * velocity);

			float deltaX = this.position.getX() - this.destPoint.getX(); 
			float deltaY = this.position.getY() - this.destPoint.getY();

			// we want the policeman to go all the way horizontally then after arrivign to the correct xpos
			// go all the way vertically, so we declare a variable movingHorizontally and we set it to true whenever the policeman is going horizontally
			// if true, the second if statement is not satisfied and the yPos has to wait for the xPos to finish
			boolean movingHorizontally = false;
			if (Math.abs(deltaX) > 2.0f)
			{
				float newX = (deltaX<0)? this.position.getX() + speed: this.position.getX() + (-1)*speed; 
				this.position.setX(newX);
				this.rect.setX(this.position.getX());
				movingHorizontally = true;
			}

			// check on movingHorizontally done here
			if (Math.abs(deltaY) > 2.0f && !movingHorizontally)
			{	
				float newY = (deltaY<0)? this.position.getY() + speed: this.position.getY() + (-1)*speed;
				this.position.setY(newY);
				this.rect.setY(this.position.getY());
			}

			// 2.0f margin of error
			if (Math.abs(this.position.getX() - this.destPoint.getX()) < 2.0f
					&& Math.abs(this.position.getY() - this.destPoint.getY()) < 2.0f) {

				this.isMoving = false;
				this.isGathering = false;
					
				// attempt to arrest the robber after arriving to the spot
				this.robberArrested = robber.isCaught = arrestRobber(this.robber);
			}
		}

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
				robberIsVisible = true;			
			}	
		}
		else {
			if (robberIsVisible)
			{
				PoliceOffice.robberVisibleCount--; 
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


		// Collision with boundaries
		Line[] boundLines = Play.getInstance().getArea().getMapBounds();
		for (Line line : boundLines) {
			if (line.intersects(this.rect))
				return true;
		}
		
		boolean isInCollision = false;
		for (Building bldg: area.getBuildings()) {
			if (this.rect.intersects(bldg.getRect())) {
				isInCollision = true;
				bldg.setShowBuildingInfo(true);
				break;
			}
		}
		return isInCollision;
	}

	public boolean moveRight() {
		this.currentAnimation.start();
		this.position.setX((float) (this.position.getX()+Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setX(this.position.getX());
		this.currentAnimation = this.rightWalkAnimation;
		
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
		this.currentAnimation = this.leftWalkAnimation;
		if (collides()){
			normalForceRight();
			return false; 
		}
		return true; 
	}

	public boolean moveUp() {
		this.currentAnimation.start();
		this.position.setY((float) (this.position.getY()-Globals.VELOCITY_MULTIPLIER*velocity));
		this.rect.setY(this.position.getY());
		if (collides()){
			normalForceDown();
			return false; 
		}
		return true;
	}

	public boolean moveDown() {
		this.currentAnimation.start();
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

	public boolean isRobberArrested() {
		return robberArrested;
	}

	public boolean isGathering() {
		return isGathering;
	}
}
