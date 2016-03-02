package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.SpeedController;

public class TurretMap { // Replace these with the Constants
	private static final SpeedController YAW_MOTOR = new Jaguar(6);
	private static final SpeedController PITCH_MOTOR = new Jaguar(2);
	private static final Encoder YAW_ENC = new Encoder(Turret.Yaw_Motor.ENC_CH_A, Turret.Yaw_Motor.ENC_CH_B, true,
			CounterBase.EncodingType.k4X);
	private static final Encoder PITCH_ENC = new Encoder(2, 3, true, CounterBase.EncodingType.k4X);
	private static final PIDController PITCH_PID = new PIDController("Turret_Pitch", 100, 0,0,1);
	private static final PIDController YAW_PID = new PIDController("Turret_Yaw", 100, 0,0,1);

	private boolean overrideYawPID;
	private boolean overridePitchPID;
	
	private TurretState state;

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
		overridePitchPID = false;
		overrideYawPID = false;
	}
	
	public TurretState getState(){
		return state;
	}
	
	public void setRawYawSpeed(double speed){
		CowDash.setNum("Turret_yawSpeed", speed);
		YAW_MOTOR.set(-speed); // Invert
	}
	
	public void setRawPitchSpeed(double speed){
		CowDash.setNum("Turret_pitchSpeed", speed);
		PITCH_MOTOR.set(speed); 
	}

	public void setYawSpeed(double speed) {
		// Speed Limits
		if(speed > Turret.Yaw_Motor.MAX_SPEED){
			speed = Turret.Yaw_Motor.MAX_SPEED;
		}else if(speed < -1 * Turret.Yaw_Motor.MAX_SPEED){
			speed = -Turret.Yaw_Motor.MAX_SPEED;
		}
		
		setRawYawSpeed(speed);
	}
	

	public void setPitchSpeed(double speed) {
		CowDash.setNum("Turret Up Speed", speed);
		if((speed > 0 && getPitchEncDistance() > Turret.Pitch_Motor.MAX_ANGLE) || (speed < 0 && getPitchEncDistance() < 0)){
			setRawPitchSpeed(0);
			return;
		}
		
		if( state == TurretState.ENGAGED ){
			if(speed<0 && getPitchEncDistance() < Turret.Pitch_Motor.MIN_ANGLE ){
				setRawPitchSpeed(0);
				return;
			}	
		}
		
		setRawPitchSpeed(speed);
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
		if (PITCH_PID.getTarget() < Turret.Pitch_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			PITCH_PID.bumpTarget(Turret.Pitch_Motor.BUMP_AMT);
			//DriverStation.reportError("\n Moving Turret Up", false);
		} else {
			//DriverStation.reportError("\n At Max Pitch Angle", false);
		}
	}

	public void bumpTurretDown() {
		if (PITCH_PID.getTarget() > Turret.Pitch_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			PITCH_PID.bumpTarget(-Turret.Pitch_Motor.BUMP_AMT);
			//DriverStation.reportError("\n Moving Turret Down", false);
		} else {
			//DriverStation.reportError("\n At Min Pitch Angle", false);
		}
	}

	public void bumpTurretRight() {
		if (YAW_PID.getTarget() < Turret.Yaw_Motor.MAX_ANGLE && state == TurretState.ENGAGED) {
			YAW_PID.bumpTarget(Turret.Yaw_Motor.BUMP_AMT);
		} else {
			//DriverStation.reportError("\n At Max Yaw Angle", false);
		}
	}

	public void bumpTurretLeft() {
		if (YAW_PID.getTarget() > Turret.Yaw_Motor.MIN_ANGLE && state == TurretState.ENGAGED) {
			YAW_PID.bumpTarget(-Turret.Yaw_Motor.BUMP_AMT);
		} else {
			//DriverStation.reportError("\n At Min Yaw Angle", false);
		}
	}
	
	public void manualPitchBump(double speed){
		if(state == TurretState.ENGAGED){
			overridePitchPID = true;
			setPitchSpeed(speed * 0.5); // TODO MOVE THESE INTO MAP
		}
	}
	
	public void manualYawBump(double speed){
		if(state == TurretState.ENGAGED){
			overrideYawPID = true;
			setYawSpeed(speed * 0.5);
			
		}
	}

	private void runPitchPID() {
		if(overridePitchPID){
			PITCH_PID.setTarget(getPitchEncDistance());
		}
		setPitchSpeed(PITCH_PID.feedAndGetValue(getPitchEncDistance()));
		overridePitchPID = false;
	}

	private void runYawPID() {
		if(overrideYawPID){
			YAW_PID.setTarget(getYawEncDistance());
		}
		setYawSpeed(YAW_PID.feedAndGetValue(getYawEncDistance()));
		overrideYawPID = false;
	}

	public void step() {
		switch (state) {
		case IDLING:
			CowDash.setString("Turret_state", "Idling");
			YAW_PID.setTarget(0);
			if (Math.abs(YAW_PID.getError()) < Turret.Yaw_Motor.ABS_TOLERANCE) {
				PITCH_PID.setTarget(0);
				if (Math.abs(PITCH_PID.getError()) < Turret.Pitch_Motor.ABS_TOLERANCE) {
					state = TurretState.IDLE;
				}
			}
			break;
		case ENGAGING:
			CowDash.setString("Turret_state", "Engaging");
			PITCH_PID.setTarget(Turret.Pitch_Motor.MIN_ANGLE);
			if (Math.abs(PITCH_PID.getError()) < Turret.Pitch_Motor.ABS_TOLERANCE) {
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
