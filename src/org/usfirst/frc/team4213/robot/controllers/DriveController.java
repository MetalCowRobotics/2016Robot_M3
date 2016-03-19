package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Drivetrain;


public class DriveController {

	private DriveMap driveMap;
	private final static double rampRate = 0.1;
	public DriveController(DriveMap driveMap) {
		this.driveMap = driveMap;
	}

	/**
	 * Arcade style driving for the DriveTrain.
	 * 
	 * @param driverController
	 *            - AIRFLO Controller that will be used for driving
	 * @param squaredUnits
	 *            - should units be squared, provides more fine control at lower
	 *            speeds
	 */
	public void drive(AIRFLOController controller, boolean squareUnits) {
////		
//		double throttle = controller.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,
//				Drivetrain.SPRINT_SPEED);
//		
//		final double leftSpeed = controller.getRY() * throttle;
//		final double rightSpeed = controller.getLY() * throttle;
//		
//		double leftSetSpeed = leftSpeed;
//		double rightSetSpeed = rightSpeed;
//		
//		if(leftSpeed > driveMap.getLeftMotorSpeed()){
//			if(Math.abs(driveMap.getLeftMotorSpeed() - leftSpeed) > rampRate){
//				leftSetSpeed = driveMap.getLeftMotorSpeed() + rampRate;
//			}
//		}else if(leftSpeed <= driveMap.getLeftMotorSpeed()){
//			if(Math.abs(driveMap.getLeftMotorSpeed() - leftSpeed) < rampRate){
//				leftSetSpeed = driveMap.getLeftMotorSpeed() - rampRate;
//			}
//		}
//		
//		if(rightSpeed > driveMap.getRightMotorSpeed()){
//			if(Math.abs(driveMap.getRightMotorSpeed() - rightSpeed) > rampRate){
//				rightSetSpeed = driveMap.getRightMotorSpeed() + rampRate;
//			}
//		}else if(rightSpeed <= driveMap.getRightMotorSpeed()){
//			if(Math.abs(driveMap.getRightMotorSpeed() - rightSpeed) < rampRate){
//				rightSetSpeed = driveMap.getRightMotorSpeed() - rampRate;
//			}
//		}
//		
		double throttle = controller.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,
				Drivetrain.SPRINT_SPEED);
		driveMap.setLeftMotorSpeed(-controller.getRY() * throttle);
		driveMap.setRightMotorSpeed(controller.getLY() * throttle);
//		double leftStick = driverController.getLY();
//
//		// Read the values from the controller
//		if(driverController.getButtonToggled(10)){
//			double rotation = -driverController.getRX();
//			
//			driveMap.arcDrive(leftStick, rotation, throttle, squareUnits);
//			
//		}else{
//			double rightStick = driverController.getRY();	
//			driveMap.tDrive(leftStick, rightStick, throttle, squareUnits);
//		}

	}
		

	/**
	 * Arcade style driving for the DriveTrain.
	 * 
	 * @param driverController
	 *            - Xbox360 Controller that will be used for driving
	 * @param squaredUnits
	 *            - should units be squared, provides more fine control at lower
	 *            speeds
	 */
	public void drive(Xbox360Controller driverController, boolean squareUnits) {

//		// Read the values from the controller
//		double direction = driverController.getLY();
//		double rotation = driverController.getRX();
//		double throttle = driverController.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,
//				Drivetrain.SPRINT_SPEED);
//
//		driveMap.arcDrive(direction, rotation, throttle, squareUnits);
	}

}
