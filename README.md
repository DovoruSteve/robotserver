# robotserver

Robot Server for the Coding Challenge 1

This presents a REST API for the robotclient to consume


Installation

	Java 17 or newer is required to be installed to compile and / or run the robot server

	Maven 3 or newer is required only if you want to recompile it and run the unit tests

	Download / clone the repo. I've included the jar so you can run it without rebuilding it

	Once downloaded (and extracted, if you downloaded the zip), in a shell (or command window
	on Windows), run it by changing into the robotserver directory and running 

	     java -jar target/robotserver-0.0.1.jar

	Then download / clone the robotclient from

	     https://github.com/DovoruSteve/robotclient.git

	and see its README
		

Assumptions

	The only time the Robot is “MISSING” is from when the REST server is started up until a valid PLACE
	command is entered. Once it is on the table, it is not possible for it to leave the table until the
	server is restarted.


Included

	Unit Tests for both the server and the client 


	The server can be tested using Swagger at the URL http://localhost:8080/swagger-ui/index.html

	The YAML spec for the REST API is at http://localhost:8080/api-docs.yaml

	The JSON spec for the REST API is at http://localhost:8080/api-docs

	For access to each of these links, you'll need to know its kizzie / HappyHippo 


	Logging is included because in an API it's important to log the given data and the response
	to make it easier to work with the team consuming the API.


	Error handling is included as good error reporting is critical in an API.


	It's probably overkill using services and repositories and models etc for such a simple project
	but it is done to show how it should be developed.


	Basic authentication on the REST API


Not included / Out of Scope

	When the server runs, it tries to access a Config Server at localhost:8888. It has sensible defaults so I
	didn't bother including it as its just a Spring Boot Config Server. Normally I do use these because they keep
	the configs all in one place, which can be protected as they often contain credentials (e.g. for the REST
	authentication).

	The robot is not persisted across server restarts. I stopped short of using any sort of database and
	JPA etc as the challenge didn’t require it. I did put in a repository interface to mock showing where
	the data layer would be implemented, if required.

	Enhancing the security beyond basic.
     
