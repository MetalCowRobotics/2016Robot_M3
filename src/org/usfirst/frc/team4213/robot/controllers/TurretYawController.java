package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.systems.TurretYawMap;
public class TurretYawController {
	TurretYawMap turretYaw; 
	
	
	public TurretYawController(TurretYawMap turretYawMap){
		turretYaw = turretYawMap;
	}
	
	private void turretYawMove(double speed){
		turretYaw.YAW_MOTOR.set(speed);
	}
	
	private void yawClockWise(){
		turretYawMove(turretYaw.CLOCK_WISE_SPEED);
	}
	
	public void yawCounterClockWise(){
		turretYawMove(turretYaw.COUNTER_CLOCK_WISE_SPEED);
	}
	
}
