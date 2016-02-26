package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Drivetrain;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class DriveController extends RobotDrive{

	
	
	/**
	 * Create the DriveTrain, this one has two motors
	 * @param leftMotor - the speed controller for the left motor
	 * @param rightMotor - the speed controller for the right motor
	 */
	public DriveController(SpeedController leftMotor, SpeedController rightMotor){
		super(leftMotor, rightMotor);
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
		double throttle = driverController.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED, Drivetrain.SPRINT_SPEED);
		
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
		double throttle = driverController.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED, Drivetrain.SPRINT_SPEED);
		
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
		this.arcadeDrive(direction*throttle, rotation*throttle, squareUnits); //ArcadeMode
	}

	
	
	
	

	

}
