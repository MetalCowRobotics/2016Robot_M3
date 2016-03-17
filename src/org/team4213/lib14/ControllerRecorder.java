package org.team4213.lib14;

import java.io.FileWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.usfirst.frc.team4213.robot.systems.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;

public class ControllerRecorder {
	public JSONArray[] inputs;
	private CowGamepad[] controller;

	public ControllerRecorder(CowGamepad... controllers) {
		inputs = new JSONArray[controllers.length];
		for (short i = 0; i < controller.length; i++) {
			inputs[i] = new JSONArray();
		}

		this.controller = controllers;
	}

	public void record() {
		for (short i = 0; i < controller.length; i++) {
			inputs[i].put(new StorableController(controller[i]).serialize());
		}
	}

	public void save() {
		try (FileWriter file = new FileWriter(RobotMap.AUTONOMOUS_FILE)) {
			JSONObject obj = new JSONObject();
			for (short i = 0; i < controller.length; i++) {
				obj.append("" + i, inputs[i]);
			}
			String out = inputs.toString();
			if (out != null) {
				file.write(out);
				DriverStation.reportError("\nCowDash.save: Success!\n", false);
			} else {
				DriverStation.reportError("\nCowDash.save: jsobj.toString() returns null\n", true);
			}
		} catch (Exception e) {
			DriverStation.reportError("\nCowDash.save: could not write to file\n", true);
		}
	}
}
