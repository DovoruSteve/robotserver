package au.com.dovoru.robot.model;

/** 
 * Create the singleton Robot object as a pseudo data store
 * @author stephen
 *
 */
public class Robot {
	private static final Robot INSTANCE = new Robot(0, 0, null, true);
	public static Robot getInstance() {
		return INSTANCE;
	}
	
	private int x, y;			// Coords of the current position on the table
	private Direction facing; 	// The direction the robot is facing. If this is null, the robot is lost!
	private boolean lost;

	/**
	 * Create the robot's position by providing its x and y coords and
	 * the direction it is facing
	 */
	private Robot(int x, int y, Direction facing, boolean lost) {
		this.x = x;
		this.y = y;
		this.facing = facing;
		this.lost = lost;
	}
	
	public String report() {
		if (lost) {
			return "lost";
		} else {
			return x+","+y+","+facing;
		}
	}

	@Override
	public String toString() {
		return "Robot [x=" + x + ", y=" + y + ", facing=" + facing + ", lost=" + lost + "]";
	}

	public Robot setX(int x) {
		this.x = x;
		return this;
	}

	public Robot setY(int y) {
		this.y = y;
		return this;
	}

	public Robot setFacing(Direction facing) {
		this.facing = facing;
		return this;
	}
	
	public Robot setLost(boolean lost) {
		this.lost = lost;
		return this;
	}

	public boolean isLost() {
		return lost;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getFacing() {
		return facing;
	}
	
}
