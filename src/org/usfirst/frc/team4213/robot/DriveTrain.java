package org.usfirst.frc.team4213.robot;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.Xbox360Controller;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class DriveTrain{
	
	//the actual logical drive that matches the physical drive
	private RobotDrive drive;
	
	//Set the top speeds for the driver
	double topSpeedNormal = .6;
	double topSpeedCrawl = .3;
	double topSpeedSprint = 1;
	
	/**
	 * Create the DriveTrain, this one has two motors
	 * @param leftMotor - the speed controller for the left motor
	 * @param rightMotor - the speed controller for the right motor
	 */
	public DriveTrain(SpeedController leftMotor, SpeedController rightMotor){
		drive = new RobotDrive(leftMotor, rightMotor);
	}
	
	/**
	 * Arcade style driving for the DriveTrain.
	 * @param driverController - AIRFLO Controller that will be used for driving
	 * @param squaredUnits - should units be squared, provides more fine control at lower speeds
	 */
	public void drive(AIRFLOController driverController, boolean squareUnits){
		
		//Read the values from the controller
		double direction = driverController.getLY();
		double rotation = driverController.getRX();
		double throttle = driverController.getThrottle(topSpeedNormal, topSpeedCrawl, topSpeedSprint);
		
		drive(direction,rotation,throttle,squareUnits);
	}
	
	/**
	 * Arcade style driving for the DriveTrain.
	 * @param driverController - Xbox360 Controller that will be used for driving
	 * @param squaredUnits - should units be squared, provides more fine control at lower speeds
	 */
	public void drive(Xbox360Controller driverController, boolean squareUnits){
		
		//Read the values from the controller
		double direction = driverController.getLY();
		double rotation = driverController.getRX();
		double throttle = driverController.getThrottle(topSpeedNormal, topSpeedCrawl, topSpeedSprint);
		
		drive(direction,rotation,throttle,squareUnits);
	}
		
	/**
	 * This one will actually move the robot.
	 * @param direction - Forwards or Backwards [1,-1]
	 * @param rotation - Left or Right [1, -1]
	 * @param throttle - Modifier for speed of direction/rotation [0-1]
	 * @param squareUnits - True, finer values at lower speeds
	 */
	private void drive(double direction, double rotation, double throttle, boolean squareUnits){	
		//Move the robot
		drive.arcadeDrive(direction*throttle, rotation*throttle, squareUnits); //ArcadeMode
	}

	
	
	
	

	

}
