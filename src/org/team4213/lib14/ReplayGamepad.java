package org.team4213.lib14;

public class ReplayGamepad implements CowInputGamepad {

	StorableController[] inputs;
	int counter;

	boolean[] previousStates;
	boolean[] toggleStates;

	public ReplayGamepad(StorableController[] storedInputs) {
		counter = 0;
		inputs = storedInputs;

		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i = 0; i < 20; i++) {
			previousStates[i] = false;
			toggleStates[i] = false;
		}
	}

	@Override
	public double getRX() {
		return inputs[counter].RX;
	}

	@Override
	public double getRY() {
		return inputs[counter].RY;
	}

	@Override
	public double getLX() {
		return inputs[counter].LX;
	}

	@Override
	public double getLY() {
		return inputs[counter].LY;
	}

	@Override
	public boolean getButton(int n) {
		return inputs[counter].currentButtonVals[n];
	}

	@Override
	public int getDpadY() {
		return inputs[counter].dpadY;
	}

	@Override
	public int getDpadX() {
		return inputs[counter].dpadX;
	}

	@Override
	public double getThrottle(double topSpeedNormal, double topSpeedCrawl, double topSpeedSprint) {
		if (getButton(GamepadButton.RT) || getButton(GamepadButton.LT)) {
			return topSpeedCrawl; // front-bottom triggers
		} else if (getButton(GamepadButton.RB) || getButton(GamepadButton.LB)) {
			return topSpeedSprint; // fromt-bumper buttons
		} else {
			return topSpeedNormal;
		}
	}

	@Override
	public void prestep() {
		// NONE ?
	}

	@Override
	public void endstep() {

		for (int i = 1; i < previousStates.length; i++) {
			try {
				previousStates[i] = getButton(i);
			} catch (Exception e) {

			}
		}

		if (counter < inputs.length) {
			counter++;
		}
	}

	public void resetAuton() {
		counter = 0;
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

}
