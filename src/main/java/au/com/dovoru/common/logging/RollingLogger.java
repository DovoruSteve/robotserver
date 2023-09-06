package au.com.dovoru.common.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * Set up logging to cycle through a fixed number of log files
 * @author stephen
 *
 */
public class RollingLogger {
	/**
	 * Use this function to remove all other logger handlers and add a single rolling log file handler. 
	 * @param fileName The name of the log file. Include the extension and and path e.g. "logs/myapp.log"
	 * @param logFileSize The size of each of the log files before it rolls in bytes e.g. 1000000 for 1 Meg
	 * @param numOfRollingLogs The number of log files to keep e.g. 5
	 * @throws IOException If it can't open the file handler
	 */
	public static void init(String fileName, int logFileSize, int numOfRollingLogs) throws IOException {
        Logger globalLogger = Logger.getLogger("");
        Handler[] handlers = globalLogger.getHandlers();
        for(Handler handler : handlers) {
            globalLogger.removeHandler(handler);
        }
        FileHandler fileHandler = new FileHandler(fileName, logFileSize, numOfRollingLogs, true);
        fileHandler.setFormatter(new LoggerFormatter());
        globalLogger.addHandler(fileHandler);
 	}

}
