package org.team4213.lib14;

import java.io.Serializable;

import edu.wpi.first.wpilibj.Joystick;

public abstract class CowGamepad extends Joystick implements Serializable, CowInputController {

	boolean[] previousStates;
	boolean[] toggleStates;

	public CowGamepad(int port) {
		super(port);
		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i = 0; i < 20; i++) {
			previousStates[i] = false;
			toggleStates[i] = false;
		}
	}

	@Override
	public abstract boolean getButton(int n);

	@Override
	public abstract double getLY();

	@Override
	public abstract double getLX();

	@Override
	public abstract double getRY();

	@Override
	public abstract double getRX();

	@Override
	public int getDpadX() {
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

	@Override
	public int getDpadY() {
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
	@Override
	public double getThrottle(double topSpeedNormal, double topSpeedCrawl, double topSpeedSprint) {
		if (getButton(GamepadButton.RT) || getButton(GamepadButton.LT))
			return topSpeedCrawl; // front-bottom triggers
		else if (getButton(GamepadButton.RB) || getButton(GamepadButton.LB))
			return topSpeedSprint; // fromt-bumper buttons
		else
			return topSpeedNormal;
	}

	@Override
	public void prestep() {
		this.rumbleLeft(0);
		this.rumbleRight(0);
	}

	@Override
	public void endstep() {
		for (int i = 1; i < previousStates.length; i++) {
			try {
				previousStates[i] = getButton(i);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public boolean getButtonTripped(int n) {
		if (getButton(n)) {
			if (previousStates[n]) {
				return false;
			} else {
				return true;
			}

		} else {
			return false;
		}
	}

	@Override
	public boolean getButtonReleased(int n) {
		if (!getButton(n)) {
			if (previousStates[n]) {
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}
	}

	@Override
	public boolean getButtonToggled(int n) {
		if (!getButton(n)) {
		} else if (previousStates[n]) {
		} else {
			toggleStates[n] = !toggleStates[n];
		}
		return toggleStates[n];
	}

	// IDEA: Timeout on the rumble
	public void rumbleLeft(float amt) {
	}

	public void rumbleRight(float amt) {
	}
}
