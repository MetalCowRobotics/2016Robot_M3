package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Drivetrain;

public class DriveMap{

	public static final SpeedController LEFT_MOTOR = new Talon(Drivetrain.LEFT_MOTOR);
	public static final SpeedController RIGHT_MOTOR = new Talon(Drivetrain.RIGHT_MOTOR);

}
