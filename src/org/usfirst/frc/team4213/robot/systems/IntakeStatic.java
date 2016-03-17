package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedController;

public class IntakeStatic {
	public static final SpeedController ROLLER_MOTOR = new CANTalon(Intake.ROLLER_MOTOR_CHANNEL);
	public static final SpeedController PITCH_MOTOR = new CANTalon(Intake.PITCH_MOTOR_CHANNEL);
	public static final Encoder PITCH_ENCODER = new Encoder(Intake.ENCODER_CH_A, Intake.ENCODER_CH_B, false, CounterBase.EncodingType.k4X);
}
