package org.usfirst.frc.team4213.robot.raw_systems;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.PIDController;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public interface RawTurret {
	static SpeedController YAW_MOTOR = new CANTalon(Turret.Yaw_Motor.MOTOR_CHANNEL);
	static SpeedController PITCH_MOTOR = new CANTalon(Turret.Pitch_Motor.MOTOR_CHANNEL);
	static Encoder YAW_ENC = new Encoder(Turret.Yaw_Motor.ENC_CH_A, Turret.Yaw_Motor.ENC_CH_B, true,
			CounterBase.EncodingType.k4X);
	static Encoder PITCH_ENC = new Encoder(Turret.Pitch_Motor.ENC_CH_A, Turret.Pitch_Motor.ENC_CH_B, true,
			CounterBase.EncodingType.k4X);
	
	static void setYawSpeed(double speed) {
		CowDash.setNum("Turret_yawSpeed", speed);
		YAW_MOTOR.set(-speed); // Invert
	}

	static void setPitchSpeed(double speed) {
		CowDash.setNum("Turret_pitchSpeed", speed);
		PITCH_MOTOR.set(speed);
	}
	
	static int getYawEncPosition() {
		return YAW_ENC.get();
	}

	static double getYawEncDistance() {
		return YAW_ENC.getDistance();
	}

	static int getPitchEncPosition() {
		return PITCH_ENC.get();
	}

	static double getPitchEncDistance() {
		return PITCH_ENC.getDistance();
	}

	static void resetYawEnc(){
		YAW_ENC.reset();
	}
	static void resetPitchEnc(){
		PITCH_ENC.reset();
	}
	
	static void setYawEncDistPerPulse(double dist){
		YAW_ENC.setDistancePerPulse(dist);
	}
	
	static void setPitchEncDistPerPulse(double dist){
		PITCH_ENC.setDistancePerPulse(dist);
	}
	
	static void setPitchEncReverse(boolean reverse){
		PITCH_ENC.setReverseDirection(reverse);
	}
}
