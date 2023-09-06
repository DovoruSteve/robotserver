package au.com.dovoru.robot.repository;

import au.com.dovoru.robot.model.Robot;
/**
 * In this trivial case, there is always precisely one and so it is returned and since a reference to is returned, it is not 
 * necessary to add save (or similar) methods.
 * 
 * Normally this would be an interface to a database and use JPA or similar, so this is just here as a proof of concept
 * @author stephen
 *
 */
public interface RobotRepository {
	/**
	 * Find the robot and return it. 
	 */
	Robot find();
}
