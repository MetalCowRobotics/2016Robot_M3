package org.usfirst.frc.team4213.robot.raw_systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public interface RawIntake {
	SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);
	
	static void setRollerSpeed(double speed) {
		ROLLER_MOTOR.set(speed);
	}

	static void setPitchSpeed(double speed) {
		PITCH_MOTOR.set(speed);
	}
	
	static double getEncPosition() {
		return PITCH_ENCODER.get();
	}

	static double getEncDistance() {
		return PITCH_ENCODER.getDistance();
	}

	static void resetEnc() {
		PITCH_ENCODER.reset();
	}

	static void setEncDistPerPulse(double distancePerPulse) {
		PITCH_ENCODER.setDistancePerPulse(distancePerPulse);
	}
	
	static void setReverseDirection(boolean reverse){
		PITCH_ENCODER.setReverseDirection(reverse);
	}
}
