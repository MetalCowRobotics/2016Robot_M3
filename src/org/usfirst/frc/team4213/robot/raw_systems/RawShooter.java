package org.usfirst.frc.team4213.robot.raw_systems;

import org.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public interface RawShooter {

	SpeedController FLYWHEEL_MOTOR_2 = new CANTalon(Shooter.FLYWHEEL_CHANNEL_2);
	SpeedController FLYWHEEL_MOTOR = new CANTalon(Shooter.FLYWHEEL_CHANNEL);
	SpeedController CAM_MOTOR = new CANTalon(Shooter.CAM_CHANNEL);
	
	Encoder FLYWHEEL_ENCODER = new Encoder(Shooter.FLYWHEEL_ENC_CH_A, Shooter.FLYWHEEL_ENC_CH_B,
			false, CounterBase.EncodingType.k4X);
	Encoder CAM_ENCODER = new Encoder(Shooter.CAM_ENC_CH_A, Shooter.CAM_ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	
	static void setCamSpeed(double speed) {
		CowDash.setNum("Shooter_camSpeed", speed);
		CAM_MOTOR.set(speed);
	}

	static void setCurrentWheelSpeed(double speed) {
		
		CowDash.setNum("Shooter_wheelSpeed", speed);
		FLYWHEEL_MOTOR.set(speed);
		FLYWHEEL_MOTOR_2.set(speed);
	}

	static double getCamEncValue() {
		return CAM_ENCODER.get();
	}

	static double getCamEncDist() {
		return CAM_ENCODER.getDistance();
	}

	static double getFlyEncValue() {
		return FLYWHEEL_ENCODER.get();
	}

	static double getFlyEncDist() {
		return FLYWHEEL_ENCODER.getDistance();
	}

	static double getFlyEncRate() {
		return FLYWHEEL_ENCODER.getRate();
	}

	static void resetEnc() {
		CAM_ENCODER.reset();
	}
	
	static void reverseCamEnc(boolean reverse){
		CAM_ENCODER.setReverseDirection(reverse);
	}
	
	static void reverseCamMotor(boolean reverse){
		CAM_MOTOR.setInverted(reverse);
	}
	
	static void setCamEncDistPerPulse(double dist){
		CAM_ENCODER.setDistancePerPulse(dist);
	}
	
	static void setFlyEncDistPerPulse(double dist){
		CAM_ENCODER.setDistancePerPulse(dist);
	}
	
	static double getFlyMotorSpeed(){
		return FLYWHEEL_MOTOR.get();
	}

}
