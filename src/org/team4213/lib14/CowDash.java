package org.team4213.lib14;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CowDash {	
	private static final String savefileName = "/home/lvuser/dashboardvalues.json";
	
	public static void save() {
		NetworkTable table = NetworkTable.getTable("SmartDashboard");
		
		JSONObject jsobj = new JSONObject();
		
		for(String key:table.getKeys()) {
			
			try{
				Object thing = table.getValue(key, null);
				jsobj.put(key, thing);
			}catch(Exception e){
				
			}
		}
		
		try (FileWriter file = new FileWriter(savefileName)) {
			String out = jsobj.toString();
			if (out!=null) {
				file.write(out);
				DriverStation.reportError("\nCowDash.save: Success!\n", false);
			} else {
				DriverStation.reportError("\nCowDash.save: jsobj.toString() returns null\n", true);
			}
		} catch (Exception e){
			DriverStation.reportError("\nCowDash.save: could not write to file\n", true);
		}
	}
	
	public static void load() {
		JSONObject savefile=null;
		try {
			savefile = (JSONObject) new JSONObject( new JSONTokener(new FileReader(savefileName)));
		} catch (FileNotFoundException e) {
			DriverStation.reportError("\nCowDash.load: could not find file\n", true);
			return;
		} catch (JSONException je) {
			DriverStation.reportError("\nCowDash.load: could not parse JSON\n", true);
			return;
		}
		
		for(String key:savefile.keySet()){
			Object val = savefile.get(key);
			if (val instanceof Double) {
				CowDash.setNum(key, ((Double) val).doubleValue());
			} else if (val instanceof Integer) {
				CowDash.setNum(key, ((Integer) val).intValue());
			}else if (val instanceof Boolean) {
				CowDash.setBool(key, ((Boolean) val).booleanValue());
			} else if (val instanceof String) {
				CowDash.setString(key, val.toString());
			}
		}
		DriverStation.reportError("\nCowDash.load: Success!\n", false);
		
	}
	
	public static double getNum(String key, double fallback) {
		try{
			return SmartDashboard.getNumber(key);
		}catch(Exception e){
			SmartDashboard.putNumber(key, fallback);
			return fallback;
		}
	}
	
	public static String getString(String key, String fallback) {
		try{
			return SmartDashboard.getString(key);
		}catch(Exception e){
			SmartDashboard.putString(key, fallback);
			return fallback;
		}
	}
	
	public static boolean getBool(String key, boolean fallback) {
		try{
			return SmartDashboard.getBoolean(key);
		}catch(Exception e){
			SmartDashboard.putBoolean(key, fallback);
			return fallback;
		}
	}
	
	public static boolean setNum(String key, double value){
		try{
			SmartDashboard.putNumber(key, value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean setString(String key, String value){
		try{
			SmartDashboard.putString(key, value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public static boolean setBool(String key, boolean value){
		try{
			SmartDashboard.putBoolean(key, value);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
}
