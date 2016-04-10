package org.team4213.lib14;

import edu.wpi.first.wpilibj.Joystick;

public abstract class CowGamepad extends Joystick {

	public CowGamepad(int port) {
		super(port);
	}

	public abstract boolean getButton(int n);

	public abstract double getLY();

	public abstract double getLX();

	public abstract double getRY();

	public abstract double getRX();

	public double getDpadX() {
		switch (getPOV()) {
		case -1:
			return 0;
		case 0:
		case 360:
			return 0;
		case 45:
		case 90:
		case 135:
			return 1;
		case 180:
			return 0;
		case 225:
		case 270:
		case 315:
			return -1;
		}
		return 0;
	}

	public double getDpadY() {
		switch (getPOV()) {
		case -1:
			return 0;
		case 315:
		case 0:
		case 45:
			return 1;
		case 90:
			return 0;
		case 135:
		case 180:
		case 225:
			return -1;
		case 270:
			return 0;
		}
		return 0;
	}

	public double getTankY() {
		return (getLY() + getRY()) / 2;
	}

	public double getTankOmega() {
		return (getLX() - getRY()) / 2;
	}

	/**
	 * Determine the top speed threshold Bumper buttons on the controller will
	 * limit the speed to the CRAWL value Trigger buttons on the controller will
	 * limit the speed to the SPRINT value Otherwise it will allow the robot a
	 * speed up to Normal max.
	 *
	 * @param topSpeedNormal
	 *            value double 0 to 1
	 * @param topSpeedCrawl
	 *            value double 0 to 1
	 * @param topSpeedSprint
	 *            value double 0 to 1
	 * @return topSpeedCurrent value double 0 to 1
	 */
	public double getThrottle(double topSpeedNormal, double topSpeedCrawl, double topSpeedSprint) {
		if (getButton(GamepadButton.RT) /* || getButton(GamepadButton.LT)*/)
			return topSpeedCrawl; // front-bottom triggers
		else if (getButton(GamepadButton.RB) /*|| getButton(GamepadButton.LB)*/)
			return topSpeedSprint; // fromt-bumper buttons
		else
			return topSpeedNormal;
	}

	// IDEA: Timeout on the rumble
	public void rumbleLeft(float amt) {
	}

	public void rumbleRight(float amt) {
	}
}
