package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    
    // Configure the button bindings
    configurePrimaryBindings();
    configureSecondaryBindings();
  }

  private void configurePrimaryBindings() {
    
  }

  private void configureSecondaryBindings() {

  }

  public Command getAutonomousCommand() {
    return null;
  }
}
