package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class IntakeMap {

	public final SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	public final SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	public Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);

	private IntakeState state;
	private int desiredPitchAngle = 0;
	private double pidError = 10;
	private double lastPidError = 0;
	private boolean hasHitZero = false;

	public enum IntakeState {
		DOWN, EJECT, INTAKE, UP;
	}

	public IntakeMap() {
		setEncDistPerPulse(1 / Intake.COUNT_PER_DEG);
		state = IntakeState.DOWN;
		desiredPitchAngle = 0;
		resetEnc();
		PITCH_ENCODER.setReverseDirection(true);
		
	}

	public void setRollerSpeed(double speed) {
		ROLLER_MOTOR.set(speed);
	}

	public void setPitchSpeed(double speed) {
		if (!hasHitZero) {
			PITCH_MOTOR.set(Intake.LOWER_SPEED);
			if (Math.abs(getEncDistance()) > 135) {
				desiredPitchAngle = 0;
				resetEnc();
				hasHitZero = true;
			}
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
		
		runPitchPID();
		lastPidError = pidError;
	}

}
