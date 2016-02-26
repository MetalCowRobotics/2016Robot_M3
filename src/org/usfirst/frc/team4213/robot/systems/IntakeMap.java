package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class IntakeMap {
	
	private static final SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	private static final SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	private static final Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A,Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);
	
	private IntakeState state;
	private int desiredPitchAngle;
	private double pidError;
	private boolean hasHitLimitSwitch = false;
	

	public enum IntakeState {
		IDLE, EJECT, INTAKE, RAISED, RAISING, LOWERING;
	}
	
	public IntakeMap(){
		resetEnc();
		setEncDistPerPulse(1/Intake.COUNT_PER_DEG);
		state = IntakeState.IDLE;
		desiredPitchAngle = 0;
	}
	
	public void setRollerSpeed(double speed){
		ROLLER_MOTOR.set(speed);
	}
	
	public void setPitchSpeed(double speed){
		PITCH_MOTOR.set(speed);
	}

	public double getEncPosition() {
		return PITCH_ENCODER.get();
	}
	
	public double getEncDistance() {
		return PITCH_ENCODER.getDistance();
	}
	
	public void resetEnc(){
		PITCH_ENCODER.reset();
	}
	
	public void setEncDistPerPulse(double distancePerPulse){
		PITCH_ENCODER.setDistancePerPulse(distancePerPulse);
	}
	
	public void runPitchPID(){
		pidError = desiredPitchAngle - getEncDistance();
		setPitchSpeed(pidError / 180);
	}
	
	public IntakeState getState(){
		return state;
	}
	
	public void setState(IntakeState state){
		this.state = state;
	}
	
	public void ejectBall() {
		if( state == IntakeState.IDLE ){
			state = IntakeState.EJECT;
		}else{
			DriverStation.reportError("You cannot Eject a Ball Unless the intake is Down", false);
		}
	}

	public void intakeBall() {
		if( state == IntakeState.IDLE ){
			state = IntakeState.INTAKE;
		}else{
			DriverStation.reportError("You cannot Intake a Ball Unless the Intake is Down", false);
		}
	}

	public void raiseIntake() {
		if( state == IntakeState.IDLE || state == IntakeState.LOWERING ){
			state = IntakeState.RAISING;
		}else{
			DriverStation.reportError("You cannot Raise while Intaking", false);
		}
	}

	public void lowerIntake() {
		if( state == IntakeState.RAISED || state == IntakeState.RAISING){
			state = IntakeState.LOWERING;
		}else{
			DriverStation.reportError("You cannot Lower while at the Bottom Limit", false);
		}
	}

	// TODO FIX IDLE
	// REDUNDANT
	public void idle() {
		setRollerSpeed(0);
		if (getEncDistance() != 1) {
			lowerIntake();
		} else {
			state = IntakeState.IDLE;
		}
	}


	public void step() {
		switch (state) {
		case EJECT:
			setRollerSpeed(Intake.EJECT_SPEED);
			desiredPitchAngle = 0;
			break;
		case INTAKE:
			setRollerSpeed(Intake.INTAKE_SPEED);
			desiredPitchAngle = 0;
			break;
		case RAISING:
			setRollerSpeed(0);
			desiredPitchAngle = Intake.RAISE_ANGLE;
			if(pidError == 0){
				state = IntakeState.RAISED;
			}
			break;
		case LOWERING:
			setRollerSpeed(0);
			desiredPitchAngle = 0;
			if(pidError == 0){
				state = IntakeState.IDLE;
			}
			break;
		default:
			break;
		}
		runPitchPID();
	}
	
}
