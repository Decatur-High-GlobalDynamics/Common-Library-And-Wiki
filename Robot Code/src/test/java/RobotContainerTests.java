import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.simulation.DoubleSolenoidSim;
import edu.wpi.first.wpilibj.simulation.PWMSim;
import frc.robot.RobotContainer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import edu.wpi.first.wpilibj2.command.Command;

class RobotContainerTests {

  RobotContainer robotContainer;
  
  @BeforeEach // this method will run before each test
  void setup() {
    robotContainer = new RobotContainer();
  }
  

  @SuppressWarnings("PMD.SignatureDeclareThrowsException")
  @AfterEach // this method will run after each test
  void shutdown() throws Exception {
    robotContainer = null;
  }

  @Test
  void testAutonomousCommandDefaultsToNull() {
    Command command = robotContainer.getAutonomousCommand();
    assertNull(command);
  }

    @Test
  void testAutonomousCommandDefaultsToNotNull() {
    Command command = robotContainer.getAutonomousCommand();
    assertNotNull(command);
  }
}