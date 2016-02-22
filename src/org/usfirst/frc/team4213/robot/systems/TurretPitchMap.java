package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.CANTalon;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret.Pitch_Motor;


public class TurretPitchMap {
public static final CANTalon PITCH_MOTOR = new CANTalon(Pitch_Motor.MOTOR_CHANNEL);
public static double getEncoder(){
	return PITCH_MOTOR.getEncPosition();
}

public static final double UP_SPEED = 0.4;

public static final double DOWN_SPEED = -0.4;
}