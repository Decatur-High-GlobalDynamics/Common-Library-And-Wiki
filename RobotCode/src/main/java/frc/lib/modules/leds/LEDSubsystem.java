package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;


public class LEDSubsystem extends SubsystemBase {

	private AddressableLED led;
	private AddressableLEDBuffer buffer;

	private static final int port = 0; // The PWM port of the LED strip. Setting to 0 for now.
	private int length; // How many "pixels" are on the strip, I think that the one we have on the showbot has 300

	public LEDSubsystem(int pixels) {

		this.length = pixels;

		buffer = new AddressableLEDBuffer(pixels);

		led = new AddressableLED(port);
		led.setLength(pixels);
		led.setData(buffer);
		led.start();

	}

	public void setAllPixels(int r, int g, int b) {
		for (int i = 0; i < length; i++)
			buffer.setRGB(i, r, g, b);
			// This is better than using the setPixel method in the same class because it's not setting the data every time.
		this.updateData();
	}

	private static int[] calcBlending(int r1, int g1, int b1, int r2, int g2, int b2, double currentFade) {
		int r = (int) (r1 * currentFade / 1 + r2 * (1 - currentFade) / currentFade);
		int g = (int) (g1 * currentFade / 1 + g2 * (1 - currentFade) / currentFade);
		int b = (int) (b1 * currentFade / 1 + b2 * (1 - currentFade) / currentFade);
		return new int[] {r, g, b};
	}

	public void setPixelRGB(int pixelToSet, int r, int g, int b) {
		buffer.setRGB(pixelToSet, r, g, b);
		// THIS METHOD DOES NOT UPDATE THE BUFFER'S DATA! THAT NEEDS TO BE DONE MANUALLY
	}

	public void setPixelHSV(int pixelToSet, int h, int s, int v) {
		buffer.setHSV(pixelToSet, h, s, v);
		// THIS METHOD DOES NOT UPDATE THE BUFFER'S DATA! THAT NEEDS TO BE DONE MANUALLY
	}

	public void updateData() {
		led.setData(buffer);
		System.out.println("Updated LED Data.");
	}

	public int getLength() {
		return length;
	}

}
