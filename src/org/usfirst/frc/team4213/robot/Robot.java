
package org.usfirst.frc.team4213.robot;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	IntakeMap intake;
	AIRFLOController driverController;
	Xbox360Controller gunnerController;
	DriveController driveTrain;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		driverController = new AIRFLOController(0);
		gunnerController = new Xbox360Controller(1);

		// TODO: Read-in and Populate the RobotMap from a textFile
		intake = new IntakeMap();
		driveTrain = new DriveController(new DriveMap());

	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {

	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
	}

	/**
	 * This function is called periodically during operator control Working
	 * Calls for Drivers to do their stuff
	 */
	@Override
	public void teleopPeriodic() {

		driveTrain.drive(driverController, true);

	}

	/**
	 * This function is called periodically during test mode Coders and
	 * Developers use this during their tests
	 */
	@Override
	public void testPeriodic() {

	}

}
