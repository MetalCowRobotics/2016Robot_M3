package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.team4213.lib14.TBHController;
import org.usfirst.frc.team4213.robot.raw_systems.RawShooter;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

import edu.wpi.first.wpilibj.Timer;

public class ShooterMap {


	private static double flywheelSpeed;
	private static ShooterState state;
	private static Timer armTimer;
	private static Timer shootTimer;
	private static Timer ejectTimer;
	private static PIDController camPID;
	private static TBHController flywheelTBH;

	public static enum ShooterState {
		INTAKE, EJECT, IDLE, ARMING, ARMED, SHOOTING;
	}

	private ShooterMap() {}
	
	static {
		state = ShooterState.IDLE;
		
		armTimer = new Timer();
		shootTimer = new Timer();
		ejectTimer = new Timer();

		camPID = new PIDController("Shooter_Cam", 75, 0, 1.2, 1);
		camPID.setTarget(0);
		
		flywheelSpeed = CowDash.getNum("Shooter_desiredFlywheelRPS", 80);
		flywheelTBH = new TBHController("Shooter_Flywheel");
		
		RawShooter.resetEnc();
		RawShooter.setCamEncDistPerPulse(1 / Shooter.CAM_PPD);
		RawShooter.setFlyEncDistPerPulse(1 / Shooter.FLYWHEEL_PPR);
	}

	public static ShooterState getState() {
		return state;
	}
	
	static void setShootWheelSpeed(double speed) {
		flywheelSpeed = speed;
	}


	public static boolean readyToFire() {
		return state == ShooterState.ARMED
				&& Math.abs(RawShooter.getFlyEncRate() - CowDash.getNum("Shooter_desiredFlywheelRPS", 80)) < CowDash
						.getNum("Shooter_RPSErrorTolerance", 1.5);
	}

	public static void arm() {
		if (state == ShooterState.IDLE || state == ShooterState.EJECT) {
			armTimer.reset();
			armTimer.start();
			state = ShooterState.ARMING;
		} else {
			// DriverStation.reportError("You cannot Arm unless You're Up and
			// Idle", false);
		}
	}

	public static void intake() {
		state = ShooterState.INTAKE;
	}

	public void eject() {
		ejectTimer.reset();
		ejectTimer.start();
		state = ShooterState.EJECT;
	}

	public boolean shoot() {
		if (state == ShooterState.ARMED) {
			shootTimer.reset();
			shootTimer.start();
			state = ShooterState.SHOOTING;
			return true;
		} else {
			return false;

		}
	}

	public void idle() {
		state = ShooterState.IDLE;
	}

	private void runCamPID() {
		RawShooter.setCamSpeed(camPID.feedAndGetValue(-RawShooter.getCamEncDist()));
	}

	public void step() {
		switch (state) {
		case INTAKE:
			CowDash.setString("Shooter_state", "INTAKE");
			// if (getSwitchHit()) {
			// state = ShooterState.IDLE;
			// break;
			// } // FIXED: This really doesn't help, we found -Thad
			camPID.setTarget(CowDash.getNum("Cam_Intake_Angle", -30));
			RawShooter.setCurrentWheelSpeed(-1 * CowDash.getNum("Shooter_intakePower", 0.5));
			break;
		case EJECT:
			CowDash.setString("Shooter_state", "EJECT");

			if (ejectTimer.get() < 0.4) {
				RawShooter.setCurrentWheelSpeed(-CowDash.getNum("Shooter_shootIntakePower", 0.4));
			} else {
				RawShooter.setCurrentWheelSpeed(-CowDash.getNum("Shooter_ejectPower", 1.0));
			}

			if (ejectTimer.get() > .75) {
				camPID.setTarget(CowDash.getNum("Cam_Eject_Angle", -180));
			}
			break;
		case SHOOTING:
			CowDash.setString("Shooter_state", "SHOOTING");
			RawShooter.setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(RawShooter.getFlyEncRate(), RawShooter.getFlyMotorSpeed()));
			camPID.setTarget(CowDash.getNum("Cam_Shoot_Angle", -180));
			if (shootTimer.get() > 2) {
				shootTimer.stop();
				shootTimer.reset();
				idle();
				camPID.setTarget(0);
			}
			break;
		case ARMING:
			CowDash.setString("Shooter_state", "ARMING");
			camPID.setTarget(CowDash.getNum("Cam_Arm_Angle", 10));
			// Intake for armIntakeTime, then go out
			flywheelTBH.setTarget(flywheelSpeed);
			if (armTimer.get() > CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				RawShooter.setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(RawShooter.getFlyEncRate(), RawShooter.getFlyMotorSpeed()));

			} else {
				RawShooter.setCurrentWheelSpeed(-CowDash.getNum("Shooter_shootIntakePower", 0.4));
			}

			if (armTimer.get() > CowDash.getNum("Shooter_armTime", 2.0)
					+ CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				// 2 Seconds, Can be changed ( ADD TO CONFIG )
				armTimer.stop();
				armTimer.reset();
				state = ShooterState.ARMED;
			}
			break;
		case ARMED:
			camPID.setTarget(CowDash.getNum("Cam_Arm_Angle", 10));
			CowDash.setString("Shooter_state", "ARMED");
			RawShooter.setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(-RawShooter.getFlyEncRate(), RawShooter.getFlyMotorSpeed()));
			break;
		case IDLE:
			CowDash.setString("Shooter_state", "IDLE");
			camPID.setTarget(CowDash.getNum("Cam_Idle_Angle", 0));
			RawShooter.setCurrentWheelSpeed(0);
			break;
		default:
			break;
		}

		CowDash.setNum("Shooter_Flywheel_RPS::", RawShooter.getFlyEncRate());
		runCamPID();
	}

}
