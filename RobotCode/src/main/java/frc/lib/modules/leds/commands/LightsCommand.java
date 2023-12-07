package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.LEDSubsystem;

public class LightsCommand extends CommandBase {
	
	private LEDSubsystem leds;
	private static int position = 0;
	// ^ THIS NEEDS TO BE STATIC

	public LightsCommand(LEDSubsystem ledSubsystem) {
		this.leds = ledSubsystem;
	}

	public void execute() {
		position++;
		if (position > 360) position = 0;
		for (int i = 0; i < leds.getLength(); i++)
			leds.setPixelHSV(i, position + i * 5, 255, 255);
		leds.updateData();
	}

	public void end(boolean interrupted) {
		leds.setAllPixels(255, 0, 0);
	}

}
