package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class IntakeMap {

	public final SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	public final SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	public Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);

	private Timer moveTimer;
	private IntakeRaiseState vertState;
	private IntakeRollerState rollerState;
	
	public enum IntakeRaiseState {
		DOWN, UP, RAISING , LOWERING ;
	}
	
	public enum IntakeRollerState {
		INTAKE,EJECT,IDLE;
	}

	public IntakeMap() {
		setEncDistPerPulse(1 / Intake.COUNT_PER_DEG);
		
		vertState = IntakeRaiseState.UP;
		rollerState = IntakeRollerState.IDLE;
		
		moveTimer = new Timer();
		resetEnc();
		PITCH_ENCODER.setReverseDirection(true);
		
	}

	public void setRollerSpeed(double speed) {
		ROLLER_MOTOR.set(speed);
	}

	public void setPitchSpeed(double speed) {
		PITCH_MOTOR.set(speed);
	}

	public double getEncPosition() {
		return PITCH_ENCODER.get();
	}

	public double getEncDistance() {
		return PITCH_ENCODER.getDistance();
	}

	public void resetEnc() {
		PITCH_ENCODER.reset();
	}

	public void setEncDistPerPulse(double distancePerPulse) {
		PITCH_ENCODER.setDistancePerPulse(distancePerPulse);
	}

	public IntakeRaiseState getRaiseState() {
		return vertState;
	}
	
	public IntakeRollerState getRollerState() {
		return rollerState;
	}

	public void idleRoller() {
		rollerState = IntakeRollerState.IDLE;
	}

	public void eject() {
		rollerState = IntakeRollerState.EJECT;
	}

	public void intake() {
		rollerState = IntakeRollerState.INTAKE;
	}
	
	public void raise() {
		moveTimer.reset();
		moveTimer.start();
		vertState = IntakeRaiseState.RAISING;
	}
	
	public void lower() {
		moveTimer.reset();
		moveTimer.start();
		vertState = IntakeRaiseState.LOWERING;
	}

	public void step() {
		
		switch (rollerState) {
		case EJECT:
			setRollerSpeed(Intake.EJECT_SPEED);
			break;
		case INTAKE:
			setRollerSpeed(Intake.INTAKE_SPEED);
			break;
		case IDLE:
			setRollerSpeed(0);
			break;
		}
		
		switch(vertState){
		case UP:
			setPitchSpeed(0);
			break;
		case DOWN:
			setPitchSpeed(0);
			break;
		case RAISING:
			if(moveTimer.get() < CowDash.getNum("Intake_Rise_Time", 8)){
			setPitchSpeed(Intake.RAISE_SPEED);
			}else{
				vertState = IntakeRaiseState.UP;
			}
			break;
		case LOWERING:
			if(moveTimer.get() < CowDash.getNum("Intake_Lower_Time", 8)){
				setPitchSpeed(Intake.LOWER_SPEED);
			}else{
				vertState = IntakeRaiseState.DOWN;
			}
			break;
		
		default:
			break;
		}
	}

}
