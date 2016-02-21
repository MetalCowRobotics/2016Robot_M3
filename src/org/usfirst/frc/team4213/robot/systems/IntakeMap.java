package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class IntakeMap {

public static final SpeedController INTAKE_MOTOR = new Talon(RobotMap.Intake.MOTOR_CHANNEL);

public static final double INTAKE_SPEED = 1;

public static final double EJECT_SPEED = -1;

}
