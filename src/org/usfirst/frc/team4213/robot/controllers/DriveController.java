package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.GamepadButton;
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
	public void drive (AIRFLOController controller, boolean squareUnits) {
		double throttle = controller.getThrottle(Drivetrain.NORMAL_SPEED, Drivetrain.CRAWL_SPEED,
				Drivetrain.SPRINT_SPEED);
		
		if (controller.getButton(GamepadButton.A)){			//Go into half-arcade
			driveMap.setLeftMotorSpeed(-controller.getLY() * throttle);
			driveMap.setRightMotorSpeed(controller.getLY() * throttle);
		} else {											//Stay in regular drive
			driveMap.setLeftMotorSpeed(-controller.getRY() * throttle);
			driveMap.setRightMotorSpeed(controller.getLY() * throttle);
		}
	}
}
