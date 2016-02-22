/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * Wrapper/convenience class for the Xbox360 gamepads that MetalCow has
 */
import edu.wpi.first.wpilibj.Joystick;
import java.lang.Math;


public class Xbox360Controller extends Joystick {


	boolean[] previousStates;
	boolean[] toggleStates;
	
	public static final int		XBOX_AXIS_LX			= 0;
	
	public static final int		XBOX_AXIS_LY			= 1;
	/**
	 * The primary Xbox 360 controller left trigger. Values range from 0.0 to
	 * 1.0.
	 */
	public static final int		XBOX_AXIS_LTRIGGER		= 2;
	/**
	 * The primary Xbox 360 controller right trigger. Values range from 0.0 to
	 * 1.0.
	 */
	public static final int		XBOX_AXIS_RTRIGGER		= 3;
	/**
	 * The primaryXbox 360 controller right X-axis.
	 */
	public static final int		XBOX_AXIS_RX			= 4;
	/**
	 * The primary Xbox 360 controller right Y-axis.
	 */
	public static final int		XBOX_AXIS_RY			= 5;
	

	/**
	 * The driver Xbox 360 controller A (south) button. See {@link http
	 * ://www.chiefdelphi.com/forums/showpost.php?p=1003245&postcount=8}.
	 */
	public static final int		XBOX_BTN_A				= 1;
	/**
	 * The primary Xbox 360 controller B (east) button.
	 */
	public static final int		XBOX_BTN_B				= 2;
	/**
	 * The primary Xbox 360 controller X (west) button.
	 */
	public static final int		XBOX_BTN_X				= 3;
	/**
	 * The primary Xbox 360 controller Y (north) button.
	 */
	public static final int		XBOX_BTN_Y				= 4;
	/**
	 * The primary Xbox 360 controller left bumper.
	 */
	public static final int		XBOX_BTN_LBUMP			= 5;
	/**
	 * The primary Xbox 360 controller right bumper.
	 */
	public static final int		XBOX_BTN_RBUMP			= 6;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer back button.
	 */
	public static final int		XBOX_BTN_BACK			= 7;
	/**
	 * The primary Xbox 360 controller and Guitar Hero X-plorer start button.
	 */
	public static final int		XBOX_BTN_START			= 8;
	/**
	 * The primary Xbox 360 controller left joystick (pressed).
	 */
	public static final int		XBOX_BTN_LSTICK			= 9;
	/**
	 * The primary Xbox 360 controller right joystick (pressed).
	 */
	public static final int		XBOX_BTN_RSTICK			= 10;
	
	public Xbox360Controller(int port) {
		super(port);
		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i=0;i<20;i++){
			previousStates[i] = false;
			toggleStates[i] = false;
		}	
	}
	
	public double getLY(){
		return getRawAxis(XBOX_AXIS_LY);
	}
	
	public double getLX(){
		return -getRawAxis(XBOX_AXIS_LX);
	}
	
	public double getRY() {
		return -getRawAxis(XBOX_AXIS_RY);
	}
	public double getRX(){
		return getRawAxis(XBOX_AXIS_RX);
	}
	public double getRT() {
		return -getRawAxis(XBOX_AXIS_RTRIGGER);
	}
	public double getLT(){
		return getRawAxis(XBOX_AXIS_LTRIGGER);
	}
	
	public boolean getHeadingPadPressed(){
		return getRawButton(1) || getRawButton(2) || getRawButton(3) || getRawButton(4);
	}
	
	
	
	/**
	* Determine the top speed threshold
	* TODO: This needs to be moved to it an implementation of AIRLFOContorller, not here directly
	* Bumper buttons on the controller will limit the speed to the CRAWL value
	* Trigger buttons on the controller will limit the speed to the SPRINT value
	* Otherwise it will allow the robot a speed up to Normal max.
	*
	* @param topSpeedNormal value double 0 to 1
	* @param topSpeedCrawl value double 0 to 1
	* @param topSpeedSprint  value double 0 to 1
	* @return topSpeedCurrent value double 0 to 1
	*/
    public double getThrottle(double topSpeedNormal, double topSpeedCrawl, double topSpeedSprint) {
        if(getRawButton(8) || getRawButton(7)) return topSpeedCrawl; //front-bottom triggers
        else if(getRawButton(6) || getRawButton(9)) return topSpeedSprint; //fromt-bumper buttons
        else return topSpeedNormal;
    }
	
	public double getHeadingPadDirection(){
		float x=0, y=0;
		if (getRawButton(1)) y-=1;
		if (getRawButton(2)) x+=1;
		if (getRawButton(3)) x-=1;
		if (getRawButton(4)) y+=1;
		return Math.toDegrees(Math.atan2(x, y));
	}
        
        public String getHeadingPadCardinal(){
            if (getRawButton(1)) return "south";
            if (getRawButton(2)) return "east";
            if (getRawButton(3)) return "west";
            if (getRawButton(4)) return "north";
            return null;
        }
	
	public boolean getButton(int n) {
		//previousStates[n] = getRawButton(n);
		//return previousStates[n];
		return getRawButton(n);
	}
	public int getPOV(int n){
		return getPOV(n);
	}
	
	public boolean getButtonTripped(int n) {
		if (getRawButton(n)) {
			if (previousStates[n]){
				previousStates[n] = true;
				return false;
			} else {
				previousStates[n] = true;
				return true;
			}
			
		} else {
			previousStates[n] = false;
			return false;
		}
	}
	
	public boolean getButtonReleased(int n) {
		if (!getRawButton(n)) {
			if (previousStates[n]){
				previousStates[n] = false;
				return true;
			} else {
				previousStates[n] = false;
				return false;
			}
			
		} else {
			previousStates[n] = true;
			return false;
		}
	}
	
	public boolean getButtonToggled(int n) {
		if (!getRawButton(n)) {
			previousStates[n] = false;
		}else if(previousStates[n]){
			previousStates[n] = true;
		}else{
			previousStates[n] = true;
			toggleStates[n] = !toggleStates[n];
		}
		return toggleStates[n];
	}
}