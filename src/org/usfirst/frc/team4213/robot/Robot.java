package org.usfirst.frc.team4213.robot;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import org.usfirst.frc.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

//	TurretMap turret;
//	IntakeMap intake; // TODO: Stitch in and test intake
//	ShooterMap shooter;

//	AIRFLOController driverController;
	Xbox360Controller controller;
        
//	DriveController driveTrain;
//	OperatorController ballSystems;
	DriveMap drivemap;
//	Timer timer;

	// Camera Controller
//	public static CowCamServer camServer;
	
	// The Thread Pool / Executor of Tasks to Use
//	public ScheduledExecutorService executor;
	// A new Camera Controller for the Shooter
//	boolean allowedToSave = false;
//	int autonState = 0;
//	double autonShotTime = 0;
//	boolean straightAuton = false;
//	int angleX = 90;
//	int angleY = 39;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
//	@Override
	public void robotInit() {
                System.out.println("\n\nStarting Robot Initilization...");

//		CowDash.load();

                System.out.print("\tCreate controller: ");
		controller = new Xbox360Controller(1);
                System.out.println("SUCCESS!");
                
//		executor = Executors.newScheduledThreadPool(1);

//		timer = new Timer();

//		try {
//			camServer = new CowCamServer();
//			executor.scheduleWithFixedDelay(camServer, 0, 60, TimeUnit.MILLISECONDS);

//		} catch (Exception e) {
//			DriverStation.reportError("Failed vision start", true);
//		}

		// Systems
//		turret = new TurretMap();
//		shooter = new ShooterMap();
//		intake = new IntakeMap();

                System.out.print("\tPopulate drive map: ");
		drivemap = new DriveMap();
		System.out.println("SUCCESS!");
                
		//Controllers
//		driveTrain = new DriveController(drivemap);
//		ballSystems = new OperatorController(turret, shooter, intake);
//		CowDash.getNum("AUTONOMOUS_MODE", 0);

                System.out.println("... initialization complete!\n");

	}

//	@Override
//	public void disabledInit() {
//		if (allowedToSave) {
//			CowDash.save();
//		}
//
//		allowedToSave = true;
//	}

//	@Override
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
//	@Override
	public void autonomousInit() {
		// TODO: AUTO!!!
//		timer.reset();
//		timer.start();
//		switch ( (int) CowDash.getNum("AUTONOMOUS_MODE", 0)){
//		case 1:
//			angleX = 94;
//			angleY = 36;
//			break;
//		case 5:
//			angleX = 270;
//			angleY = 49;
//			break;
//		case 2:
//			angleX = 90;
//			angleY = 49;
//			break;
//		case 34:
//			straightAuton = true;
//			break;
//		default:
//			angleX = 92;
//			angleY = 38;
//			break;
//		
//		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
//	@Override
	public void autonomousPeriodic() {

        }

	/**
	 * This function is called periodically during operator control Working
	 * Calls for Drivers to do their stuff
	 */
//	@Override
	public void teleopPeriodic() {
//		ballSystems.drive(gunnerController);
//		driveTrain.drive(driverController, true);
	}

	/**
	 * This function is called periodically during test mode Coders and
	 * Developers use this during their tests
	 * 
	 * But here it's for mechanical team to test their shit
	 */
//	@Override
	public void testPeriodic() {
//		turret.setRawPitchSpeed(gunnerController.getLY());
//		turret.setRawYawSpeed(gunnerController.getRX());

//		if (gunnerController.getButton(GamepadButton.A))
//			shooter.setCurrentWheelSpeed(1);
//		else if (gunnerController.getButton(GamepadButton.B))
//			shooter.setCurrentWheelSpeed(-1);
//		else
//			shooter.setCurrentWheelSpeed(0);

//		if (gunnerController.getButton(GamepadButton.X))
//			shooter.setCamSpeed(1);
//		else if (gunnerController.getButton(GamepadButton.Y))
//			shooter.setCamSpeed(-1);
//		else
//			shooter.setCamSpeed(0);

                
//                if(gunnerController.getButton(GamepadButton.A) ){//&& Intake.LIMIT_SWITCH_DWN==0){
//                    System.out.println("I am moving down!");
//                }
//                if(gunnerController.getButton(GamepadButton.A) ){//&& Intake.LIMIT_SWITCH_DWN==1){
//                    System.out.println("I am down!");
//                }
//                if(gunnerController.getButton(GamepadButton.Y) ){//&& Intake.LIMIT_SWITCH_UP==0){
//                    System.out.println("I am moving up!");
//                }
//                if(gunnerController.getButton(GamepadButton.Y) ){//&& Intake.LIMIT_SWITCH_UP==1){
//                    System.out.println("I am up!");
//                }
//System.out.println("Down Switch: "+intake.LIMIT_SWITCH_DOWN);
//System.out.println("  Up Switch: "+intake.LIMIT_SWITCH_UP);


                
                
		//System.out.println("\n Enc 1 Position:" + shooter.getFlyEncDist());
		//System.out.println("\n Enc 2 Revolutions:" + turret.getYawEncPosition() / 1024);
		
//                driveTrain.drive(driverController, true);
//		intake.setPitchSpeed(gunnerController.getLY());
		//DriverStation.reportError("\n" + CowDash.getNum("AUTONOMOUS_MODE",0), false);
                
                /*********
                 * Robot has two modes drive/shoot
                 *     Drive = allows intake operation and movement
                 *     Shoot = engages turret, intake is down, and cannot move around
                 * Toggle between these two modes is determined by if the Right-trigger
                 * is pressed all the way down.
                 */
                if(controller.getRT()==1){
                    //in shooter mode
                    System.out.println("Shooter Mode!");
                    drivemap.setLeftMotorSpeed(0); //needs to be inverted, unless motor is inverted
                    drivemap.setRightMotorSpeed(0);
                    
                    
                    
                    
                    
                }else{
                    //Engages Drive Mode
                    drivemap.setLeftMotorSpeed(-1*controller.getLY()); //needs to be inverted, unless motor is inverted
                    drivemap.setRightMotorSpeed(controller.getRY());
                    
                    
                    
                    
                }
                

		
	}

}