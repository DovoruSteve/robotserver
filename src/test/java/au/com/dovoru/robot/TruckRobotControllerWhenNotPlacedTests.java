package au.com.dovoru.robot;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import au.com.dovoru.robot.controller.RobotController;
import au.com.dovoru.robot.model.Robot;
import au.com.dovoru.robot.repository.RobotRepository;

@SpringBootTest(classes = TruckRobotApplication.class)
public class TruckRobotControllerWhenNotPlacedTests {
	private final String lostRobotResponseJSON = "{\"x\": 0, \"y\": 0, \"facing\": null, \"lost\": true}";

	MockMvc mockMvc;
	@Autowired
	private RobotController robotController;
	
	@BeforeEach
	public void preTest() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(robotController).build();
	}

	/**
	 * Moving when not placed on the table results in ignoring the move and reporting lost
	 * MOVE REPORT 
	 */
	@MockBean
	RobotRepository robotRepository;
	@Test
	public void givenReportAfterMove_whenNotPlacedOnTable_thenIgnoreMoveAndReportLost() {
		Robot robot = Robot.getInstance().setX(0).setY(0).setFacing(null).setLost(true);
		Mockito.doReturn(robot).when(robotRepository).find();
		try {
			mockMvc.perform(put("/move").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk());
			mockMvc.perform(get("/report").contentType("application/json"))
					//.andDo(print())
					.andExpect(status().isOk())
					.andExpect(content().json(lostRobotResponseJSON));
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		}
	}
}
