package org.team4213.lib14;

public abstract class CowGamepad extends edu.wpi.first.wpilibj.Joystick {

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
	
	public abstract boolean getButton(int n);
	public abstract double getLY();
	public abstract double getLX();
	public abstract double getRY();
	public abstract double getRX();
	public abstract double getDPADX();
	public abstract double getDPADY();
	
	public double getTankY(){
		return (getLY()+getRY())/2;
	}
	
	public double getTankOmega(){
		return (getLX()-getRY())/2;
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
        if(getButton(GamepadButton.RT) || getButton(GamepadButton.LT)) return topSpeedCrawl; //front-bottom triggers
        else if(getButton(GamepadButton.RB) || getButton(GamepadButton.LB)) return topSpeedSprint; //fromt-bumper buttons
        else return topSpeedNormal;
    }
	public boolean getButtonTripped(int n) {
		if (getButton(n)) {
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
		if (!getButton(n)) {
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
		if (!getButton(n)) {
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
