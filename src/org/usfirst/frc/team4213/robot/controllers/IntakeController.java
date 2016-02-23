package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.DriverStation;

public class IntakeController {

	private final IntakeMap intake;
	public IntakeState state;
	private int desiredPitchAngle;
	private double error;
	
	public enum IntakeState {
		IDLE, EJECT, INTAKE, RAISED, RAISING, LOWERING;
	}

	public IntakeController(IntakeMap intakeMap) {
		intake = intakeMap;
		state = IntakeState.IDLE;
		intake.PITCH_ENCODER.reset();
		desiredPitchAngle = 0;
		intake.PITCH_ENCODER.setDistancePerPulse(1/Intake.COUNT_PER_DEG);
		
		// TODO Start with raised

	}

	public void ejectBall() {
		if( state == IntakeState.IDLE ){
			intake.setRollerSpeed(Intake.EJECT_SPEED);
			state = IntakeState.EJECT;
		}else{
			DriverStation.reportError("You cannot Eject a Ball Unless the intake is Down", false);
		}
	}

	public void intakeBall() {
		if( state == IntakeState.IDLE ){
			intake.setRollerSpeed(Intake.INTAKE_SPEED);
			state = IntakeState.INTAKE;
		}else{
			DriverStation.reportError("You cannot Intake a Ball Unless the Intake is Down", false);
		}
	}

	public void raiseIntake() {
		if( state == IntakeState.IDLE || state == IntakeState.LOWERING ){
			desiredPitchAngle = Intake.RAISE_ANGLE;
			state = IntakeState.RAISING;
		}else{
			DriverStation.reportError("You cannot Raise while Intaking", false);
		}
	}

	public void lowerIntake() {
		if( state == IntakeState.RAISED || state == IntakeState.RAISING){
			desiredPitchAngle = 1;
			state = IntakeState.LOWERING;
		}else{
			DriverStation.reportError("You cannot Lower while at the Bottom Limit", false);
		}
	}

	public void idle() {
		intake.setRollerSpeed(0);
		if (intake.getEncDistance() != 1) {
			lowerIntake();
		} else {
			state = IntakeState.IDLE;
		}
	}
	
	public void runPitchPID(){
		error = desiredPitchAngle - intake.getEncDistance();
		intake.setPitchSpeed(-error / Intake.PID_P_VAL);
	}

	public void step() {
		switch (state) {
		case RAISING:
			if(error == 0){
				state = IntakeState.RAISED;
			}
			break;
		case LOWERING:
			if(error == 0){
				state = IntakeState.IDLE;
			}
			break;
		default:
			break;
		}
		runPitchPID();
	}
}
