package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.systems.IntakeMap;

public class IntakeController {
	
	IntakeMap intake; 
	
	
	public IntakeController(IntakeMap intakeMap){
		intake = intakeMap;
	}
	
	private void spinIntake(double speed){
		intake.INTAKE_MOTOR.set(speed);
	}
	
	private void ejectBall(){
		spinIntake(intake.EJECT_SPEED);
	}
	
	public void intakeBall(){
		spinIntake(intake.INTAKE_SPEED);
	}
	
}
