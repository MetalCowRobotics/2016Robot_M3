package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class IntakeMap {

public static final SpeedController ROLLER_MOTOR = new Talon(Intake.ROLLER_MOTOR_CHANNEL);
public static final SpeedController PITCH_MOTOR = new Talon(Intake.ROLLER_MOTOR_CHANNEL);

	public void setRollerSpeed(double speed){
		ROLLER_MOTOR.set(speed);
	}
	
	public void setPitchSpeed(double speed){
		PITCH_MOTOR.set(speed);
	}
}
