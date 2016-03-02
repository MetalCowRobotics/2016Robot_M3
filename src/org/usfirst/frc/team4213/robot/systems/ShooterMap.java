package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

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
	private static final Encoder CAM_ENCODER = new Encoder(Shooter.ENC_CH_A, Shooter.ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	private static final DigitalInput BALL_LIM_SWITCH = new DigitalInput(Shooter.LIMIT_SWITCH);

	private int desiredCamAngle;
	private ShooterState state;
	private double error;
	private Timer timer;
	

	public enum ShooterState {
		INTAKE, EJECT, IDLE, ARMING, ARMED, SHOOTING;
	}

	public ShooterMap() {
		state = ShooterState.IDLE;
		// shooter.camMotor.enableBrakeMode(true); // DOES THIS WORK ????
		//CAM_ENCODER.setReverseDirection(true);
		//CAM_MOTOR.setInverted(true);
		timer = new Timer();
		resetEnc();
		desiredCamAngle = 0;
		CAM_ENCODER.setDistancePerPulse(1 / Shooter.COUNT_PER_DEG);
	}
	
	public ShooterState getState(){
		return state;
	}

	public void setCamSpeed(double speed) {
		CAM_MOTOR.set(speed);
	}

	public void setWheelSpeed(double speed) {
		FLYWHEEL_MOTOR.set(speed);
		FLYWHEEL_MOTOR2.set(-speed);
	}

	public boolean getSwitchHit() {
		return !BALL_LIM_SWITCH.get();
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
			timer.start();
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
			timer.start();
			state = ShooterState.SHOOTING;
		} else {
			// TODO VIBE CONTROLLER
			DriverStation.reportError("Shooter is Not Armed : Ball Cannot be Shot", false);
		}
	}

	public void idle() {
		state = ShooterState.IDLE;
	}

	private void runCamPID() {
		// TODO: Swap to PIDController
		error = -desiredCamAngle - getEncDist();
		setCamSpeed(error / 180);
	}
	
	// FIXME: The ball can only be fired once per reboot for some weird reason.

	public void step() {
		switch (state) {
		case INTAKE:
			CowDash.setString("Shooter_state", "INTAKE");
			/*if (getSwitchHit()) {
				state = ShooterState.IDLE;
				break;
			}*/ // FIXED: This really doesn't help, we found -Thad
			desiredCamAngle = 0;
			setWheelSpeed(Shooter.INTAKE_SPEED);
			break;
		case EJECT:
			// FIXME: "Zoo-ZOO!" intake then eject (would help, we found from testing) -Thad
			CowDash.setString("Shooter_state", "EJECT");
			desiredCamAngle = 360;
			setWheelSpeed(Shooter.EJECT_SPEED);
			break;
		case SHOOTING:
			CowDash.setString("Shooter_state", "SHOOTING");
			desiredCamAngle = 360;
			if ( timer.get() > 2) {
				// 1 Second of Wheel Spinning. ( ADD TO CONFIG )
				timer.stop();
				timer.reset();
				resetEnc(); // TODO: This could (and I think it is) leading to weird things. Consider modulo? Not sure why you're doing this. -Thad
				idle();
				desiredCamAngle = 0;
			}
			break;
		case ARMING:
			CowDash.setString("Shooter_state", "ARMING");
			setWheelSpeed(Shooter.SHOOT_SPEED);
			DriverStation.reportError("/n Time:" + (timer.get()), false);
			if (timer.get() > 2) {
				// 2 Seconds, Can be changed ( ADD TO CONFIG )
				timer.stop();
				timer.reset();
				state = ShooterState.ARMED;
			}
			break;
		case ARMED:
			CowDash.setString("Shooter_state", "ARMED");
			setWheelSpeed(Shooter.SHOOT_SPEED);
			break;
		case IDLE:
			CowDash.setString("Shooter_state", "IDLE");
			desiredCamAngle = 120;
			setWheelSpeed(0);
			break;
		default:
			break;
		}
		
		

		runCamPID();
	}
}
