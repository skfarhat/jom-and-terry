package game.city.person;

import org.newdawn.slick.Input;

public interface Movable {

	
	public void processInput(Input input); 
	
	public void moveRight();
	public void moveLeft();
	public void moveUp() ;
	public void moveDown();
	
	
	// Normal Forces for collision
	public void normalForceRight();
	public void normalForceLeft();
	public void normalForceUp();
	public void normalForceDown();

	public void stop(); 
	
	public boolean collides();	
}
