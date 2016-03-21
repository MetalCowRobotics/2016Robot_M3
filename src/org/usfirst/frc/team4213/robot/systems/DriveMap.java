package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Drivetrain;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class DriveMap {

	private static final SpeedController LEFT_MOTOR = new Talon(Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_MOTOR = new Talon(Drivetrain.RIGHT_MOTOR_CHANNEL);

	public void setLeftMotorSpeed(double speed) {
		LEFT_MOTOR.set(speed);
	}

	public void setRightMotorSpeed(double speed) {
		RIGHT_MOTOR.set(speed);
	}
}
