package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ElevatorSubsystem;
import java.util.function.DoubleSupplier;

public class MoveElevatorCommand extends CommandBase
{

	public ElevatorSubsystem elevator;
	public DoubleSupplier input;

	public MoveElevatorCommand(DoubleSupplier in, ElevatorSubsystem elev)
	{
		elevator = elev;
		input = in;
	}

	public void execute()
	{
		System.out.println("Executing move elevator... Input: " + input.getAsDouble());
		if (elevator.targetOverridden)
			elevator.setSpeed(Math.abs(input.getAsDouble()) > .05 ? input.getAsDouble() : 0);
	}

}