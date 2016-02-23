package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

public class IntakeController {
	
	IntakeMap intake; 
	
	public enum intakeState {
		IDLE ,EJECTING, INTAKING, RAISED;
	}
	
	public IntakeController(IntakeMap intakeMap){
		intake = intakeMap;
	}
	
	public void ejectBall(){
		intake.setRollerSpeed(Intake.EJECT_SPEED);
	}
	
	public void intakeBall(){
		intake.setRollerSpeed(Intake.INTAKE_SPEED);
	}
	
	public void raiseIntake(){
		
	}
}
