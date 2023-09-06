package au.com.dovoru.robot.repository;

import org.springframework.stereotype.Repository;

import au.com.dovoru.robot.model.Robot;

/**
 * {@inheritDoc}
 * @author stephen
 *
 */
@Repository
public class RobotRepositoryImpl implements RobotRepository {
	/**
	 * {@inheritDoc}
	 * Note that the model is implemented as a singleton
	 */
	@Override
	public Robot find() {
		return Robot.getInstance();
	}
}
