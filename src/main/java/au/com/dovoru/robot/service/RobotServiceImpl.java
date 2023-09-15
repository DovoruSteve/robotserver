package au.com.dovoru.robot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import au.com.dovoru.robot.model.Direction;
import au.com.dovoru.robot.model.Robot;
import au.com.dovoru.robot.model.RobotResourceNotFoundException;
import au.com.dovoru.robot.repository.RobotRepository;

/**
 * {@inheritDoc}
 * @author stephen
 *
 */
@Service
public class RobotServiceImpl implements RobotService {
	private final int tableSize;	// the size of the square table, so the robot coords must each be less than this				
	private final RobotRepository robotRepository;
	
	public RobotServiceImpl(@Value("${tableSize:5}") int tableSize, @Autowired RobotRepository robotRepository) {
		this.tableSize = tableSize;
		this.robotRepository = robotRepository;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot place(int x, int y, Direction facing) {
		Robot robot = robotRepository.find();
		if (x < 0 || x >= tableSize || y < 0 || y >= tableSize || facing == null) {
			robot.setLost(true);
		} else {
			robot.setX(x).setY(y).setFacing(facing).setLost(false);
		}
		return robot;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot move() {
		Robot robot = robotRepository.find();
		if (!robot.isLost()) {
			int x = robot.getX();
			int y = robot.getY();
			switch(robot.getFacing()) {
			case EAST:
				if (x < tableSize-1) {
					robot.setX(x+1);
				}
				break;
			case NORTH:
				if (y < tableSize-1) {
					robot.setY(y+1);
				}
				break;
			case SOUTH:
				if (y > 0) {
					robot.setY(y-1);
				}
				break;
			case WEST:
				if (x > 0) {
					robot.setX(x-1);
				}
				break;
			}
		}
		return robot;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot right() {
		Robot robot = robotRepository.find();
		if (!robot.isLost()) {
			Direction current = robot.getFacing();
			Direction[] values = Direction.values();
			robot.setFacing(values[(current.ordinal() + 1) % values.length]); // Move to the next direction around the compass
		}
		return robot;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot left() {
		Robot robot = robotRepository.find();
		if (!robot.isLost()) {
			Direction[] values = Direction.values();
			Direction current = robot.getFacing();
			int newOrdinal = current.ordinal() - 1;
			if ( newOrdinal < 0) { 
				newOrdinal = values.length - 1;
			}
			robot.setFacing(values[newOrdinal]); 			// Move to the previous direction around the compass
		}
		return robot;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot report() {
		return robotRepository.find();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Robot findById(Long id) throws RobotResourceNotFoundException {
		throw new RobotResourceNotFoundException("No robot with given id "+id);
	}
}
