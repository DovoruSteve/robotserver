package au.com.dovoru.robot.controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import au.com.dovoru.common.logging.RollingLogger;
import au.com.dovoru.robot.model.Robot;
import au.com.dovoru.robot.service.RobotService;
/**
 * Rest API for controlling the robot
 * This class accepts the requests and calls the RobotService to carry them out. Each request returns the current state of the robot.
 * 
 * Logging is included because in an API it's important to log the given data and the response to make it easier to work with the team consuming the API.
 * Lots of error handling is included as good error reporting is critical in an API.
 * 
 * It's probably overkill using services and repositories and models etc for a simple project but it is done to show how it should be developed.
 * 
 * @author stephen
 */

@RestController
public class RobotController {
	private final Logger logger;
	private final int tableSize;
	@Autowired
	private RobotService robotService;
	
	/** 
	 * Construct the Robot Controller initialising logging
	 * 
	 * It is expected that the values for the parameters would come from a Spring Config Server but to simplify
	 * things, I've just let them run with their default values
	 * 
	 * @param logFileDirectory The directory the logs should be put in (e.g. "logs" or "/var/logs"). The directory must exist
	 * @param numLogFiles The number of logs the rolling logger cycles through (e.g. 5)
	 * @param logFileSize The size of each log before rolling to the next one (e.g. 1000000 for 1Meg)
	 * @param tableSize The size of the table, which goes from 0..tableSize-1
	 */
	public RobotController(@Value("${logFileDirectory:logs}") String logFileDirectory, 
			@Value("${numLogFiles:5}") int numLogFiles, 
			@Value("${logFileSize:1000000}") int logFileSize,
			@Value("${tableSize:5}") int tableSize) {
		String logFile = logFileDirectory+File.separator+RobotController.class.getSimpleName()+".log";
		try {
			RollingLogger.init(logFile, logFileSize, numLogFiles);
		} catch (IOException ex) {
			System.err.println("Warning: Ignoring IOException trying to initialise the logging : "+ex.getMessage());
		}
		logger = Logger.getLogger(RobotController.class.getSimpleName());
		this.tableSize = tableSize;
	}
	/**
	 * Constructor used for unit testing
	 * @param robotService
	 */
	public RobotController() {
		this("logs",5,100000,5);
	}
	
	/**
	 * Place a new robot on the table
	 * Note that @Valid is not used because I want to take the max valud for the coords from a dynamic value
	 * @param placementRequest The x,y coordinates at which to place the robot and the direction it should be facing. 
	 * If the placementRequest is not valid, an IllegalArgumentException or MissingParameterException is thrown containing the reason
	 * @return The robot in the given position
	 */
	@PostMapping("/place")
	public Robot place(@RequestBody PlacementRequest placementRequest) {
		logger.info("Received Place request "+placementRequest);
		placementRequest.validate(tableSize);	//  org.springframework.http.converter.HttpMessageNotReadableExceptionThrows a runtime exception if not valid
		Robot robot = robotService.place(placementRequest.getX(),placementRequest.getY(),placementRequest.getFacing());
		logger.info("Place request returning "+robot.report());
		return robot;
	}
	/**
	 * If the robot's current state is valid, then move the robot one unit forward. If the move would cause the robot to fall off the table, 
	 * then the move request is ignored
	 * @param placementRequest
	 * @return The robot in its new position. 
	 */
	@PutMapping("/move")
	public Robot move() {
		logger.info("Received move request");
		Robot robot = robotService.move();
		logger.info("Move request returning "+robot.report());
		return robot;
	}
	/**
	 * If the robot's current state is valid, then turn the robot to the right 90 degrees
	 * @param placementRequest
	 * @return The robot in its new direction. 
	 */
	@PutMapping("/right")
	public Robot right() {
		logger.info("Received turn right request");
		Robot robot = robotService.right();
		logger.info("Turn right request returning "+robot.report());
		return robot;
	}
	/**
	 * If the robot's current state is valid, then turn the robot to the left 90 degrees
	 * @param placementRequest
	 * @return The robot in its new direction. 
	 */
	@PutMapping("/left")
	public Robot left() {
		logger.info("Received turn left request");
		Robot robot = robotService.left();
		logger.info("Turn left request returning "+robot.report());
		return robot;	}
	/**
	 * Report on the current state of the robot
	 * @return The robot in its current position and direction
	 */
	@GetMapping("/report")
	public Robot report() {
		logger.info("Received report request");
		Robot robot = robotService.report();
		logger.info("Report request returning "+robot.report());
		return robot;
	}
	
	public Logger getLogger() {
		return logger;
	}
	public int getTableSize() {
		return tableSize;
	}
	public RobotService getRobotService() {
		return robotService;
	}
	public void setRobotService(RobotService robotService) {
		this.robotService = robotService;
	}
	
}
