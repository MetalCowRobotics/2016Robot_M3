package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;

public class IntakeMap {

	public final SpeedController ROLLER_MOTOR = new Victor(Intake.ROLLER_MOTOR_CHANNEL);
	public final SpeedController PITCH_MOTOR = new Victor(Intake.PITCH_MOTOR_CHANNEL);
	//public Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);
        public final DigitalInput LIMIT_SWITCH_UP = new DigitalInput(Intake.LIMIT_SWITCH_UP);
        public final DigitalInput LIMIT_SWITCH_DOWN = new DigitalInput(Intake.LIMIT_SWITCH_DWN);

	private Timer moveTimer;
	private int vertState;
	private int rollerState;
	
        public class IntakeRaiseState {
            public static final int DOWN = 1;
            public static final int UP = 2;
            public static final int RAISING = 3;
            public static final int LOWERING = 4;
        }
        
        public class IntakeRollerState {
            public static final int INTAKE = 1;
            public static final int EJECT = 2;
            public static final int IDLE = 3;
        }

	public IntakeMap() {
		
		vertState = IntakeRaiseState.UP;
		rollerState = IntakeRollerState.IDLE;
		
		moveTimer = new Timer();
		//resetEnc();
		//PITCH_ENCODER.setReverseDirection(true);
		
	}

	public void setRollerSpeed(double speed) {
		ROLLER_MOTOR.set(speed);
	}

	public void setPitchSpeed(double speed) {
		PITCH_MOTOR.set(speed);
	}

	/*public double getEncPosition() {
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
	}*/

	public int getRaiseState() {
		return vertState;
	}
	
	public int getRollerState() {
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
            
            System.out.println("Roller State:" + rollerState);
            System.out.println("Vert State:" + vertState);
		
		switch (rollerState) {
		case IntakeRollerState.EJECT:
			setRollerSpeed(Intake.EJECT_SPEED);
			break;
		case IntakeRollerState.INTAKE:
			setRollerSpeed(Intake.INTAKE_SPEED);
			break;
		case IntakeRollerState.IDLE:
			setRollerSpeed(0);
			break;
		}
		
		switch(vertState){
		case IntakeRaiseState.UP:
			setPitchSpeed(0);
			break;
		case IntakeRaiseState.DOWN:
			setPitchSpeed(0);
			break;
		case IntakeRaiseState.RAISING:
			if(moveTimer.get() < CowDash.getNum("Intake_Rise_Time", 8)){
                        	setPitchSpeed(Intake.RAISE_SPEED);
			}else{
				vertState = IntakeRaiseState.UP;
			}
			break;
		case IntakeRaiseState.LOWERING:
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
