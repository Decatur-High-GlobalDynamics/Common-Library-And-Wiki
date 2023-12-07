package frc.lib.modules.leds.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.modules.leds.LEDSubsystem;

public class FlashLightsCommand extends CommandBase {
	
	private LEDSubsystem ledSubsystem;
	public static double progress = 0;

	public FlashLightsCommand(LEDSubsystem ledSubsystem) {
		this.ledSubsystem = ledSubsystem;
	}

	public void execute() {
        progress = 1;
        ledSubsystem.setAllPixels(0, 0, 255);
	}

	public void end(boolean interrupted) {
        
	}

}
