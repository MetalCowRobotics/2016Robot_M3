
package org.usfirst.frc.team4213.robot;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.team4213.lib14.AIRFLOController;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowCamServer;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.GamepadButton;
import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.image_processor.ShooterImageProcessor;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.controllers.OperatorController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
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

	TurretMap turret;
	IntakeMap intake; // TODO: Stitch in and test intake
	ShooterMap shooter;

	AIRFLOController driverController;
	CowGamepad gunnerController;

	DriveController driveTrain;
	OperatorController ballSystems;

	Timer timer;

	// Camera Controller
	public static CowCamServer camServer;
	// The Camera Server
	public CowCamController shooterCameraController;
	// public CowCamController frontCameraController;

	// The Camera Image Processor
	public ShooterImageProcessor shooterProcessingTask;
	// The Thread Pool / Executor of Tasks to Use
	public ScheduledExecutorService executor;
	// A new Camera Controller for the Shooter
	boolean allowedToSave = false;

	static {
		DriverStation.reportError("\n Loading OpenCV...", false);
		// Loads the OpenCV Library from The RoboRIO's Local Lib Directory
		System.load("/usr/local/lib/lib_OpenCV/java/libopencv_java2410.so");
		DriverStation.reportError("\n Loaded OpenCV.", false);
	}

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

			shooterCameraController = new CowCamController(0, 25);
			// frontCameraController = new CowCamController(1, 25);

			shooterProcessingTask = new ShooterImageProcessor(shooterCameraController);
			camServer = new CowCamServer(1180, shooterCameraController, shooterProcessingTask);

			executor.scheduleWithFixedDelay(shooterCameraController, 0, 15, TimeUnit.MILLISECONDS);
			// executor.scheduleWithFixedDelay(frontCameraController, 0,
			// 35,TimeUnit.MILLISECONDS);

			executor.scheduleWithFixedDelay(shooterProcessingTask, 0, 10, TimeUnit.MILLISECONDS);
			executor.scheduleWithFixedDelay(camServer, 0, 35, TimeUnit.MILLISECONDS);
			executor.scheduleAtFixedRate(() -> {
				System.gc();
			} , 45, 45, TimeUnit.SECONDS);
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
		driveTrain = new DriveController(new DriveMap());
		ballSystems = new OperatorController(turret, shooter, intake, shooterProcessingTask, shooterCameraController);

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
		turret.setRawPitchSpeed(gunnerController.getLY());
		turret.setRawYawSpeed(gunnerController.getRX());

		if (gunnerController.getButton(GamepadButton.A))
			shooter.setCurrentWheelSpeed(1);
		else if (gunnerController.getButton(GamepadButton.B))
			shooter.setCurrentWheelSpeed(-1);
		else
			shooter.setCurrentWheelSpeed(0);

		if (gunnerController.getButton(GamepadButton.X))
			shooter.setCamSpeed(1);
		else if (gunnerController.getButton(GamepadButton.Y))
			shooter.setCamSpeed(-1);
		else
			shooter.setCamSpeed(0);

		DriverStation.reportError("\n Enc 1 Position:" + shooter.getFlyEncDist(), false);
		DriverStation.reportError("\n Enc 2 Revolutions:" + turret.getYawEncPosition() / 1024, false);
		driveTrain.drive(driverController, true);
		intake.setPitchSpeed(driverController.getLY());
	}

}
