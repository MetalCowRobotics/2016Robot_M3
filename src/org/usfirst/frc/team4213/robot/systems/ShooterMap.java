package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.team4213.lib14.TBHController;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class ShooterMap {

	private static final SpeedController CAM_MOTOR = new CANTalon(Shooter.CAM_CHANNEL);
	private static final SpeedController FLYWHEEL_MOTOR = new CANTalon(Shooter.FLYWHEEL_CHANNEL);
	private static final SpeedController FLYWHEEL_MOTOR_2 = new CANTalon(Shooter.FLYWHEEL_CHANNEL_2);

	private static final Encoder CAM_ENCODER = new Encoder(Shooter.CAM_ENC_CH_A, Shooter.CAM_ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	private static final Encoder FLYWHEEL_ENCODER = new Encoder(Shooter.FLYWHEEL_ENC_CH_A, Shooter.FLYWHEEL_ENC_CH_B,
			false, CounterBase.EncodingType.k4X);

	private double flywheelSpeed;
	private ShooterState state;
	private Timer armTimer;
	private Timer shootTimer;
	private Timer ejectTimer;
	private PIDController camPID;
	private TBHController flywheelTBH;

	private boolean stateSet;

	public enum ShooterState {
		INTAKE, EJECT, IDLE, ARMING, ARMED, SHOOTING;
	}

	public ShooterMap() {
		setState(ShooterState.IDLE);
		CAM_ENCODER.setReverseDirection(true);
		CAM_MOTOR.setInverted(true);

		armTimer = new Timer();
		shootTimer = new Timer();
		ejectTimer = new Timer();

		flywheelSpeed = CowDash.getNum("Shooter_desiredFlywheelRPS", 100);
		camPID = new PIDController("Shooter_Cam", 75, 0, 1.2, 1);
		flywheelTBH = new TBHController("Shooter_Flywheel");

		resetEnc();
		camPID.setTarget(0);
		CAM_ENCODER.setDistancePerPulse(1 / Shooter.CAM_PPD);
		FLYWHEEL_ENCODER.setDistancePerPulse(1 / Shooter.FLYWHEEL_PPR);
	}

	public ShooterState getState() {
		return state;
	}

	public void setCamSpeed(double speed) {
		CAM_MOTOR.set(speed);
	}

	public void setCurrentWheelSpeed(double speed) {
		FLYWHEEL_MOTOR.set(speed);
		FLYWHEEL_MOTOR_2.set(speed);
	}

	public void setShootWheelSpeed(double speed) {
		flywheelSpeed = speed;
	}

	public double getCamEncValue() {
		return CAM_ENCODER.get();
	}

	public double getCamEncDist() {
		return CAM_ENCODER.getDistance();
	}

	public double getFlyEncValue() {
		return FLYWHEEL_ENCODER.get();
	}

	public double getFlyEncDist() {
		return FLYWHEEL_ENCODER.getDistance();
	}

	public double getFlyEncRate() {
		return FLYWHEEL_ENCODER.getRate();
	}

	public void resetEnc() {
		CAM_ENCODER.reset();
	}

	public boolean readyToFire() {
		return state == ShooterState.ARMED
				&& Math.abs(FLYWHEEL_ENCODER.getRate() - CowDash.getNum("Shooter_desiredFlywheelRPS", 80)) < CowDash
						.getNum("Shooter_RPSErrorTolerance", 1.5);
	}

	public void arm() {
		if (state == ShooterState.IDLE || state == ShooterState.EJECT) {
			armTimer.reset();
			armTimer.start();
			setState(ShooterState.ARMING);
		} else {
			// DriverStation.reportError("You cannot Arm unless You're Up and
			// Idle", false);
		}
	}

	public void intake() {
		setState(ShooterState.INTAKE);
	}

	public void eject() {
		ejectTimer.reset();
		ejectTimer.start();
		setState(ShooterState.EJECT);
	}

	public boolean shoot() {
		if (state == ShooterState.ARMED) {
			shootTimer.reset();
			shootTimer.start();
			setState(ShooterState.SHOOTING);
			return true;
		} else {
			return false;

		}
	}

	public void idle() {
		setState(ShooterState.IDLE);
	}

	private void runCamPID() {
		setCamSpeed(camPID.feedAndGetValue(-getCamEncDist()));
	}

	public void setState(ShooterState state) {
		stateSet = false;
		this.state = state;
	}

	public void step() {
		switch (state) {
		case INTAKE:
			if (!stateSet) {
				CowDash.setString("Shooter_state", "INTAKE");
				camPID.setTarget(CowDash.getNum("Cam_Intake_Angle", -30));
				setCurrentWheelSpeed(-1 * CowDash.getNum("Shooter_intakePower", 0.5));
				stateSet = true;
			}
			break;
		case EJECT:
			if (!stateSet) {
				CowDash.setString("Shooter_state", "EJECT");
				if (ejectTimer.get() < 0.4) {
					setCurrentWheelSpeed(-CowDash.getNum("Shooter_shootIntakePower", 0.4));
				} else {
					setCurrentWheelSpeed(-CowDash.getNum("Shooter_ejectPower", 1.0));
				}

				if (ejectTimer.get() > .75) {
					camPID.setTarget(CowDash.getNum("Cam_Eject_Angle", -180));
					stateSet = true;
					ejectTimer.stop();
				}
			}

			break;
		case SHOOTING:
			if (!stateSet) {
				camPID.setTarget(CowDash.getNum("Cam_Shoot_Angle", -180));
				CowDash.setString("Shooter_state", "SHOOTING");
				stateSet = true;
			}
			setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(getFlyEncRate(), FLYWHEEL_MOTOR.get()));
			if (shootTimer.get() > 2) {
				shootTimer.stop();
				shootTimer.reset();
				idle();
				camPID.setTarget(0);
			}
			break;
		case ARMING:
			if (!stateSet) {
				CowDash.setString("Shooter_state", "ARMING");
				camPID.setTarget(CowDash.getNum("Cam_Arm_Angle", 10));
				// Intake for armIntakeTime, then go out
				flywheelTBH.setTarget(flywheelSpeed);
				stateSet = true;
			}
			if (armTimer.get() > CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(getFlyEncRate(), FLYWHEEL_MOTOR.get()));
			} else {
				setCurrentWheelSpeed(-CowDash.getNum("Shooter_shootIntakePower", 0.4));
			}

			if (armTimer.get() > CowDash.getNum("Shooter_armTime", 2.0)
					+ CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				// 2 Seconds, Can be changed ( ADD TO CONFIG )
				armTimer.stop();
				armTimer.reset();
				setState(ShooterState.ARMED);
			}
			break;
		case ARMED:
			if (!stateSet) {
				camPID.setTarget(CowDash.getNum("Cam_Arm_Angle", 10));
				CowDash.setString("Shooter_state", "ARMED");
				stateSet = true;
			}
			setCurrentWheelSpeed(flywheelTBH.feedAndGetValue(-getFlyEncRate(), FLYWHEEL_MOTOR.get()));
			break;
		case IDLE:
			if (!stateSet) {
				CowDash.setString("Shooter_state", "IDLE");
				camPID.setTarget(CowDash.getNum("Cam_Idle_Angle", 0));
				setCurrentWheelSpeed(0);
				stateSet = true;
			}
			break;
		default:
			break;
		}

		CowDash.setNum("Shooter_Flywheel_RPS::", getFlyEncRate());
		runCamPID();
	}

}
