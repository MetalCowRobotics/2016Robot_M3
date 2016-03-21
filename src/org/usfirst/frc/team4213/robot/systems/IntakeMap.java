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
	public Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false,
			CounterBase.EncodingType.k4X);

	private Timer moveTimer;
	private IntakeState state;

	private boolean stateSet;

	private double raiseTime;
	private double lowerTime;

	public enum IntakeState {
		DOWN, EJECT, INTAKE, UP, RAISING, LOWERING;
	}

	public IntakeMap() {
		setEncDistPerPulse(1 / Intake.COUNT_PER_DEG);
		setState(IntakeState.UP);
		moveTimer = new Timer();
		resetEnc();
		PITCH_ENCODER.setReverseDirection(true);
		lowerTime = CowDash.getNum("Intake_Lower_Time", 4);
		raiseTime = CowDash.getNum("Intake_Rise_Time", 4);

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

	public IntakeState getState() {
		return state;
	}

	public void idle() {
		moveTimer.reset();
		moveTimer.start();
		setState(IntakeState.LOWERING);
	}

	public void eject() {
		setState(IntakeState.EJECT);
	}

	public void intake() {
		setState(IntakeState.INTAKE);
	}

	public void raise() {
		moveTimer.reset();
		moveTimer.start();
		setState(IntakeState.RAISING);
	}

	public void setState(IntakeState state) {
		stateSet = false;
		this.state = state;
	}

	public void step() {
		switch (state) {
		case EJECT:
			if (!stateSet) {
				setRollerSpeed(Intake.EJECT_SPEED);
				stateSet = true;
			}
			break;
		case INTAKE:
			if (!stateSet) {
				setRollerSpeed(Intake.INTAKE_SPEED);
				stateSet = true;
			}
			break;
		case UP:
			if (!stateSet) {
				setPitchSpeed(0);
				setRollerSpeed(0);
				stateSet = true;
			}
			break;
		case DOWN:
			if (!stateSet) {
				setPitchSpeed(0);
				setRollerSpeed(0);
				stateSet = true;
			}
			break;
		case RAISING:
			if (moveTimer.get() < raiseTime) {
				if (!stateSet) {
					raiseTime = CowDash.getNum("Intake_Rise_Time", 4);
					setPitchSpeed(Intake.RAISE_SPEED);
					setRollerSpeed(0);
					stateSet = true;
				}
			} else {
				moveTimer.stop();
				setState(IntakeState.UP);
			}
			break;
		case LOWERING:
			if (moveTimer.get() < lowerTime) {
				if (!stateSet) {
					lowerTime = CowDash.getNum("Intake_Lower_Time", 4);
					setPitchSpeed(Intake.LOWER_SPEED);
					setRollerSpeed(0);
					stateSet = true;
				}
			} else {
				moveTimer.stop();
				setState(IntakeState.DOWN);
			}
			break;

		default:
			break;
		}
	}

}
