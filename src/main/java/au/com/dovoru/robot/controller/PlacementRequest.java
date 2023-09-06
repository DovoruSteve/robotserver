package au.com.dovoru.robot.controller;

import java.security.InvalidParameterException;

import au.com.dovoru.robot.model.Direction;

/**
 * Holds the request arguments for a PLACE command
 * @author stephen
 *
 */
public class PlacementRequest {
	// The following are accepted in the request as Strings to maximise error handling control
	private String x;
	private String y;
	private String facing;
		
	@Override
	public String toString() {
		return "PlacementRequest [x=" + x + ", y=" + y + ", facing=" + facing + "]";
	}

	/**
	 * Validate the parameters are okay and if not, throw exception
	 * Use this approach rather than JSR-303 @Valid because the tableSize is dynamic and so @Max can't be used etc.
	 * 
	 * @param tableSize The size of the table, which is square. 
	 * 		So, a value of 5 would mean the x coord and the y coord can each take a value 0..4
	 */
	public void validate(int tableSize) throws MissingParameterException, InvalidParameterException {
		validateCoordinate("x", x, 0, tableSize-1);
		validateCoordinate("y", y, 0, tableSize-1);
		if (isBlankString(facing)) {
			throw new MissingParameterException("Place command requires the direction the robot should face");
		} else {
			try {
				Direction.valueOf(facing);
			} catch (IllegalArgumentException ex) {
				throw new InvalidParameterException("Place command requires a valid direction the robot should face, but I was given "+facing);
			}
		}
	}
	/**
	 * Validate and return the given coordinate
	 * @param name The name of the coordinate (e.g. "x", or "y")
	 * @param value The String value provided in the request for this coordinate
	 * @param minValue The minimum value the coordinate can take (e.g. 0)
	 * @param maxValue The maximum value the coordinate can take (e.g. 4)
	 * @return The coordinate converted to an int
	 * @throws MissingParameterException If the coordinate value is missing
	 * @throws IllegalArgumentException If the coordinate value is not an int between 0 and the maxValue
	 */
	private int validateCoordinate(String name, String value, int minValue, int maxValue) {
		if (isBlankString(value)) {
			throw new MissingParameterException("Place command requires a value for coordinate "+name);
		}
		int intValue;
		try {
			intValue = Integer.parseInt(value);
		} catch (Throwable th) {
			throw new IllegalArgumentException("Place command requires the "+name+" coordinate to be a number, but I was given '"+value+"'");
		}
		if (intValue < 0 || intValue > maxValue) {
			throw new InvalidParameterException("Place command requires the "+name+" coordinate to be between 0 and "+maxValue+", but I was given "+value);
		}
		return intValue;
	}
	private boolean isBlankString(String s) {
		return s == null || s.trim().equals("");
	}

	public int getX() {
		return Integer.parseInt(x);
	}
	public int getY() {
		return Integer.parseInt(y);
	}
	public Direction getFacing() {
		return Direction.valueOf(facing);
	}

}
