package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.core.motors.TeamSparkMAX;
import frc.robot.Ports;
import frc.robot.RobotContainer;

public class ClawIntakeSubsystem extends SubsystemBase
{

	public Compressor mainCompressor;

	public DoubleSolenoid clawGrabberLeft, clawGrabberRight;

	public TeamSparkMAX intakeMotor;

	public boolean open;
	boolean closed;

	public ClawIntakeSubsystem()
	{
		mainCompressor = new Compressor(Ports.PNEUMATICS_HUB, PneumaticsModuleType.REVPH);
		clawGrabberLeft = new DoubleSolenoid(Ports.PNEUMATICS_HUB, PneumaticsModuleType.REVPH, Ports.CLAW_LEFT_CLOSE, Ports.CLAW_LEFT_OPEN);
		clawGrabberRight = new DoubleSolenoid(Ports.PNEUMATICS_HUB, PneumaticsModuleType.REVPH,
				Ports.CLAW_RIGHT_CLOSE, Ports.CLAW_RIGHT_OPEN);
		clawGrabberLeft.set(Value.kOff);
		clawGrabberRight.set(Value.kOff);

		// intakeMotor = new TeamSparkMAX("Subsystems.ClawIntake.IntakeMotor", Ports.INTAKE_MOTOR);

		// intakeMotor.enableVoltageCompensation(12);

		mainCompressor.enableDigital();
		// mainCompressor.enableAnalog(100, 115);
		RobotContainer.shuffleboard.addDouble("Pressure", () -> mainCompressor.getPressure());
		RobotContainer.shuffleboard.addBoolean("Grabber Closed (new)", () -> closed);
	}

	public void periodic()
	{
		closed = clawGrabberLeft.get() == Value.kForward
				&& clawGrabberRight.get() == Value.kForward;

	}

	public void setSolenoid(Value mode)
	{
		clawGrabberLeft.set(mode);
		clawGrabberRight.set(mode);
		System.out.println("Setting solenoids to " + mode);
	}
}