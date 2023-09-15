package au.com.dovoru.robot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import au.com.dovoru.robot.controller.RobotController;

/**
 * Unit tests for the robot
 * @author stephen
 */

@SpringBootTest(classes = TruckRobotApplication.class)
class TruckRobotControllerWhenPlacedTests {
	MockMvc mockMvc;
	@Autowired
	private RobotController robotController;

	private final String incorrectXPlaceJSON = "{\"x\": -2, \"y\": 0, \"facing\": \"NORTH\"}";
	private final String missingDirectionJSON = "{\"x\": 0, \"y\": 0}";
	private final String correct44NPlacementJSON = "{\"x\": 4, \"y\": 4, \"facing\": \"NORTH\"}";
	private final String correct44NPlacementResponseJSON = "{\"x\": 4, \"y\": 4, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct00PlacementJSON = "{\"x\": 0, \"y\": 0, \"facing\": \"NORTH\"}";
	private final String correct00NPlacementResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct00WPlacementResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": \"WEST\", \"lost\": false}";
	private final String correct01NPlacementResponseJSON = "{\"x\": 0, \"y\": 1, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct12EPlacementJSON = "{\"x\": 1, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct12EPlacementResponseJSON = "{\"x\": 1, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct22EPlacementResponseJSON = "{\"x\": 2, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct32EPlacementResponseJSON = "{\"x\": 3, \"y\": 2, \"facing\": \"EAST\", \"lost\": false}";
	private final String correct32NPlacementResponseJSON = "{\"x\": 3, \"y\": 2, \"facing\": \"NORTH\", \"lost\": false}";
	private final String correct33NPlacementResponseJSON = "{\"x\": 3, \"y\": 3, \"facing\": \"NORTH\", \"lost\": false}";
	
	
	@BeforeEach
	public void preTest() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(robotController).build();
	}


	/**
	 * PLACE 0,0,NORTH MOVE REPORT
	 */
	@Test
	public void givenPlacementAndMoveNorth_whenPlacementCorrect_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place").content(correct00PlacementJSON).contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct00NPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct01NPlacementResponseJSON));
			mockMvc.perform(get("/report").contentType("application/json"))
					//.andDo(print())
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
	public void givenPlacementAndTurnLeft_whenPlacementCorrect_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place").content(correct00PlacementJSON).contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct00NPlacementResponseJSON));
			mockMvc.perform(put("/left").contentType("application/json"))
					//.andDo(print())
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
	public void givenPlaceWithMultiMoves_whenPlacementCorrectAndMovesOk_thenFinalPositionReportedCorrectly() {
		try {
			mockMvc.perform(post("/place").content(correct12EPlacementJSON).contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct12EPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct22EPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct32EPlacementResponseJSON));
			mockMvc.perform(put("/left").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct32NPlacementResponseJSON));
			mockMvc.perform(put("/move").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(correct33NPlacementResponseJSON));
			mockMvc.perform(get("/report").contentType("application/json"))
					//.andDo(print())
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
	public void givenPlacement_whenPlacementAtTableExtremety_thenValidPositionAtNWCorner() {
		try {
			mockMvc.perform(post("/place").content(correct44NPlacementJSON).contentType("application/json"))
					//.andDo(print())
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
	public void givenPlacement_whenPlacementOffTheTable_thenExpectIllegalArgumentException() {
		try {
			mockMvc.perform(post("/place").content(incorrectXPlaceJSON).contentType("application/json"))
					//.andDo(print())
					.andExpect(status().is4xxClientError());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * PLACE without specifying the direction to cause missing argument exception
	 */
	@Test
	public void givenPlacement_whenPlacementMissingDirection_thenExpectException() {
		try {
			mockMvc.perform(post("/place").content(missingDirectionJSON).contentType("application/json"))
					//.andDo(print())
					.andExpect(status().is4xxClientError());
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
	/**
	 * PLACE without specifying the direction to cause missing argument exception
	 */
	@Test
	public void givenNoPlacement_whenGetById_thenExpectException() {
		try {
			mockMvc.perform(get("/1").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}