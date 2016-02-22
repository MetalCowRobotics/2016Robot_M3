package org.usfirst.frc.team4213.robot.systems;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret.Yaw_Motor;

import edu.wpi.first.wpilibj.CANTalon;

public class TurretYawMap {

	public static final CANTalon YAW_MOTOR = new CANTalon(Yaw_Motor.MOTOR_CHANNEL);
	
	//TODO add string potentiometer
	public static final double CLOCK_WISE_SPEED = 1;

	public static final double COUNTER_CLOCK_WISE_SPEED = -1;
}
