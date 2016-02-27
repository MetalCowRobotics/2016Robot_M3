package org.usfirst.frc.team4213.robot.systems;

import java.util.Calendar;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;

public class ShooterMap {

	private static final SpeedController CAM_MOTOR = new Jaguar(Shooter.CAM_CHANNEL);
	private static final SpeedController FLYWHEEL_MOTOR = new Jaguar(Shooter.FLYWHEEL_CHANNEL);
	private static final Encoder CAM_ENCODER = new Encoder(Shooter.ENC_CH_A, Shooter.ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	private static final DigitalInput BALL_LIM_SWITCH = new DigitalInput(Shooter.LIMIT_SWITCH);

	private int desiredCamAngle;
	public ShooterState state;
	private long armTime;
	private long shootTime;
	private double error;

	public enum ShooterState {
		INTAKE, EJECT, IDLE, ARMING, ARMED, SHOOTING;
	}

	public ShooterMap() {
		state = ShooterState.IDLE;
		// shooter.camMotor.enableBrakeMode(true); // DOES THIS WORK ????
		resetEnc();
		desiredCamAngle = 0;
		CAM_ENCODER.setDistancePerPulse(1 / Shooter.COUNT_PER_DEG);
	}

	public void setCamSpeed(double speed) {
		CAM_MOTOR.set(speed);
	}

	public void setWheelSpeed(double speed) {
		FLYWHEEL_MOTOR.set(speed);
	}

	public boolean getSwitchHit() {
		return BALL_LIM_SWITCH.get();
	}

	public double getEncValue() {
		return CAM_ENCODER.get();
	}

	public double getEncDist() {
		return CAM_ENCODER.getDistance();
	}

	public void resetEnc() {
		CAM_ENCODER.reset();
	}

	public void arm() {
		if (state == ShooterState.IDLE || state == ShooterState.EJECT) {
			armTime = getCurrentTimeMS();
			state = ShooterState.ARMING;
		} else {
			DriverStation.reportError("You cannot Arm unless You're Up and Idle", false);
		}
	}

	public void intake() {
		state = ShooterState.INTAKE;
	}

	public void eject() {
		state = ShooterState.EJECT;
	}

	public void shoot() {
		if (state == ShooterState.ARMED) {
			shootTime = getCurrentTimeMS();
			state = ShooterState.SHOOTING;
		} else {
			// TODO VIBE CONTROLLER
			DriverStation.reportError("Shooter is Not Armed : Ball Cannot be Shot", false);
		}
	}

	public void idle() {
		state = ShooterState.IDLE;
	}

	private long getCurrentTimeMS() {
		return Calendar.getInstance().get(Calendar.MILLISECOND);
	}

	private void runCamPID() {
		error = desiredCamAngle - getEncDist();
		setCamSpeed(error / 180);
	}

	public void step() {
		switch (state) {
		case INTAKE:
			if (getSwitchHit()) {
				state = ShooterState.IDLE;
				break;
			}
			desiredCamAngle = 0;
			setWheelSpeed(Shooter.INTAKE_SPEED);
			break;
		case EJECT:
			desiredCamAngle = 0;
			setWheelSpeed(Shooter.EJECT_SPEED);
			break;
		case SHOOTING:
			desiredCamAngle = 360;
			if (error == 3 && getCurrentTimeMS() - shootTime > 1 * 1000) {
				// 1 Second of Wheel Spinning. ( ADD TO CONFIG )
				resetEnc();
				idle();
				desiredCamAngle = 0;
			}
			break;
		case ARMING:
			setWheelSpeed(Shooter.SHOOT_SPEED);
			if (getCurrentTimeMS() - armTime > 2 * 1000) {
				// 2 Seconds, Can be changed ( ADD TO CONFIG )
				state = ShooterState.ARMED;
			}
			break;
		case ARMED:
			setWheelSpeed(Shooter.SHOOT_SPEED);
		case IDLE:
			desiredCamAngle = 120;
			setWheelSpeed(0);
			break;
		default:
			break;
		}

		runCamPID();
	}
}
