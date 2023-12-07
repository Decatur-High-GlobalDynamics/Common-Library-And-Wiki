package frc.lib.modules.leds.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.lib.modules.leds.LEDSubsystem;

import java.util.Random;
import java.lang.Thread;

import frc.lib.modules.leds.commands.FlashLightsCommand;

public class FlashLightsOffCommand extends CommandBase {
	
	private LEDSubsystem ledSubsystem;

	public FlashLightsOffCommand(LEDSubsystem ledSubsystem) {
		this.ledSubsystem = ledSubsystem;
	}

	public void execute() {
		if (FlashLightsCommand.progress <= 0) return;
        FlashLightsCommand.progress-=0.02;
		ledSubsystem.setAllPixels(LEDSubsystem.calcBlending(0, 0, 255, 255, 0, 0, FlashLightsCommand.progress));
	}

	public void end(boolean interrupted) {
		
	}

}
