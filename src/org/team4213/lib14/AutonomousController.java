package org.team4213.lib14;

import edu.wpi.first.wpilibj.Joystick;

public class AutonomousController extends Joystick {
	
	boolean[] previousStates;
	boolean[] toggleStates;
	
	public AutonomousController(int port) {
		super(port);
		previousStates = new boolean[20];
		toggleStates = new boolean[20];
		for (short i=0;i<20;i++){
			previousStates[i] = false;
			toggleStates[i] = false;
		}
	}
	public double getLY(){
		return 0.5;
	}
	
	public double getLX(){
		return 0.5;
	}
	
	public double getRY() {
		return 0.5;
	}
	public double getRX(){
		return 0.5;
	}
	
	public boolean getHeadingPadPressed(){
		return true || false || true || false;
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
