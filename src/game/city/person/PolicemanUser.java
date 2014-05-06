package game.city.person;

import game.Game;
import game.Globals;
import game.city.building.Area;
import game.city.building.Building;

import java.util.Timer;
import java.util.TimerTask;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Path.Step;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

/**
 * A policeman that can be controlled by the player
 * 
 * @author sami
 * 
 */
public class PolicemanUser extends Policeman implements Movable, Mover {

	/**
	 * Used only in the case where the user presses 'G' and wants all the
	 * Policemen to automatically gather somewhere. This can be used only 3
	 * times
	 */
	private boolean isGathering = false;
	private boolean isSelected = false;
	private boolean robberArrested = false;

	private Path pathToTarget;
	private Step nextStep;
	private Step currentStep;
	private int pathToTargetIndex = 0;

	public PolicemanUser(Area area, Robber robber, Point position, String name,
			double velocity) throws SlickException {
		super(area, robber, position, name, velocity);

		this.userIsPolice = true;

		// keep changing the position of the policemen until he doesn't collide
		// with anything
		while (collides()) {
			this.position.setX(Globals.random.nextInt(800));
			this.position.setY(Globals.random.nextInt(800));
			this.rect.setX(position.getX());
			this.rect.setY(position.getY());
		}
		stop();
	}

	@Override
	public void processInput(Input input) {
		// ARROWS: UP DOWN LEFT RIGHT

		if (!isGathering)
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				moveRight();
			} else if (input.isKeyDown(Input.KEY_LEFT)) {
				moveLeft();
			} else if (input.isKeyDown(Input.KEY_UP)) {
				moveUp();
			} else if (input.isKeyDown(Input.KEY_DOWN)) {
				moveDown();
			} else if (input.isKeyPressed(Input.KEY_SPACE)) {
				if (!isGathering)
					robberArrested = arrestRobber(robber);
			} else if (input.isKeyPressed(Input.KEY_F)) {
				if (nearByBldg != null) {
					nearByBldg.nextFlag();
				}
			} else {
				stop();
			}
	}

	// TODO: keep only one of the functions
	/**
	 * Unused, remove later
	 * 
	 * @param region
	 */
	public void gather(Circle region) {
		System.out.println("gather");
		this.destPoint = randomPointInCircle(region);

		this.isGathering = true;

		// TODO: probably remove this
		this.isMoving = true;
	}

	/**
	 * Unused, remove later
	 * 
	 * @param region
	 */
	public void gather2(Circle region) {
		TileBasedMap tileBasedMap = area.getCityMap();
		AStarPathFinder pathFinder = new AStarPathFinder(tileBasedMap,
				Integer.MAX_VALUE, false);

		int targetX = (int) region.getCenterX() / Globals.TILE_SIZE;
		int targetY = (int) region.getCenterY() / Globals.TILE_SIZE;

		int posX = (int) this.position.getX() / Globals.TILE_SIZE;
		int posY = (int) this.position.getY() / Globals.TILE_SIZE;

		position.setX(posX * Globals.TILE_SIZE);
		position.setY(posY * Globals.TILE_SIZE);

		pathToTarget = pathFinder.findPath((Mover) this, posX, posY, targetX,
				targetY);

		if (pathToTarget == null)
			return;

		pathToTargetIndex = 0;
		nextStep = pathToTarget.getStep(0);

		moveToNextStep();

		this.isGathering = true;
		this.isMoving = true;
	}

	/**
	 * Gather in a region
	 * 
	 * @param region
	 */
	public void gather1(Circle region) {

		TileBasedMap tileBasedMap = area.getCityMap();
		AStarPathFinder pathFinder = new AStarPathFinder(tileBasedMap,
				Integer.MAX_VALUE, true);

		// current position (rounded)
		final int posX = (int) position.getX() / Globals.TILE_SIZE;
		final int posY = (int) position.getY() / Globals.TILE_SIZE;
		// Target position (rounded)
		final int targetX = (int) region.getCenterX() / Globals.TILE_SIZE;
		final int targetY = (int) region.getCenterY() / Globals.TILE_SIZE;

		// TODO: Check posX,posY, targetX and targetY are in the bounds of the
		// map

		// Get the Path to the target
		pathToTarget = pathFinder.findPath((Mover) this, posX, posY, targetX,
				targetY);
		// reset the index of pathToTarget
		pathToTargetIndex = 0;

		// if there is no path to the desired area, return
		if (pathToTarget == null)
			return;

		currentStep = pathToTarget.getStep(pathToTargetIndex);
		nextStep = pathToTarget.getStep(pathToTargetIndex + 1);

		// set the current position to the rounded
		position.setX(currentStep.getX() * Globals.TILE_SIZE);
		position.setY(currentStep.getY() * Globals.TILE_SIZE);

		moveToNextStep();
	}

	public void moveToNextStep() {

		final int period = 80;
		final int interval = Globals.TILE_SIZE;

		final Timer timer = new Timer();

		final TimerTask timerTask = new TimerTask() {
			int count = 0;

			@Override
			public void run() {

				if (count == interval) {

					count = 0; // reset count

					// make sure there are more steps to get
					if (pathToTargetIndex < pathToTarget.getLength() - 1) {
						currentStep = nextStep;
						pathToTargetIndex++;
						nextStep = pathToTarget.getStep(pathToTargetIndex);
					}
					// if there are no more steps cancel the timer
					else
						timer.cancel();
				}

				int stepX = (nextStep.getX() - currentStep.getX()) * interval;
				int stepY = (nextStep.getY() - currentStep.getY()) * interval;

				// compute delta of prev and next steps
				float newX = position.getX() + stepX / interval;
				float newY = position.getY() + stepY / interval;

				position.setX(newX);
				position.setY(newY);

				count++;
			}
		};
		timer.schedule(timerTask, 0, period / interval);

	}

	public void moveFrom(Step currStep, final Step nextStep) {
		final float deltaX = Globals.TILE_SIZE
				* (nextStep.getX() - currStep.getX());
		final float deltaY = Globals.TILE_SIZE
				* (nextStep.getY() - currStep.getY());

		System.out.println("moveFrom: (" + currStep.getX() + ","
				+ currStep.getY() + ") nextStep: (" + nextStep.getX() + ","
				+ nextStep.getY() + ") actual position: (" + position.getX()
				+ "," + position.getY());
		boolean moveHorizontally = deltaY == 0;

		final int period = 16;
		if (moveHorizontally) {
			final Timer timer = new Timer();

			final TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					// float newX = position.getX() + deltaX/Globals.TILE_SIZE;
					float newX = position.getX() + 1;

					position.setX(newX);
					if (Math.abs(position.getX() - nextStep.getX()
							* Globals.TILE_SIZE) < 1.0f) {
						System.out.println("X-timer cancelled");
						timer.cancel();
					}
				}
			};

			timer.schedule(timerTask, 0, period);
		} else {
			final Timer timer = new Timer();

			final TimerTask tt2 = new TimerTask() {
				@Override
				public void run() {
					// float newY = position.getY() + deltaY/Globals.TILE_SIZE;
					float newY = position.getY() + 1;
					position.setY(newY);

					if (Math.abs(position.getY() - nextStep.getY()
							* Globals.TILE_SIZE) < 1.0f) {
						System.out.println("Y-timer cancelled");
						timer.cancel();
					}
				}
			};

			timer.scheduleAtFixedRate(tt2, 0, period);
		}
	}

	@Override
	public void draw() {
		super.draw();

		if (pathToTarget != null)
			for (int i = 0; i < pathToTarget.getLength(); i++) {
				Step step = pathToTarget.getStep(i);
				Rectangle rect = new Rectangle(step.getX() * Globals.TILE_SIZE,
						step.getY() * Globals.TILE_SIZE, 10, 10);
				Game.getInstance().getContainer().getGraphics().draw(rect);
			}

		if (isSelected) {
			selectedImage.draw(this.position.getX(), this.position.getY()
					- selectedImage.getHeight());

		}
		float distance = (float) Math.sqrt(Math.pow(this.position.getX()
				- this.robber.position.getX(), 2.0)
				+ Math.pow(this.position.getY() - this.robber.position.getY(),
						2.0));
		if (distance < Globals.POLICEMAN_VISION_DISTANCE) {
			if (robberIsVisible == false) {
				PoliceOffice.robberVisibleCount++;
				robberIsVisible = true;
			}
		} else {
			if (robberIsVisible) {
				PoliceOffice.robberVisibleCount--;
				robberIsVisible = false;
			}
		}

	}

	public void moveToStep(Step step) {
		int x = step.getX() * Globals.TILE_SIZE;
		int y = step.getY() * Globals.TILE_SIZE;
		destPoint = new Point(x, y);
	}

	public void reachedStep() {

		System.out.println("reached step");
		final int length = pathToTarget.getLength();

		System.out.println("length: " + length);
		System.out.println("path-index: " + pathToTargetIndex);
		if (pathToTargetIndex >= length) {
			this.isMoving = false;
			this.isGathering = false;
			// attempt to arrest the robber after arriving to the spot
			this.robberArrested = robber.isCaught = arrestRobber(this.robber);
			destPoint = null;
		} else
			moveToNextStep();
	}

	// MOVEMENT
	// =====================================================================================================================

	@Override
	public boolean collides() {
		// convert the position of the Player from pixels to 'Tile' position
		// divide by the tile Size (in this case 16px)

		// Collision with boundaries
		Line[] boundLines = area.getMapBounds();
		for (Line line : boundLines) {
			if (line.intersects(this.rect))
				return true;
		}

		nearByBldg = null;
		boolean isInCollision = false;
		for (Building bldg : area.getBuildings()) {
			if (rect.intersects(bldg.getRect())) {
				isInCollision = true;
				nearByBldg = bldg;
				bldg.setShowBuildingInfo(true);
				break;
			}
			if (this.rect.intersects(bldg.robbingRegion)) {
				this.nearByBldg = bldg;
				bldg.setShowBuildingInfo(true);
			}
		}
		final int x = (int) position.getX() / Globals.TILE_SIZE;
		final int y = (int) position.getY() / Globals.TILE_SIZE;

		return area.getCityMap().blocked(this) || isInCollision;
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
		this.currentAnimation.start();
		this.position
				.setY((float) (this.position.getY() - Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setY(this.position.getY());
		if (collides()) {
			normalForceDown();
			return false;
		}
		return true;
	}

	public boolean moveDown() {
		this.currentAnimation.start();
		this.position
				.setY((float) (this.position.getY() + Globals.VELOCITY_MULTIPLIER
						* velocity));
		this.rect.setY(this.position.getY());
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
