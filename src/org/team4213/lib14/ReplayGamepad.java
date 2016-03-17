package org.team4213.lib14;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.usfirst.frc.team4213.robot.systems.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;

public class ReplayGamepad implements CowInputGamepad {

	StorableController[] inputs;
	int counter;

	boolean[] previousStates;
	boolean[] toggleStates;

	public ReplayGamepad(short n) {
		counter = 0;

		JSONArray savefile = null;
		try {
			savefile = new JSONObject(new JSONTokener(new FileReader(RobotMap.AUTONOMOUS_FILE))).getJSONArray("" + n);
		} catch (FileNotFoundException e) {
			DriverStation.reportError("\nCowDash.load: could not find file for autonomous\n", true);
			return;
		} catch (JSONException je) {
			DriverStation.reportError("\nCowDash.load: could not parse JSON\n", true);
			return;
		}
		inputs = new StorableController[savefile.length()];
		for (int i = 0; i < savefile.length(); i++) {
			inputs[i] = new StorableController(savefile.getJSONObject(i));
		}
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
