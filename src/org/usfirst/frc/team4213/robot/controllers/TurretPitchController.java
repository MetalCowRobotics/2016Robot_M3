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
	
	public void setMinHeight(){
		double error = turretPitch.MIN_HEIGHT - turretPitch.getEncoder();
		turretPitch.UP_SPEED = Math.abs(error/100);
	}
	
	public void heightMax(){
		if(turretPitch.MAX_HEIGHT <= turretPitch.getEncoder()){
			turretPitch.UP_SPEED = 0;
		}
		else{
			
		}
	}
	//TODO make if yaw is not zeroed dont allow turret to go less than min height
}
