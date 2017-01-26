package org.usfirst.frc.team4213.lib14;

import java.util.Hashtable;

/**
 * Convenience wrapper for SmartDashboard
 *
 * @author hughest1
 */
public class CowDash {
    protected static Hashtable values = new Hashtable();  
    
    static public void load(){

       //Loading in manually of all DashBoard Settings.
        values.put("Turret_pitchSpeed","0.0375");
        values.put("Shooter_Flywheel::ki","1");
        values.put("Vision_Tracking_Y_Kp1_Med","1.8");
        values.put("Vision_Tracking_Y_Kp1","1.375");
        values.put("Vision_Pitch_Setting","LOW");
        values.put("Vision_Tracking_Y_Kp2","0.55");
        values.put("ChevalDeFriseFire","false");
        values.put("Vision_Tracking_Y_Kp1_Hi","3");
        values.put("Shooter_Flywheel::currentRPS","-15.9821000479463");
        values.put("Vision_Tracking_X_Kp1_Hi","1.5");
        values.put("Vision_Shoot_BR_2","85");
        values.put("AUTONOMOUS_MODE","5");
        values.put("Vision_Shoot_BR_1","100");
        values.put("Vision_Dist_Curve_Mult","0.1");
        values.put("Vision_Sat_Lo","100");
        values.put("Turret_Yaw::kp","100");
        values.put("Vision_Y_Med_Limit","4");
        values.put("Vision_Lum_Lo","115");
        values.put("Shooter_Cam::kp","75");
        values.put("Shooter_Cam::current","-9.682539682539682");
        values.put("Shooter_intakePower","0.75");
        values.put("RockWall","false");
        values.put("Turret_Pitch::current","0.375");
        values.put("Vision_max_ratio","2");
        values.put("Vision_Y_Hi_Limit","10");
        values.put("Vision_X_Med_Limit","4");
        values.put("Turret_Pitch::target","0");
        values.put("Turret Up Speed","-0.0375");
        values.put("Portcullis","false");
        values.put("Turret_Pitch::error","-0.375");
        values.put("LowBar","false");
        values.put("Turret_Pitch::ki","0");
        values.put("Vision_Human_Exposure","50");
        values.put("Turret_Pitch::kd","0");
        values.put("Current_Shooting_Bracket","Bracket 3");
        values.put("ChevalDeFrise","false");
        values.put("Shooter_desiredFlywheelRPS","80");
        values.put("Vision_Offset_5","0");
        values.put("Cam_Shoot_Angle","-35");
        values.put("Vision_Offset_6","-10");
        values.put("SallyPort","false");
        values.put("Moat","false");
        values.put("Shooter_armTime","0.5");
        values.put("Vision_Tracking_X_Kp2_Med","3");
        values.put("Shooter_wheelSpeed","0");
        values.put("RoughTerrain","false");
        values.put("Turret_Pitch::kp","100");
        values.put("Turret_yawSpeed","0.0193359375");
        values.put("FME","NOOOO");
        values.put("Turret_Yaw::ki","0");
        values.put("Turret_Yaw::kd","0");
        values.put("Vision_Yaw_Setting","MED");
        values.put("Shooter_Cam::integralLifespan","1");
        values.put("Desired_angle","77.70964571010438");
        values.put("Turret_state","IDLE");
        values.put("Intake_Lower_Time","3.5");
        values.put("Vision_Offset_Ang_1","115");
        values.put("Vision_Tracking_Y_Kp2_Hi","0.2");
        values.put("Vision_Yaw_Slowdown","0.8");
        values.put("Vision_Offset_Ang_6","70");
        values.put("LowBarFire","false");
        values.put("Vision_Offset_Ang_2","100");
        values.put("Vision_Offset_Ang_3","95");
        values.put("Shooter_shootIntakePower","0.4");
        values.put("Vision_Offset_Ang_4","90");
        values.put("Vision_Dist_Curve_Offset","200");
        values.put("Vision_Offset_Ang_5","85");
        values.put("Shooter_ejectPower","-1");
        values.put("Intake_Rise_Time","3.5");
        values.put("Vision_min_ratio","1");
        values.put("Min Rect Area","500");
        values.put("Turret_Yaw::current","-0.193359375");
        values.put("Rampart","false");
        values.put("Vision_target_y","100");
        values.put("Vision_target_x","111");
        values.put("Shooter_armIntakeTime","0.5");
        values.put("Cam_Eject_Angle","-30");
        values.put("ITSWORKING","false");
        values.put("Vision_Hue_Lo","50");
        values.put("PortcullisFire","false");
        values.put("Vision_Sat_Hi","255");
        values.put("SallyPortFire","false");
        values.put("Vision_Lum_Hi","255");
        values.put("Vision_Tracking_X_Kp2","0.8");
        values.put("Vision_Tracking_Y_Kp2_Med","0.4");
        values.put("Vision_Tracking_X_Kp1","1.05");
        values.put("Shooter_Flywheel_RPS::","0");
        values.put("Shooter_Cam::error","-0.3174603174603181");
        values.put("Vision_Tracking_Exposure","30");
        values.put("Vision_Crosshair_Rad","5");
        values.put("Cam_Arm_Angle","10");
        values.put("Cam_Idle_Angle","-10");
        values.put("Vision_Offset_3","-34");
        values.put("Vision_Offset_4","-4");
        values.put("Vision_Offset_1","-80");
        values.put("Vision_Offset_2","-50");
        values.put("Vision_Tracking_X_Kp2_Hi","1");
        values.put("Shooter_RPSErrorTolerance","1.5");
        values.put("Drive_Ramp_Rate","0.03");
        values.put("Turret_Yaw::target","0");
        values.put("Turret_Yaw::integralLifespan","1");
        values.put("Turret_Pitch::integralLifespan","1");
        values.put("Shooter_Cam::target","-10");
        values.put("Vision_debug","false");
        values.put("Vision_Crosshair_X","3");
        values.put("RampartFire","false");
        values.put("Vision_Crosshair_Y","-5");
        values.put("Cam_Intake_Angle","-30");
        values.put("Vision_X_Hi_Limit","15");
        values.put("Vision_Tracking_X_Kp1_Med","1.16");
        values.put("RoughTerrainFire","false");
        values.put("Shooter_state","IDLE");
        values.put("Deg_Per_PX","0.1");
        values.put("Turret_Yaw::error","0.193359375");
        values.put("Vision_Shoot_Speed_BR_2","90");
        values.put("Vision_Shoot_Speed_BR_1","74");
        values.put("MoatFire","false");
        values.put("screw","yay");
        values.put("Shooter_Cam::ki","0");
        values.put("Vision_Hue_Hi","100");
        values.put("Shooter_Flywheel::currentPWM","1");
        values.put("Shooter_Cam::kd","1.2");
        values.put("Distance_From_Target","-47.479636889370695");
        values.put("RockWallFire","false");
        values.put("Shooter_camSpeed","-0.02380952380952386");
        values.put("Vision_Pitch_Slowdown","0.7");


       
    }
    
    
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public double getNum(String key, double fallback) {
        try{
            return ((Double)values.get(key)).doubleValue();
        }catch(Exception e) {
            CowDash.setNum(key, fallback);
            return fallback;
        }
    }
    
    static public void setNum(String key, double value) {
        values.put(key, Double.valueOf(value));
    }
    
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public boolean getBool(String key, boolean fallback) {
        try{
            return ((Boolean)values.get(key)).booleanValue();
        }catch(Exception e) {
            values.put(key, Boolean.valueOf(fallback));
            return fallback;
        }
    }
    
    static public void setBool(String key, boolean value) {
        values.put(key, Boolean.valueOf(value));
    }
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public String getString(String key, String fallback) {
        try{
            return ((String)values.get(key));
        }catch(Exception e) {
            values.put(key, fallback);
            return fallback;
        }
    }
    
    static public void setString(String key, String value) {
        values.put(key, value);
    }
}
