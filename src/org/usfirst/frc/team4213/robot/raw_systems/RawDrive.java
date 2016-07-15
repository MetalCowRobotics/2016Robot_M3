package org.usfirst.frc.team4213.robot.raw_systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public interface RawDrive {
	SpeedController LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	SpeedController RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);

	public static void setLeftMotorSpeed(double speed){
		LEFT_MOTOR.set(speed);
	}
	public static void setRightMotorSpeed(double speed){
		RIGHT_MOTOR.set(speed);
	}
	
	public static double getRightMotorSpeed(){
		return RIGHT_MOTOR.get();
	}
	
	public static double getLeftMotorSpeed(){
		return LEFT_MOTOR.get();
	}
}
