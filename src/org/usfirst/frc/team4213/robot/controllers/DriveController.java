package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.DriveMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class DriveController{  //TODO: Extends RobotDrive to get other methods

	
	//Set the top speeds for the driver
	double topSpeedNormal = .6;
	double topSpeedCrawl = .3;
	double topSpeedSprint = 1;
	
	RobotDrive robotDrive;
	
	/**
	 * Create the DriveTrain, this one has two motors
	 * @param leftMotor - the speed controller for the left motor
	 * @param rightMotor - the speed controller for the right motor
	 */
	public DriveController(SpeedController leftMotor, SpeedController rightMotor){
		//TODO: see class definition comment ---- super(leftMotor, rightMotor);  
		robotDrive = new RobotDrive(leftMotor, rightMotor);
	}

	/**
	 * Create the DriveTrain, this one has two motors
	 * @param DriveMap - Takes in a DriveMap and then identifies the motors.
	 */
	public DriveController(DriveMap driveMap){
		this(driveMap.LEFT_MOTOR, driveMap.RIGHT_MOTOR);
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
		robotDrive.arcadeDrive(direction*throttle, rotation*throttle, squareUnits); //ArcadeMode
	}

	
	
	
	

	

}
