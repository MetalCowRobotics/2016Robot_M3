/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * Wrapper/convenience class for the Xbox360 gamepads that MetalCow has
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
			return getDPADY()>0;
		case GamepadButton.DPADDOWN:
			return getDPADY()<0;
		case GamepadButton.DPADLEFT:
			return getDPADX()<0;
		case GamepadButton.DPADRIGHT:
			return getDPADX()>0;
		case GamepadButton.RT:
			return getRT()>0.2;
		case GamepadButton.LT:
			return getLT()>0.2;
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

	@Override
	public double getDPADX() {
		// TODO xbox.dpadx
		return 0;
	}

	@Override
	public double getDPADY() {
		// TODO xbox.dpady
		return 0;
	}
}