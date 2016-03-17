package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class IntakeMap {

	private static final SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	private static final SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	private static final Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false,
			CounterBase.EncodingType.k4X);
	public static final DigitalInput LIMIT_SWITCH = new DigitalInput(Intake.LIMIT_SWITCH_CH);

	private IntakeState state;
	private int desiredPitchAngle;
	private double pidError;
	private boolean hasHitLimitSwitch = false;

	public enum IntakeState {
		DOWN, EJECT, INTAKE, UP;
	}

	public IntakeMap() {
		resetEnc();
		setEncDistPerPulse(1 / Intake.COUNT_PER_DEG);
		state = IntakeState.DOWN;
		desiredPitchAngle = 0;
	}

	public void setRollerSpeed(double speed) {
		ROLLER_MOTOR.set(speed);
	}

	public void setPitchSpeed(double speed) {
		if (!hasHitLimitSwitch) {
			PITCH_MOTOR.set(Intake.LOWER_SPEED);
		} else {
			PITCH_MOTOR.set(speed);
		}
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

	public void runPitchPID() {
		pidError = desiredPitchAngle - getEncDistance();
		setPitchSpeed(pidError * Intake.PID_P_VAL);
	}

	public IntakeState getState() {
		return state;
	}

	public void idle() {
		state = IntakeState.DOWN;
	}

	public void eject() {
		state = IntakeState.EJECT;
	}

	public void intake() {
		state = IntakeState.INTAKE;
	}

	public void raise() {
		state = IntakeState.UP;
	}

	public boolean getBottomedOut() {
		return !LIMIT_SWITCH.get();
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
		case UP:
			setRollerSpeed(0);
			desiredPitchAngle = Intake.RAISE_ANGLE;
			break;
		case DOWN:
			setRollerSpeed(0);
			desiredPitchAngle = 0;
			break;
		default:
			break;
		}
		if (getBottomedOut()) {
			hasHitLimitSwitch = true;
			resetEnc();
		}
		runPitchPID();
	}

}
