package au.com.dovoru.robot.controller;

import au.com.dovoru.robot.model.Direction;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Holds the request arguments for a PLACE command
 * @author stephen
 *
 */
public class PlacementRequest {
	@NotNull( message = "Place command requires an x coordinate to be provided")
	@Min(value = 0, message = "Place command requires the x coordinate to be greater than or equal to 0")
	// We can't test the Max value as it is dynamic, so that is tested when it is used
	private int x;
	
	@NotNull( message = "Place command requires a y coordinate to be provided")
	@Min(value = 0, message = "Place command requires the y coordinate to be greater than or equal to 0")
	// We can't test the Max value as it is dynamic, so that is tested when it is used
	private int y;
	
	@NotNull( message = "Place command requires a direction")
	@Pattern(regexp = "^NORTH|SOUTH|EAST|WEST$", message = "Place command requires a direction of either NORTH, EAST, SOUTH, or WEST")
	private String facing;

	@Override
	public String toString() {
		return "PlacementRequest [x=" + x + ", y=" + y + ", facing=" + facing + "]";
	}
	public int getX(int tableSize) {
		if (x > tableSize-1) {
			throw new IllegalArgumentException("Place command requires the x coordinate to be less than or equal to "+(tableSize-1));
		}
		return x;
	}
	public int getY(int tableSize) {
		if (y > tableSize-1) {
			throw new IllegalArgumentException("Place command requires the y coordinate to be less than or equal to "+(tableSize-1));
		}
		return y;
	}
	public Direction getFacing() {
		return Direction.valueOf(facing);
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setFacing(String facing) {
		this.facing = facing;
	}
	
}
