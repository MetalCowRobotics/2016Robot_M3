package org.team4213.lib14;

import org.json.JSONArray;
import org.json.JSONObject;

public class StorableController {
	double RY;
	double RX;
	double LY;
	double LX;
	int dpadX;
	int dpadY;
	boolean[] currentButtonVals;

	private static final String SAVE_FILE_NAME = "/home/lvuser/dashboardvalues.json";

	public StorableController(CowGamepad controller) {
		RX = controller.getRX();
		RY = controller.getRY();
		LY = controller.getLY();
		LX = controller.getLX();
		currentButtonVals = new boolean[20];
		for (short i = 1; i < currentButtonVals.length; i++) {
			currentButtonVals[i] = controller.getButton(i);
		}
		dpadX = controller.getDpadX();
		dpadY = controller.getDpadY();
	}

	public StorableController(JSONObject obj) {
		RX = obj.getDouble("RX");
		RY = obj.getDouble("RY");
		LX = obj.getDouble("LX");
		LY = obj.getDouble("LY");
		dpadX = obj.getInt("dpadX");
		dpadY = obj.getInt("dpadY");

		currentButtonVals = new boolean[20];
		JSONArray buttonVals = obj.getJSONArray("currentButtonVals;");

		for (short i = 0; i < currentButtonVals.length; i++) {
			currentButtonVals[i] = buttonVals.getBoolean(i);

		}
	}

	public JSONObject serialize() {
		JSONObject obj = new JSONObject();
		obj.put("RX", RX);
		obj.put("RY", RY);
		obj.put("LY", LY);
		obj.put("LX", LX);
		obj.put("dpadX", dpadX);
		obj.put("dpadY", dpadY);
		for (short i = 0; i < currentButtonVals.length; i++) {
			obj.append("currentButtonVals", currentButtonVals[i]);
		}
		return null;
	}

}
