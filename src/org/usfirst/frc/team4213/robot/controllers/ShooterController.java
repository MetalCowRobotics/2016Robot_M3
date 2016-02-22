package org.usfirst.frc.team4213.robot.controllers;

import java.util.Calendar;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;

public class ShooterController {
	private ShooterMap shooter;
	private int desiredCamAngle;
	public ShooterState state;
	private long armTime;
	private long shootTime;

	public ShooterController(ShooterMap shooter) {
		this.shooter = shooter;
		state = ShooterState.IDLE;
		//shooter.camMotor.enableBrakeMode(true); // DOES THIS WORK ???? 
		desiredCamAngle = 0;
		shooter.resetEnc();
		shooter.camEncoder.setDistancePerPulse(1 / Shooter.COUNT_PER_DEG);
	}

	public enum ShooterState {
		INTAKE, IDLE, ARMING, ARMED, SHOOTING;
	}

	public void arm() {
		shooter.setWheelSpeed(Shooter.SHOOT_SPEED);
		armTime = getCurrentTimeMS();
		state = ShooterState.ARMING;
	}

	public void intake() {
		shooter.setWheelSpeed(Shooter.INTAKE_SPEED);
		state = ShooterState.INTAKE;
	}

	public void shoot() {
		desiredCamAngle = 360;
		shootTime = getCurrentTimeMS();
		state = ShooterState.SHOOTING;
	}
	public void idle(){
		shooter.setCamSpeed(0);
		shooter.setWheelSpeed(0);
		state = ShooterState.IDLE;
	}
	
	public void lowerCam(){
		desiredCamAngle = 120; // ADD TO CONFIG
		
	}
	
	private long getCurrentTimeMS(){
		return  Calendar.getInstance().get(Calendar.MILLISECOND);
	}
	
	private void runCamPID(){
		double error = desiredCamAngle - shooter.getEncDist();
		shooter.setCamSpeed(error / 180);
	}
	
	public void step(){
		switch(state){
		case SHOOTING:
			if(Math.abs(desiredCamAngle - shooter.getEncDist()) < 3 && getCurrentTimeMS() - shootTime >  1*1000 ){ // 1 Second of Wheel Spinning. ( ADD TO CONFIG )
				shooter.resetEnc();
				idle();
				desiredCamAngle = 0;
			}
			break;
		case ARMING:
			if(getCurrentTimeMS()-armTime > 2*1000){ // 2 Seconds, Can be changed ( ADD TO CONFIG )
				state = ShooterState.ARMED;		
			}
			break;
		default:
			break;
		}
		
		runCamPID();
	}
	
}
