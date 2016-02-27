package org.team4213.lib14;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CowDash {
	public static double getNum(String key, double fallback) {
		try{
			return SmartDashboard.getNumber(key);
		}catch(Exception e){
			return fallback;
		}
	}
	
	public static String getString(String key, String fallback) {
		try{
			return SmartDashboard.getString(key);
		}catch(Exception e){
			return fallback;
		}
	}
	
	public static boolean getNum(String key, boolean fallback) {
		try{
			return SmartDashboard.getBoolean(key);
		}catch(Exception e){
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
