
package org.usfirst.frc.team4213.robot;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.GamepadButton;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.controllers.OperatorController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	TurretMap turret;
	IntakeMap intake;
	ShooterMap shooter;
	
	CowGamepad driverController;
	CowGamepad gunnerController;
	
	DriveController driveTrain;
	OperatorController ballSystems;
	
	// Camera Controller
	public static CowCamServer camServer;
	// The Thread Pool / Executor of Tasks to Use
	public ExecutorService executor;
	// A new Camera Controller for the Shooter
	public CowCamController shooterCamController;
	boolean allowedToSave = false;

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
		
		CowDash.load();

		driverController = new Xbox360Controller(0);
		gunnerController = new Xbox360Controller(1);
		
		camServer = new CowCamServer(1180);
		executor = Executors.newWorkStealingPool();
		shooterCamController = new CowCamController(0, 20, CowCamController.ImageTask.SHOOTER);

		turret = new TurretMap();
		shooter = new ShooterMap();
		//intake = new IntakeMap();
		
		driveTrain = new DriveController(new DriveMap());
		ballSystems = new OperatorController(turret,shooter,null);

		camServer.start(shooterCamController, executor);

	}
	
	public void disabledInit() {
		if(allowedToSave) {
			CowDash.save();
		}
		
		
		allowedToSave=true;
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
		// TODO: AUTO!!!
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
		driverController.prestep();
		gunnerController.prestep();
		
		ballSystems.drive(gunnerController);

		driveTrain.drive(driverController, true);
		
		driverController.endstep();
		gunnerController.endstep();
	}

	/**
	 * This function is called periodically during test mode Coders and
	 * Developers use this during their tests
	 * 
	 * But here it's for mechanical team to test their shit
	 */
	@Override
	public void testPeriodic() {
		turret.setRawPitchSpeed(gunnerController.getLY());
		turret.setRawYawSpeed(gunnerController.getRX());
		
		if(gunnerController.getButton(GamepadButton.A)) shooter.setWheelSpeed(1);
		else if(gunnerController.getButton(GamepadButton.B)) shooter.setWheelSpeed(-1);
		else shooter.setWheelSpeed(0);
		
		if(gunnerController.getButton(GamepadButton.X)) shooter.setCamSpeed(1);
		else if(gunnerController.getButton(GamepadButton.Y)) shooter.setCamSpeed(-1);
		else shooter.setCamSpeed(0);
		
		driveTrain.drive(driverController, true);
		
	}

}
