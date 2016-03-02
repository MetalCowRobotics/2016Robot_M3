package org.team4213.lib14;

import edu.wpi.first.wpilibj.Joystick;

public abstract class CowGamepad extends Joystick {

	boolean[] previousStates;
	boolean[] toggleStates;
	
	public CowGamepad(int port) {
		super(port);
		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i=0;i<20;i++){
			previousStates[i] = false;
			toggleStates[i] = false;
		}
	}
	
	public boolean getHeadingPadPressed(){
		return getRawButton(1) || getRawButton(2) || getRawButton(3) || getRawButton(4);
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
	
	public boolean getButtonTripped(int n) {
		if (getRawButton(n) && previousStates[n]==false) {
			previousStates[n]=true;
			return true; //return TRUE this changed 
		} else {
			return false;
		}
	}
	
	public boolean getButtonReleased(int n) {
		if (!getRawButton(n) && previousStates[n]==true) {
			previousStates[n]=false;
			return true; //return TRUE this changed
		} else {
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
	
	/**
	* Determine the top speed threshold
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
}
