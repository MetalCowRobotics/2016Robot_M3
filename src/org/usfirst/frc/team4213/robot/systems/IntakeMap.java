package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.raw_systems.RawIntake;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.Timer;

public class IntakeMap{

	private static Timer moveTimer;
	private static IntakeRaiseState vertState;
	private static IntakeRollerState rollerState;
	
	public static enum IntakeRaiseState {
		DOWN, UP, RAISING , LOWERING ;
	}
	
	public static enum IntakeRollerState {
		INTAKE,EJECT,IDLE;
	}

	private IntakeMap() {}
	
	static {
		RawIntake.setEncDistPerPulse(1 / Intake.COUNT_PER_DEG);
		RawIntake.resetEnc();
		RawIntake.setReverseDirection(true);
		
		vertState = IntakeRaiseState.UP;
		rollerState = IntakeRollerState.IDLE;
		
		moveTimer = new Timer();
	}

	public static IntakeRaiseState getRaiseState() {
		return vertState;
	}
	
	public static IntakeRollerState getRollerState() {
		return rollerState;
	}

	public static void idleRoller() {
		rollerState = IntakeRollerState.IDLE;
	}

	public static void eject() {
		rollerState = IntakeRollerState.EJECT;
	}

	public static void intake() {
		rollerState = IntakeRollerState.INTAKE;
	}
	
	public static void raise() {
		moveTimer.reset();
		moveTimer.start();
		vertState = IntakeRaiseState.RAISING;
	}
	
	public static void lower() {
		moveTimer.reset();
		moveTimer.start();
		vertState = IntakeRaiseState.LOWERING;
	}

	public static void step() {
		
		switch (rollerState) {
		case EJECT:
			RawIntake.setRollerSpeed(Intake.EJECT_SPEED);
			break;
		case INTAKE:
			RawIntake.setRollerSpeed(Intake.INTAKE_SPEED);
			break;
		case IDLE:
			RawIntake.setRollerSpeed(0);
			break;
		}
		
		switch(vertState){
		case UP:
			RawIntake.setPitchSpeed(0);
			break;
		case DOWN:
			RawIntake.setPitchSpeed(0);
			break;
		case RAISING:
			if(moveTimer.get() < CowDash.getNum("Intake_Rise_Time", 8)){
			RawIntake.setPitchSpeed(Intake.RAISE_SPEED);
			}else{
				vertState = IntakeRaiseState.UP;
			}
			break;
		case LOWERING:
			if(moveTimer.get() < CowDash.getNum("Intake_Lower_Time", 8)){
				RawIntake.setPitchSpeed(Intake.LOWER_SPEED);
			}else{
				vertState = IntakeRaiseState.DOWN;
			}
			break;
		
		default:
			break;
		}
	}

}
