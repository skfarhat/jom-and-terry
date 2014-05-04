package game.city.building;

import game.AudioGame;
import game.city.person.Robber;
import game.states.Play;

import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;

public class Gate {
	private Point position; 
	private Rectangle rect; 

	private boolean isOpen = false; 

	public Gate(Point position, Rectangle rect) {
		this.position = position; 
		this.rect = rect; 
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public boolean intersects(Rectangle rect){
		return this.rect.intersects(rect);
	}
	
	public boolean passThrough(Robber robber) {
		if (isOpen && robber.rect.intersects(this.rect)){
			Play.getInstance().levelUp();
			return true; 
		}
		else {
			AudioGame.playAsSound("blocked.ogg");
			return false; 
		}
	}
	public Point getPosition() {
		return position;
	}
}
