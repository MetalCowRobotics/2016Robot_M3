package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.robot.raw_systems.RawDrive;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Drivetrain;


public class DriveController {

	private DriveController() {}
	
	/**
	 * Arcade style driving for the DriveTrain.
	 * 
	 * @param driverController
	 *            - AIRFLO Controller that will be used for driving
	 * @param squaredUnits
	 *            - should units be squared, provides more fine control at lower
	 *            speeds
	 */
	public static void drive (AIRFLOController controller, boolean squareUnits) {
		
		double throttle = controller.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,
				Drivetrain.SPRINT_SPEED);
		
		if (controller.getButton(GamepadButton.A) || controller.getButton(GamepadButton.LT)){			//Go into half-arcade
			RawDrive.setLeftMotorSpeed(-controller.getLY() * throttle);
			RawDrive.setRightMotorSpeed(controller.getLY() * throttle);
		} else {											//Stay in regular drive
			RawDrive.setLeftMotorSpeed(-controller.getRY() * throttle);
			RawDrive.setRightMotorSpeed(controller.getLY() * throttle);
		}
	}

}
