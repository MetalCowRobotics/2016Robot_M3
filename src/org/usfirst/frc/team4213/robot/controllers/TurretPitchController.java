package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.systems.TurretPitchMap;

public class TurretPitchController {
TurretPitchMap turretPitch; 
	
	
	public TurretPitchController(TurretPitchMap turretPitchMap){
		turretPitch = turretPitchMap;
	}
	
	private void turretPitchMove(double speed){
		turretPitch.PITCH_MOTOR.set(speed);
	}
	
	private void pitchUp(){
		turretPitchMove(turretPitch.UP_SPEED);
	}
	
	public void pitchDown(){
		turretPitchMove(turretPitch.DOWN_SPEED);
	}
	
}
