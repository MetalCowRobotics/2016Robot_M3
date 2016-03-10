package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;
import org.team4213.lib14.PIDController;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;

public class ShooterMap {

	private static final SpeedController CAM_MOTOR = new Jaguar(Shooter.CAM_CHANNEL);
	private static final SpeedController FLYWHEEL_MOTOR = new Jaguar(Shooter.FLYWHEEL_CHANNEL);
	private static final SpeedController FLYWHEEL_MOTOR2 = new Jaguar(4);
	private static final Encoder CAM_ENCODER = new Encoder(Shooter.CAM_ENC_CH_A, Shooter.CAM_ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	private static final DigitalInput BALL_LIM_SWITCH = new DigitalInput(Shooter.BALL_LIMIT_SWITCH);
	private static final Encoder FLYWHEEL_ENCODER = new Encoder(Shooter.FLYWHEEL_ENC_CH_A,Shooter.FLYWHEEL_ENC_CH_B,false,CounterBase.EncodingType.k4X);

	private ShooterState state;
	private Timer armTimer;
	private Timer shootTimer;
	private PIDController camPID;
	
	public enum ShooterState {
		INTAKE, EJECT, IDLE, ARMING, ARMED, SHOOTING;
	}

	public ShooterMap() {
		state = ShooterState.IDLE;
		CAM_ENCODER.setReverseDirection(true);
		CAM_MOTOR.setInverted(true);
		armTimer = new Timer();
		shootTimer = new Timer();
		
		camPID = new PIDController("Shooter_Cam", 75, 0, 1.2, 1);
		resetEnc();
		camPID.setTarget(0);
		CAM_ENCODER.setDistancePerPulse(1/Shooter.CAM_PPD);
		FLYWHEEL_ENCODER.setDistancePerPulse(1/Shooter.FLYWHEEL_PPR);
		FLYWHEEL_MOTOR2.setInverted(false);
	}
	
	public ShooterState getState(){
		return state;
	}

	public void setCamSpeed(double speed) {
		CowDash.setNum("Shooter_camSpeed", speed);
		CAM_MOTOR.set(speed);
	}

	public void setWheelSpeed(double speed) {
		CowDash.setNum("Shooter_wheelSpeed", speed);
		FLYWHEEL_MOTOR.set(speed);
		FLYWHEEL_MOTOR2.set(speed);
	}

	public boolean getSwitchHit() {
		return !BALL_LIM_SWITCH.get();
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
	
	public double getFlyEncRate(){
		return FLYWHEEL_ENCODER.getRate();
	}

	public void resetEnc() {
		CAM_ENCODER.reset();
	}
	
	

	public void arm() {
		if (state == ShooterState.IDLE || state == ShooterState.EJECT) {
			armTimer.reset();
			armTimer.start();
			state = ShooterState.ARMING;
		} else {
			//DriverStation.reportError("You cannot Arm unless You're Up and Idle", false);
		}
	}

	public void intake() {
		state = ShooterState.INTAKE;
	}

	public void eject() {
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
		setCamSpeed(camPID.feedAndGetValue(-getCamEncDist()));
	}
	
	private void runFlywheelControl() {
		setWheelSpeed(0);
	}
	

	public void step() {
		switch (state) {
		case INTAKE:
			CowDash.setString("Shooter_state", "INTAKE");
//			if (getSwitchHit()) {
//				state = ShooterState.IDLE;
//				break;
//			} // FIXED: This really doesn't help, we found -Thad
			camPID.setTarget(CowDash.getNum("Cam_Intake_Angle", -30));
			setWheelSpeed(-1*CowDash.getNum("Shooter_intakePower", 0.5));
			break;
		case EJECT:
			CowDash.setString("Shooter_state", "EJECT");
			camPID.setTarget(CowDash.getNum("Cam_Eject_Angle", -180));
			setWheelSpeed(+CowDash.getNum("Shooter_ejectPower", 1.0));
			break;
		case SHOOTING:
			CowDash.setString("Shooter_state", "SHOOTING");
			camPID.setTarget(CowDash.getNum("Cam_Shoot_Angle", -180));
			if ( shootTimer.get() > 2) {
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
			if (armTimer.get() > CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				setWheelSpeed(+CowDash.getNum("Shooter_shootPower", 1.0));
			} else {
				setWheelSpeed(-CowDash.getNum("Shooter_shootIntakePower", 0.4));
			}
			
			if (armTimer.get() > CowDash.getNum("Shooter_armTime", 2.0)+CowDash.getNum("Shooter_armIntakeTime", 0.5)) {
				// 2 Seconds, Can be changed ( ADD TO CONFIG )
				armTimer.stop();
				armTimer.reset();
				state = ShooterState.ARMED;
			}
			break;
		case ARMED:
			camPID.setTarget(CowDash.getNum("Cam_Arm_Angle", 10));
			CowDash.setString("Shooter_state", "ARMED");
			setWheelSpeed(+CowDash.getNum("Shooter_shootPower", 1.0));
			break;
		case IDLE:
			CowDash.setString("Shooter_state", "IDLE");
			camPID.setTarget(CowDash.getNum("Cam_Idle_Angle", 0));
			setWheelSpeed(0);
			break;
		default:
			break;
		}
		
		runFlywheelControl();

		runCamPID();
	}

	
}
