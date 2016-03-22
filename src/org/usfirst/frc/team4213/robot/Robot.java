
package org.usfirst.frc.team4213.robot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.team4213.lib14.AIRFLOController;
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
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	TurretMap turret;
	IntakeMap intake; // TODO: Stitch in and test intake
	ShooterMap shooter;

	AIRFLOController driverController;
	CowGamepad gunnerController;

	DriveController driveTrain;
	OperatorController ballSystems;
	DriveMap drivemap;
	Timer timer;

	// Camera Controller
	public static CowCamServer camServer;
	
	// The Thread Pool / Executor of Tasks to Use
	public ScheduledExecutorService executor;
	// A new Camera Controller for the Shooter
	boolean allowedToSave = false;


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {

		CowDash.load();

		driverController = new AIRFLOController(0);
		gunnerController = new Xbox360Controller(1);

		executor = Executors.newScheduledThreadPool(1);

		timer = new Timer();

		try {
			camServer = new CowCamServer();
			executor.scheduleWithFixedDelay(camServer, 0, 60, TimeUnit.MILLISECONDS);

		} catch (Exception e) {
			DriverStation.reportError("Failed vision start", true);
		}

		// Systems
		turret = new TurretMap();
		shooter = new ShooterMap();
		try{
		intake = new IntakeMap();
		}catch(Exception ex){
			DriverStation.reportError(ex.getMessage(), false);
		}
		drivemap = new DriveMap();
		driveTrain = new DriveController(drivemap);
		ballSystems = new OperatorController(turret, shooter, intake);

	}

	@Override
	public void disabledInit() {
		if (allowedToSave) {
			CowDash.save();
		}

		allowedToSave = true;
	}

	@Override
	public void disabledPeriodic() {
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
		timer.reset();
		timer.start();
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		if(timer.get() <3){
			drivemap.setLeftMotorSpeed(-0.7);
			drivemap.setRightMotorSpeed(0.7);
		}else if(timer.get() > 3){
			drivemap.setLeftMotorSpeed(0);
			drivemap.setRightMotorSpeed(0);
		}
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

		// if(gunnerController.getButtonTripped(GamepadButton.START)){
		// DriverStation.reportError("There is an Error", false);
		// camServer.setCam(frontCameraController);
		// }else if(gunnerController.getButtonReleased(GamepadButton.START)){
		// camServer.setCam(shooterCameraController);
		// }

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
//		turret.setRawPitchSpeed(gunnerController.getLY());
//		turret.setRawYawSpeed(gunnerController.getRX());
//
//		if (gunnerController.getButton(GamepadButton.A))
//			shooter.setCurrentWheelSpeed(1);
//		else if (gunnerController.getButton(GamepadButton.B))
//			shooter.setCurrentWheelSpeed(-1);
//		else
//			shooter.setCurrentWheelSpeed(0);
//
//		if (gunnerController.getButton(GamepadButton.X))
//			shooter.setCamSpeed(1);
//		else if (gunnerController.getButton(GamepadButton.Y))
//			shooter.setCamSpeed(-1);
//		else
//			shooter.setCamSpeed(0);
//
//		DriverStation.reportError("\n Enc 1 Position:" + shooter.getFlyEncDist(), false);
//		DriverStation.reportError("\n Enc 2 Revolutions:" + turret.getYawEncPosition() / 1024, false);
//		driveTrain.drive(driverController, true);
		intake.setPitchSpeed(gunnerController.getLY());
		
	}

}
