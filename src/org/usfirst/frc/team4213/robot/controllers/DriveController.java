package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.lib14.Xbox360Controller;
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
	 *            - xbox360 Controller that will be used for driving
	 * @param squaredUnits
	 *            - should units be squared, provides more fine control at lower
	 *            speeds
	 */
	public void drive (Xbox360Controller controller, boolean squareUnits) {
		double throttle = controller.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,Drivetrain.SPRINT_SPEED);
                
                //System.out.println("Throttle: " + Double.toString(throttle) + "\n");
		
		if (controller.getButton(GamepadButton.LT)){			//Go into half-arcade
                        driveMap.setLeftMotorSpeed(controller.getRX() * throttle);
			driveMap.setRightMotorSpeed(controller.getRX() * throttle);
		} else {											//Stay in regular drive
			driveMap.setLeftMotorSpeed(-controller.getLY() * throttle);
			driveMap.setRightMotorSpeed(controller.getRY() * throttle);
		}
	}
}
