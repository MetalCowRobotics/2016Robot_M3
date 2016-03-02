

package org.team4213.lib14;

/**
 * Wrapper/convenience class for the Xbox360 gamepads that MetalCow has
 * 
 * Drivers through here http://wccftech.com/xbox-controller-pc-drivers-download-courtesy-major-nelson/ to here: mjr.mn/XboxOnePCDriversx64
 */

public class Xbox360Controller extends CowGamepad {
	
	public Xbox360Controller(int port) {
		super(port);
	}
    
    public double getLT(){
    	return getRawAxis(2);
    }
    public double getRT(){
    	return getRawAxis(3);
    }

	public boolean getButton(int n) {
		switch(n){
		case GamepadButton.DPADUP:
			return getDpadY()>0;
		case GamepadButton.DPADDOWN:
			return getDpadY()<0;
		case GamepadButton.DPADLEFT:
			return getDpadX()<0;
		case GamepadButton.DPADRIGHT:
			return getDpadX()>0;
		case GamepadButton.RT:
			return getRT()>0.2;
		case GamepadButton.LT:
			return getLT()>0.2;
		case GamepadButton.BACK:
			return getRawButton(7);
		case GamepadButton.START:
			return getRawButton(8);
		default:		
			return getRawButton(n);
		}
	}

	@Override
	public double getLY() {
		return -getRawAxis(1);
	}

	@Override
	public double getLX() {
		return getRawAxis(0);
	}

	@Override
	public double getRY() {
		return -getRawAxis(5);
	}

	@Override
	public double getRX() {
		return getRawAxis(4);
	}
	
	public void rumbleLeft(float amt){
		this.setRumble(RumbleType.kLeftRumble, amt);
	}
	public void rumbleRight(float amt){
		this.setRumble(RumbleType.kRightRumble, amt);
	}
}