package game.city.road;

/**
 * A road, which may or may not be lighted.
 * 
 * @author sami
 * 
 */
public class Road {
	/**
	 * Indicates whether the road is lighted.
	 */
	public boolean isLighted;

	/**
	 * Creates a road.
	 * 
	 * @param positionX
	 * @param positionY
	 * @param isLighted
	 */
	public Road(double positionX, double positionY, boolean isLighted) {
//		super(positionX, positionY);
		this.isLighted = isLighted;
	}

	/**
	 * Display's a road's info.
	 */
	public void displayRoadInfo() {
//		String info = String.format("Road position (%d,%d). Lights:%s",
//				positionX, positionY, (isLighted) ? "ON" : "OFF");
//		System.out.println(info);
	}
}
