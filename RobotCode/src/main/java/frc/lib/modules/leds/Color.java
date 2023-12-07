package frc.lib.modules.leds;

public class Color {

	private boolean hsv;
	
	private int r;
	private int g;
	private int b;

	private int h;
	private int s;
	private int v;

	public Color(int red, int green, int blue) {
		this(red, green, blue, false);
	}

	public Color(int[] color) {
		this(color[0], color[1], color[2], false);
	}

	public Color(int red, int green, int blue, boolean hsv) {
		this.hsv = hsv;
		if (!hsv) {
			this.r = red;
			this.g = green;
			this.b = blue;
		} else {
			this.h = red;
			this.s = green;
			this.v = blue;
		}
	}

	public int[] getList() {
		return hsv ? new int[]{h, s, v} : new int[]{r, g, b};
	}

	public int get(int index) {
		switch(index) {
			case 0: return hsv ? h : r;
			case 1: return hsv ? s : g;
			case 2: return hsv ? v : b;
		}
		return 0; // If index > 2, return 0
	}

	

}
