package org.usfirst.frc.team4213.robot.systems;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret.Yaw_Motor;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.CANTalon;

public class TurretYawMap {
	
	public static final AnalogPotentiometer STRING_POT = new AnalogPotentiometer(0, 1000 , 0);
	
	public static final int START_POSITION = 500;
	
	public static final int UPPER_LIMIT = 400;
	
	public static final int LOWER_LIMIT = 600;
		
	public static double CURRENT_POSITION = STRING_POT.get();
	
	public static final CANTalon YAW_MOTOR = new CANTalon(Yaw_Motor.MOTOR_CHANNEL);
	
	public static double CLOCK_WISE_SPEED = 0.4;

	public static double COUNTER_CLOCK_WISE_SPEED = -0.4;
	
	
	//TODO add string potentiometer

}
