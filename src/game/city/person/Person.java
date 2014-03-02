package game.city.person;

import org.lwjgl.opengl.Drawable;

/**
 * An abstract class for the Person/Player in the game.
 * 
 * @author sami
 * 
 */
public abstract class Person {
	public String name;
	public double velocity;

	/**
	 * Creates a person.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param name
	 * @param velocity
	 */
	public Person(double positionX, double positionY, String name,
			double velocity) {
//		super(positionX, positionY);
		this.name = name;
		this.velocity = velocity;
	}
	
}