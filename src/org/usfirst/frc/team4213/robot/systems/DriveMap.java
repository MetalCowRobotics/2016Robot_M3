package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class DriveMap{

	public static final SpeedController LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	public static final SpeedController RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);

}
