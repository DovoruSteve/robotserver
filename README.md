# robotserver
Robot Server for the Coding Challenge 1
This presents a REST API for the robotclient to consume

Installation
	Java 17 or newer to compile and / or run the robot server
	Maven 3 or newer if you want to recompile it and run the unit tests
	Download / Clone the repo. I've included the jar so you can run it without rebuilding it

Instructions
	This is a console application, so expect to enter the commands and see the output
	The commands are:
	    PLACE x,y,direction
	    	  This allows you to place the robot onto the table, which defaults to 5x5.
		  x and y can each take a value between 0 and 4 inclusive.
		  The direction can only be NORTH, EAST, SOUTH, or WEST
            MOVE
		This moves the robot one position in the direction it is currently facing.
		If such a move would cause the robot to fall off the table, the move is ignored.
	    LEFT
		This turns the robot 90 degrees to the left
	    RIGHT
		This turns the robot 90 degrees to the right
	    REPORT
	        This displays the current position of the robot or "MISSING" if it isn't on the table.
		
Assumptions
	The only time the Robot is “MISSING” is from when the REST server is started up until a valid PLACE
	command is entered. Once it is on the table, it is not possible for it to leave the table until the
	server is restarted.

Included
	Unit Tests for both the server and the client 
	The server can be tested using Swagger at the URL http://localhost:8080/swagger-ui/index.html
	The YAML spec for the REST API is at http://localhost:8080/api-docs.yaml
	The JSON spec for the REST API is at http://localhost:8080/api-docs

	Logging is included because in an API it's important to log the given data and the response
	to make it easier to work with the team consuming the API.

	Error handling is included as good error reporting is critical in an API.

	It's probably overkill using services and repositories and models etc for such a simple project
	but it is done to show how it should be developed.


Not included / Out of Scope
    	Securing the REST API. Implementing a security layer was not specified in the requirements,
	and therefore, for a coding exercise where the server and client are likely to run on the same server,
	and there’s no sensitive data exchanged, it has not been included.

	Normally I would add at least a user/password style authentication to get a token to pass on all subsequent
	requests across the REST API. Also I’d change it to use HTTPS instead of HTTP. 

	The robot is not persisted across server restarts. I stopped short of using any sort of database and
	JPA etc as the challenge didn’t require it. I did put in a repository interface to mock showing where
	the data layer would be implemented, if required.
