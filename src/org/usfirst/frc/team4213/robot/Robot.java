
package org.usfirst.frc.team4213.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	//TurretMap turret;
	ShooterMap shooter;
	AIRFLOController driverController;
	Xbox360Controller gunnerController;
	DriveController driveTrain;

	// Camera Controller
	public static CowCamServer camServer;
	// The Thread Pool / Executor of Tasks to Use
	public ExecutorService executor;
	// A new Camera Controller for the Shooter
	public CowCamController shooterCamController;

	static {
		// Loads the OpenCV Library from The RoboRIO's Local Lib Directory
		System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
	}

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		driverController = new AIRFLOController(0);
		gunnerController = new Xbox360Controller(1);

		camServer = new CowCamServer(1180);
		executor = Executors.newWorkStealingPool();
		shooterCamController = new CowCamController(0, 20, CowCamController.ImageTask.SHOOTER);

		// TODO: Read-in and Populate the RobotMap from a textFile
		//turret = new TurretMap();
		shooter = new ShooterMap();
		driveTrain = new DriveController(new DriveMap());

		camServer.start(shooterCamController, executor);

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
		
		//TODO: Move all the OperatorController code to the OperatorController.java... maybe
		
		 if (driverController.getButtonTripped(1)) { shooter.intake(); }
		 
		 if (driverController.getButtonTripped(2)) { shooter.eject(); }
		 
		 if (driverController.getButtonTripped(5)) { shooter.arm(); }
		 
		 if (driverController.getButtonTripped(6)) { shooter.shoot(); }
		 
		 
		 if (driverController.getButtonTripped(4)){
			 shooter.idle();
		 }
		 
		 
		 if (driverController.getButtonReleased(1) ||
		 driverController.getButtonReleased(2) ||
		 driverController.getButtonReleased(5)) { shooter.idle(); }
		 
		 shooter.step();

		 
		 //TODO: Enable the Camera for the gunner and then add the crosshairs
		 //TODO: We need to calibrate the shooter to the crosshairs overlay
		 
		 
		/*turret.setPitchSpeed(driverController.getRY());
		if (driverController.getButtonTripped(5)) {
			turret.engage();
		}

		
		if (driverController.getButtonTripped(6)) {
			turret.idle();
		}

		if (driverController.getLY() < 0) {
			turret.bumpTurretUp();
		}

		if (driverController.getLY() > 0) {
			turret.bumpTurretDown();
		}
		
		CowDash.setBool("TEST", driverController.getButtonTripped(1));
		

		driveTrain.drive(driverController, true);

		turret.step();*/
	}

	/**
	 * This function is called periodically during test mode Coders and
	 * Developers use this during their tests
	 * 
	 * But here it's for mechanical team to test their shit
	 */
	@Override
	public void testPeriodic() {
		//turret.setPitchSpeed(driverController.getLY());
		if(driverController.getButtonReleased(1)){
			DriverStation.reportError("/n Released", false);
		}
		if(driverController.getButtonTripped(1)){
			DriverStation.reportError("/n Pressed", false);
		}

		
	}

}
