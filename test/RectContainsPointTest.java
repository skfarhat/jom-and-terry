import static org.junit.Assert.*;
import game.Globals;

import org.junit.Test;
import org.newdawn.slick.geom.Rectangle;


public class RectContainsPointTest {

	@Test
	public void test() {
		
		Rectangle rect = new Rectangle (0,0,100,500);
		final float error = 1.0f; 

		assertTrue(Globals.rectContainsPoint(rect, 0, 0, error));
		assertTrue(Globals.rectContainsPoint(rect, 50, 50, error));
		
		assertFalse(Globals.rectContainsPoint(rect, 102, 502, error));
		assertFalse(Globals.rectContainsPoint(rect, 1000, 1000, error));
	}

}
