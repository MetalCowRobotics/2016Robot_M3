package org.usfirst.frc.team4213.robot.controllers;

import java.util.Calendar;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;

import edu.wpi.first.wpilibj.DriverStation;

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
		shooter.resetEnc();
		desiredCamAngle = 0;
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
		if(desiredCamAngle != 0){
			desiredCamAngle = 0;
		}
		shooter.setWheelSpeed(Shooter.INTAKE_SPEED);
		state = ShooterState.INTAKE;
	}

	public void shoot() {
		if(state == ShooterState.ARMED){
			desiredCamAngle = 360;
			shootTime = getCurrentTimeMS();
			state = ShooterState.SHOOTING;
		}else{
			// TODO VIBE CONTROLLER
			DriverStation.reportError("Shooter is Not Armed : Ball Cannot be Shot", false);
		}
	}
	
	public void idle(){
		//shooter.setCamSpeed(0); // Don't Need ( Unsure )
		shooter.setWheelSpeed(0);
		state = ShooterState.IDLE;
	}
	
	public void lowerCam(){
		desiredCamAngle = 120; // ADD TO CONFIG		
	}
	
	private long getCurrentTimeMS(){
		return Calendar.getInstance().get(Calendar.MILLISECOND);
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
