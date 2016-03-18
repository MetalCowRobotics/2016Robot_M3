package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class DriveMap{

	private static final SpeedController LEFT_MOTOR = new Talon(RobotMap.Drivetrain.LEFT_MOTOR_CHANNEL);
	private static final SpeedController RIGHT_MOTOR = new Talon(RobotMap.Drivetrain.RIGHT_MOTOR_CHANNEL);

//	/**
//	 * This one will actually move the robot.
//	 * @param direction - Forwards or Backwards [1,-1]
//	 * @param rotation - Left or Right [1, -1]
//	 * @param throttle - Modifier for speed of direction/rotation [0-1]
//	 * @param squareUnits - True, finer values at lower speeds
//	 */
//	public void arcDrive(double direction, double rotation, double throttle, boolean squareUnits){	
//		arcadeDrive(direction*throttle, rotation*throttle, squareUnits); //ArcadeMode
//	}
//	
//	public void tDrive(double leftStick, double rightStick, double throttle, boolean squareUnits){
//		tankDrive(leftStick*throttle, rightStick * throttle, squareUnits); // TankDrive
//	}
	public void setLeftMotorSpeed(double speed){
		LEFT_MOTOR.set(speed);
	}
	public void setRightMotorSpeed(double speed){
		RIGHT_MOTOR.set(speed);
	}
	
	public double getRightMotorSpeed(){
		return RIGHT_MOTOR.get();
	}
	
	public double getLeftMotorSpeed(){
		return -LEFT_MOTOR.get();
	}
}
