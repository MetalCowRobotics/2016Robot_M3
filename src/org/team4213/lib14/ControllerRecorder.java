package org.team4213.lib14;

import java.io.FileWriter;

import org.json.JSONArray;
import org.usfirst.frc.team4213.robot.systems.RobotMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class ControllerRecorder {
	public JSONArray inputs;
	private Timer timer;
	private CowGamepad controller;

	public ControllerRecorder(CowGamepad controller) {
		inputs = new JSONArray();
		this.controller = controller;
	}

	public void record() {
		inputs.put(new StorableController(controller).serialize());
	}

	public void save() {
		try (FileWriter file = new FileWriter(RobotMap.AUTONOMOUS_FILE)) {
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
