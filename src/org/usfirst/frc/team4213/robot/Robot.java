package org.usfirst.frc.team4213.robot;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.DigitalInput;
import org.usfirst.frc.team4213.lib14.AIRFLOController;
//import org.usfirst.frc.team4213.lib14.CowCamServer;
import org.usfirst.frc.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.lib14.CowGamepad;
import org.usfirst.frc.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.controllers.OperatorController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap.TurretState;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Intake;

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

	Xbox360Controller driverController;
	Xbox360Controller gunnerController;

	DriveController driveTrain;
	OperatorController ballSystems;
	DriveMap drivemap;
	Timer timer;

        
        
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
                System.out.println("HOI IM FLANK!!!!!!!!!!!!\n");

                CowDash.load();


		driverController = new Xbox360Controller(1); //CRio port 1, Dash port 0, because cRio is 1 index
		gunnerController = new Xbox360Controller(2); //Crio port 2, dash port 1, because cRio is 2 index
                
//		executor = Executors.newScheduledThreadPool(1);

//		timer = new Timer();

//		try {
//			camServer = new CowCamServer();
//			executor.scheduleWithFixedDelay(camServer, 0, 60, TimeUnit.MILLISECONDS);

//		} catch (Exception e) {
//			DriverStation.reportError("Failed vision start", true);
//		}

		// Systems
		turret = new TurretMap();
		shooter = new ShooterMap();
		intake = new IntakeMap();
		drivemap = new DriveMap();
		
		//Controllers
		driveTrain = new DriveController(drivemap);
		ballSystems = new OperatorController(turret, shooter, intake);
		//CowDash.getNum("AUTONOMOUS_MODE", 0);

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
		ballSystems.drive(gunnerController);
		driveTrain.drive(driverController, true);
	}

        
        

        
	/**
	 * This function is called periodically during test mode Coders and
	 * Developers use this during their tests
	 * 
	 * But here it's for mechanical team to test their shit
	 */
//	@Override
	public void testPeriodic() {   //this is test mode button!!

                
//                if (driverController.getButton(GamepadButton.A)) {
//                    System.out.println("Pressing A");
//                    intake.setRollerSpeed(.75);
//                } else if (driverController.getButton(GamepadButton.B)){
//                    System.out.println("Pressing B");
//                    intake.setRollerSpeed(-.75);
//                } else {
//                    System.out.println("Not pressing A or B");
//                    intake.setRollerSpeed(0);
//                }

                //This is the wheels code
                drivemap.setLeftMotorSpeed(-driverController.getLY());
                drivemap.setRightMotorSpeed(driverController.getRY());
                
                
                //this is all the other code
                
                
                
                if(gunnerController.getButton(GamepadButton.B)){
                    //System.out.println("Roller in!");
                    intake.setRollerSpeed(.5);
                }else if(gunnerController.getButton(GamepadButton.X)){
                    //System.out.println("Roller eject!");
                    intake.setRollerSpeed(-0.5);
                }else{
                    //System.out.println("Roller Stopped!");
                    intake.setRollerSpeed(0);
                }
                
                if(gunnerController.getButton(GamepadButton.Y)){
                    //System.out.println("Intake Up!");
                    intake.setPitchSpeed(1);
                }else if(gunnerController.getButton(GamepadButton.A)){
                    //System.out.println("Intake Down!");
                    intake.setPitchSpeed(-1);
                }else{
                    //System.out.println("Intake Stopped!");
                    intake.setPitchSpeed(0);
                }

         if(gunnerController.getButton(GamepadButton.RT)){
                    //System.out.println("Kicker forward!");
                    shooter.setCamSpeed(1);
                }else if(gunnerController.getButton(GamepadButton.LT)){
                    //System.out.println("Kicker backward!");
                    shooter.setCamSpeed(-1);
                }else{
                    //System.out.println("Kicker Stopped!");
                    shooter.setCamSpeed(0);
                }
         
          if(gunnerController.getButton(GamepadButton.LB)){
                    //System.out.println("Shooter In!");
                    shooter.setCurrentWheelSpeed(1);
                }else if(gunnerController.getButton(GamepadButton.RB)){
                    //System.out.println("Shooter Out!");
                    shooter.setCurrentWheelSpeed(-1);
                }else{
                    //System.out.println("Shooter Stopped!");
                    shooter.setCurrentWheelSpeed(0);
                }
          
          
          //double gunnerControllerLYvalue = gunnerController.getLY();
          //if(gunnerControllerLYvalue==1){ //do this
          
          
          if(gunnerController.getLY()==1){
                    //System.out.println("Pitch Turret Down!");
                    turret.setRawPitchSpeed(.5);
         }else if(gunnerController.getLY()==-1){
                    //System.out.println("Pitch Turret Up!");
                    turret.setRawPitchSpeed(-.5);
         }else{
                    //System.out.println("Pitch Turret Stopped!");
                    turret.setRawPitchSpeed(0);
         }
          
         if(gunnerController.getLX()==1) {
                //System.out.println("Turret spinning Left!");
                turret.setRawYawSpeed(-.5);}
             else if(gunnerController.getLX()==-1){
                //System.out.println("Turret Spinning Right!");
                turret.setRawYawSpeed(.5);}
             else {
                //System.out.println("Turret Spinning Stopped!");
                turret.setRawYawSpeed(0);
         }
        
        
        ///encoder tests
        //turret.getPitchEncPosition()
        System.out.println(); //blank line
        System.out.println("Shooter Pitch Encoder: "+turret.getPitchEncPosition());
        
        System.out.println("Cam Encoder: "+shooter.getCamEncValue());
        System.out.println("Cam Encoder Angle: "+shooter.getCamEncDist());
        
        System.out.println("Flywheel: "+shooter.getFlyEncValue());
        System.out.println("Turret Spin Encoder: "+turret.getYawEncPosition());
        
        System.out.println("Limit Switch Up: "+intake.LIMIT_SWITCH_UP.get());
        System.out.println("Limit Switch Dwn: "+intake.LIMIT_SWITCH_DOWN.get());
        
	}

}   