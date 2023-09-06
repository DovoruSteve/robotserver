package au.com.dovoru.robot.service;

import au.com.dovoru.robot.model.Direction;
import au.com.dovoru.robot.model.Robot;

/**
 * Business logic for the robot controls is handled by this interface
 * 
 * @author stephen
 *
 */
public interface RobotService {
	/**
	 * Place the robot on the table at the given x,y coordinates and facing the given direction
	 * If the placement of the robot is invalid, then the returned robot will be "lost" i.e. isLost() will return true
	 * @param x The x coordinate for the robot, which should be >= 0 and < tableSize 
	 * @param y The y coordinate for the robot, which should be >= 0 and < tableSize
	 * @param facing The direction this robot is to face (e.g. NORTH)
	 * @return The robot in its new placement. The caller should check the returned object's isLost() method 
	 * before using in order to confirm the placement was valid
	 */
	Robot place(int x, int y, Direction facing);
	/**
	 * Move the robot implementing the business rule: Any move that would cause the robot to fall must be ignored
	 * @return The robot after the move
	 */
	Robot move();
	/**
	 * Turn the robot to the right 90 degrees, provided it is not lost
	 * @return The robot in the new direction
	 */
	Robot right();
	/**
	 * Turn the robot to the left 90 degrees, provided it is not lost
	 * @return The robot in the new direction
	 */
	Robot left();
	/**
	 * Fetch the robot
	 * @return The robot in its current state
	 */
	Robot report();
}
