package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;

public class TurretMap { // Replace these with the Constants
	private static final SpeedController YAW_MOTOR = new Jaguar(1);
	private static final SpeedController PITCH_MOTOR = new Jaguar(2);
	private static final Encoder YAW_ENC = new Encoder(Turret.Yaw_Motor.ENC_CH_A, Turret.Yaw_Motor.ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	private static final Encoder PITCH_ENC = new Encoder(2, 3, true, CounterBase.EncodingType.k4X);

	private double desiredPitchAngle;
	private double desiredYawAngle;
	private double pitchError;
	private double yawError;

	public TurretState state;

	public enum TurretState {
		ENGAGING, ENGAGED, IDLING, IDLE;
	}

	public TurretMap() {
		state = TurretState.IDLE;
		YAW_ENC.reset();
		YAW_ENC.setDistancePerPulse(1/Turret.Yaw_Motor.COUNT_PER_DEG);
		PITCH_ENC.reset();
		PITCH_ENC.setDistancePerPulse(1/Turret.Pitch_Motor.COUNT_PER_DEG);
		PITCH_MOTOR.setInverted(true);
	}

	public void setYawSpeed(double speed) {
		YAW_MOTOR.set(speed);
	}

	public void setPitchSpeed(double speed) {
		PITCH_MOTOR.set(speed);
	}

	public int getYawEncPosition() {
		return YAW_ENC.get();
	}

	public double getYawEncDistance() {
		return YAW_ENC.getDistance();
	}

	public int getPitchEncPosition() {
		return PITCH_ENC.get();
	}

	public double getPitchEncDistance() {
		return PITCH_ENC.getDistance();
	}

	public void engage() {
		state = TurretState.ENGAGING;
	}

	public void idle() {
		state = TurretState.IDLING;
	}

	public void bumpTurretUp() {
		if (desiredPitchAngle < Turret.Pitch_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			desiredPitchAngle += Turret.Pitch_Motor.BUMP_AMT;
		} else {
			DriverStation.reportError("\n At Max Pitch Angle", false);
		}
	}

	public void bumpTurretDown() {
		if (desiredPitchAngle > Turret.Pitch_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			desiredPitchAngle -= Turret.Pitch_Motor.BUMP_AMT;
		} else {
			DriverStation.reportError("\n At Min Pitch Angle", false);
		}
	}

	public void bumpTurretRight() {
		if (desiredYawAngle < Turret.Yaw_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			desiredYawAngle += Turret.Yaw_Motor.BUMP_AMT;
		} else {
			DriverStation.reportError("\n At Max Yaw Angle", false);
		}
	}

	public void bumpTurretLeft() {
		if (desiredYawAngle > Turret.Yaw_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			desiredYawAngle -= Turret.Yaw_Motor.BUMP_AMT;
		} else {
			DriverStation.reportError("\n At Min Yaw Angle", false);
		}
	}

	private void runPitchPID() {
		CowDash.setNum("Turret_desiredPitchAngle", desiredPitchAngle);
		pitchError = desiredPitchAngle - getPitchEncDistance();
		CowDash.setNum("Turret_pitchError", pitchError);
		setPitchSpeed(pitchError / 10);
	}

	private void runYawPID() {
		CowDash.setNum("Turret_desiredYawAngle", desiredYawAngle);
		yawError = desiredYawAngle - getYawEncDistance();
		CowDash.setNum("Turret_yawError", yawError);
		setYawSpeed(yawError / 180);
	}

	public void step() {
		switch (state) {
		case IDLING:
			CowDash.setString("Turret_state", "Idling");
			desiredYawAngle = 0;
			if (Math.abs(yawError) < Turret.Yaw_Motor.ABS_TOLERANCE) {
				desiredPitchAngle = 0;
				if (Math.abs(pitchError) < Turret.Pitch_Motor.ABS_TOLERANCE) {
					state = TurretState.IDLE;
				}
			}
			break;
		case ENGAGING:
			CowDash.setString("Turret_state", "Engaging");
			desiredPitchAngle = Turret.Pitch_Motor.MIN_ANGLE;
			if (Math.abs(pitchError) < Turret.Pitch_Motor.ABS_TOLERANCE) {
				state = TurretState.ENGAGED;
			}
			break;
		case ENGAGED:
			CowDash.setString("Turret_state", "Engaged");
			break;
		case IDLE:
			CowDash.setString("Turret_state", "Idle");
			setPitchSpeed(0);
			setYawSpeed(0);
			break;
		default:
			CowDash.setString("Turret_state", "SOMETHINGELSE");
			break;
		}
		runPitchPID();
		runYawPID();
	}

}
