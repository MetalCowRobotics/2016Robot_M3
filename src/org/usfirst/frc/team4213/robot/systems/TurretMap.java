package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.usfirst.frc.team4213.robot.raw_systems.RawTurret;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class TurretMap { // Replace these with the Constants
	

	private static boolean overrideYawPID;
	private static boolean overridePitchPID;

	private static TurretState state;
	
	private static PIDController PITCH_PID = new PIDController("Turret_Pitch", 100, 0, 0, 1);
	private static PIDController YAW_PID = new PIDController("Turret_Yaw", 100, 0, 0, 1);

	public static enum TurretState {
		ENGAGING, ENGAGED, IDLING, IDLE;
	}

	private TurretMap(){}
	
	static {
		state = TurretState.IDLE;
		RawTurret.resetYawEnc();
		RawTurret.setYawEncDistPerPulse(1 / Turret.Yaw_Motor.COUNT_PER_DEG);
		RawTurret.resetPitchEnc();
		RawTurret.setPitchEncDistPerPulse(1 / Turret.Pitch_Motor.COUNT_PER_DEG);
		RawTurret.setPitchEncReverse(true);
		overridePitchPID = false;
		overrideYawPID = false;
	}

	public static TurretState getState() {
		return state;
	}

	public static void setYawSpeed(double speed) {

		// STATE CHECKING
		if (state == TurretState.IDLE) {
			RawTurret.setYawSpeed(0);
		}

		// MAX / MIN ANGLE CHECKING
		if (speed > 0 && RawTurret.getYawEncDistance() > Turret.Yaw_Motor.MAX_ANGLE) {
			RawTurret.setYawSpeed(0);
			return;
		}

		if (speed < 0 && RawTurret.getYawEncDistance() < Turret.Yaw_Motor.MIN_ANGLE) {
			RawTurret.setYawSpeed(0);
			return;
		}

		// SPEED LIMITS
		if (speed > Turret.Yaw_Motor.MAX_SPEED) {
			speed = Turret.Yaw_Motor.MAX_SPEED;
		} else if (speed < -1 * Turret.Yaw_Motor.MAX_SPEED) {
			speed = -Turret.Yaw_Motor.MAX_SPEED;
		}

		RawTurret.setYawSpeed(speed);
	}

	public static void setPitchSpeed(double speed) {
		CowDash.setNum("Turret Up Speed", speed);
		if ((speed > 0 && RawTurret.getPitchEncDistance() > Turret.Pitch_Motor.MAX_ANGLE)
				|| (speed < 0 && RawTurret.getPitchEncDistance() < 0)) {
			RawTurret.setPitchSpeed(0);
			speed = 0;
			return;
		}

		if (state == TurretState.ENGAGED) {
			if (speed < 0) {
				if(RawTurret.getPitchEncDistance() < Turret.Pitch_Motor.MIN_ANGLE){
					DriverStation.reportError("We Cannot Go Any Lower", false);
					RawTurret.setPitchSpeed(0);
					speed = 0;
					return;
				}
			}
		}
		
		DriverStation.reportError("\n Speed : " + speed + "; Current Pos : " + RawTurret.getPitchEncDistance() + "; State : " + state.toString(), false);
		RawTurret.setPitchSpeed(speed);
	}

	public static void engage() {
		state = TurretState.ENGAGING;
	}

	public static void idle() {
		state = TurretState.IDLING;
	}

	public static void bumpTurretUp() {
		if (PITCH_PID.getTarget() < Turret.Pitch_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			PITCH_PID.bumpTarget(Turret.Pitch_Motor.BUMP_AMT);
			// DriverStation.reportError("\n Moving Turret Up", false);
		} else {
			// DriverStation.reportError("\n At Max Pitch Angle", false);
		}
	}

	public static void bumpTurretDown() {
		if (PITCH_PID.getTarget() > Turret.Pitch_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			PITCH_PID.bumpTarget(-Turret.Pitch_Motor.BUMP_AMT);
			// DriverStation.reportError("\n Moving Turret Down", false);
		} else {
			// DriverStation.reportError("\n At Min Pitch Angle", false);
		}
	}

	public static void bumpTurretRight() {
		if (YAW_PID.getTarget() < Turret.Yaw_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			YAW_PID.bumpTarget(Turret.Yaw_Motor.BUMP_AMT);
		} else {
			// DriverStation.reportError("\n At Max Yaw Angle", false);
		}
	}

	public static void bumpTurretLeft() {
		if (YAW_PID.getTarget() > Turret.Yaw_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			YAW_PID.bumpTarget(-Turret.Yaw_Motor.BUMP_AMT);
		} else {
			// DriverStation.reportError("\n At Min Yaw Angle", false);
		}
	}

	public static void manualPitchOverride(double speed) {
		overridePitchPID = true;
		if (state == TurretState.ENGAGED) {
			overridePitchPID = true;
			setPitchSpeed(speed);
		}
	}

	
	public static void manualYawOverride(double speed) {
		overrideYawPID = true;
		if (state == TurretState.ENGAGED) {
			overrideYawPID = true;
			setYawSpeed(speed);

		}
	}

	public static void manualYawBumpOverride(double angle) {
		if (state == TurretState.ENGAGED) {
			YAW_PID.setTarget(RawTurret.getYawEncDistance() + angle);
		}
	}

	public static void manualPitchBumpOverride(double angle) {
		if (state == TurretState.ENGAGED) {
			PITCH_PID.setTarget(RawTurret.getPitchEncDistance() + angle);
		}
	}

	private static void runPitchPID() {
		if (overridePitchPID) {
			PITCH_PID.setTarget(RawTurret.getPitchEncDistance());
		} else {
			setPitchSpeed(PITCH_PID.feedAndGetValue(RawTurret.getPitchEncDistance()));
		}
	}

	private static void runYawPID() {
		if (overrideYawPID) {
			YAW_PID.setTarget(RawTurret.getYawEncDistance());
		} else {
			setYawSpeed(YAW_PID.feedAndGetValue(RawTurret.getYawEncDistance()));
		}
	}

	public static void prestep() {
		overrideYawPID = false;
		overridePitchPID = false;
	}

	public static void endstep() {
		switch (state) {
		case IDLING:
			CowDash.setString("Turret_state", "IDLING");
			YAW_PID.setTarget(0);
			if (Math.abs(YAW_PID.getError()) < Turret.Yaw_Motor.ABS_TOLERANCE) {
				PITCH_PID.setTarget(0);
				if (Math.abs(PITCH_PID.getError()) < Turret.Pitch_Motor.ABS_TOLERANCE) {
					state = TurretState.IDLE;
				}
			}
			break;
		case ENGAGING:
			CowDash.setString("Turret_state", "ENGAGING");
			PITCH_PID.setTarget(Turret.Pitch_Motor.MIN_ANGLE);
			if (Math.abs(PITCH_PID.getError()) < Turret.Pitch_Motor.ABS_TOLERANCE) {
//				YAW_PID.setTarget(-180);
//				if(Math.abs(YAW_PID.getError()) < Turret.Yaw_Motor.ABS_TOLERANCE){
						state = TurretState.ENGAGED;
//				}
			}
			break;
		case ENGAGED:
			CowDash.setString("Turret_state", "ENGAGED");
			break;
		case IDLE:
			CowDash.setString("Turret_state", "IDLE");
			setPitchSpeed(0);
			setYawSpeed(0);
			break;
		default:
			CowDash.setString("Turret_state", "I_AM_ERROR");
			break;
		}
		runYawPID();
		runPitchPID();

	}

	public static boolean isAtYawTarget() {
		return Math.abs(YAW_PID.getError())<Turret.Yaw_Motor.ABS_TOLERANCE;
	}

}
