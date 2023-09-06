package au.com.dovoru.robot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import au.com.dovoru.robot.controller.RobotController;
import au.com.dovoru.robot.service.RobotService;

/**
 * Unit tests for the robot
 * @author stephen
 */

@SpringBootTest(classes = TruckRobotApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)	// Allows tests off the table to run before it is placed on the table
class TruckRobotApplicationTests {
	MockMvc mockMvc;
	@InjectMocks
	@Autowired
	private RobotController robotController;
	@Autowired 
	private RobotService robotService;
	
	private final String incorrectXPlaceJSON = "{\"x\": \"10\", \"y\": \"0\", \"facing\": \"NORTH\"}";
	private final String missingDirectionJSON = "{\"x\": \"0\", \"y\": \"0\"}";
	private final String correct44NPlacementJSON = "{\"x\": \"4\", \"y\": \"4\", \"facing\": \"NORTH\"}";
	private final String correct44NPlacementResponseJSON = "{\"x\": 4, \"y\": 4, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct00PlacementJSON = "{\"x\": \"0\", \"y\": \"0\", \"facing\": \"NORTH\"}";
	private final String correct00NPlacementResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct00WPlacementResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": \"WEST\", \"lost\": false}";
	private final String correct01NPlacementResponseJSON = "{\"x\": 0, \"y\": 1, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct12EPlacementJSON = "{\"x\": 1, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct12EPlacementResponseJSON = "{\"x\": 1, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct22EPlacementResponseJSON = "{\"x\": 2, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct32EPlacementResponseJSON = "{\"x\": 3, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct32NPlacementResponseJSON = "{\"x\": 3, \"y\": 2, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct33NPlacementResponseJSON = "{\"x\": 3, \"y\": 3, \"facing\": \"NORTH\", \"lost\": false}";
	private final String lostRobotResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": null, \"lost\": true}";
	
	@BeforeEach
	public void preTest() {
		robotController.setRobotService(robotService);
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(robotController).build();
	}
	/**
	 * Moving when not placed on the table results in ignoring the move and reporting lost
	 * MOVE REPORT 
	 */
	@Test
	@Order(1)
	public void givenMove_whenNotPlacedOnTable_thenIgnoreMoveAndReportLost() {
		try {
			mockMvc.perform(put("/move").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk());
			mockMvc.perform(get("/report").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(lostRobotResponseJSON));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}

	/**
	 * PLACE 0,0,NORTH MOVE REPORT
	 */
	@Test
	@Order(2)	
	public void givenPlacementAndMoveNorth_whenPlacementCorrect_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place")
					.content(correct00PlacementJSON).contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct00NPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct01NPlacementResponseJSON));
			mockMvc.perform(get("/report").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct01NPlacementResponseJSON));
							
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * PLACE 0,0,NORTH LEFT REPORT
	 * resulting in Output: 0,0,WEST
	 */
	@Test
	@Order(3)
	public void givenPlacementAndTurnLeft_whenPlacementCorrect_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place")
					.content(correct00PlacementJSON)
					.contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct00NPlacementResponseJSON));
			mockMvc.perform(put("/left")
					.contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct00WPlacementResponseJSON));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * PLACE 1,2,EAST MOVE MOVE LEFT MOVE REPORT
	 * resulting in Output: 3,3,NORTH
	 */
	@Test
	@Order(4)
	public void givenPlaceWithMultiMoves_whenPlacementCorrectAndMovesOk_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place")
					.content(correct12EPlacementJSON)
					.contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct12EPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct22EPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct32EPlacementResponseJSON));
			mockMvc.perform(put("/left")
					.contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct32NPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct33NPlacementResponseJSON));
			mockMvc.perform(get("/report").contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct33NPlacementResponseJSON));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * Correctly place the robot at the north west corner
	 * PLACE 4,4,NORTH 
	 */
	@Test
	@Order(5)
	public void givenPlacement_whenPlacementAtTableExtremety_thenValidPositionAtNWCorner() {
		try {
			mockMvc.perform(post("/place")
					.content(correct44NPlacementJSON)
					.contentType("application/json"))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct44NPlacementResponseJSON));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}



	/**
	 * PLACE off the board causing illegal argument exception
	 */
	@Test
	@Order(6)
	public void givenPlacement_whenPlacementOffTheTable_thenExpectIllegalArgumentException() {
		try {
			mockMvc.perform(post("/place").content(incorrectXPlaceJSON).contentType("application/json")).andDo(print())
					.andExpect(status().is4xxClientError());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * PLACE without specifying the direction to cause missing argument exception
	 */
	@Test
	@Order(7)
	public void givenPlacement_whenPlacementMissingDirection_thenExpectException() {
		try {
			mockMvc.perform(post("/place").content(missingDirectionJSON).contentType("application/json")).andDo(print())
					.andExpect(status().is4xxClientError());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}