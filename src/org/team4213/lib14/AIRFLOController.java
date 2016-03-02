/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

public class AIRFLOController extends CowGamepad {
	
	public AIRFLOController (int port) {
		super(port);
	}
	
	public double getLY(){
		return -getRawAxis(1);
	}
	
	public double getLX(){
		return getRawAxis(0);
	}
	
	public double getRY() {
		return -getRawAxis(2);
	}
	public double getRX(){
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
		default:		
			return getRawButton(n);
		}
	}
}