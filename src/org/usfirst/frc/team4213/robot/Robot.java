package org.usfirst.frc.team4213.robot;

//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;

//import org.usfirst.frc.team4213.lib14.CowCamServer;
import edu.wpi.first.wpilibj.DriverStation;
import org.usfirst.frc.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.controllers.OperatorController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap;
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

	Xbox360Controller driverController;
	Xbox360Controller gunnerController;

	DriveController driveTrain;
	OperatorController ballSystems;
	DriveMap drivemap;
	Timer timer;

        DriverStation driverStation;
        
        
        
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

                //Setup Controllers
		driverController = new Xbox360Controller(1); //CRio port 1, Dash port 0, because cRio is 1 index
		gunnerController = new Xbox360Controller(2); //Crio port 2, dash port 1, because cRio is 2 index
                

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

                //if the Turret is IDLE you can drive around.
                System.out.println("LT Val: "+driverController.getLT());
                if(driverController.getLT()==0){  //if it is zero let them drive
                    driveTrain.drive(driverController, true); 
                }else{ //if not zero turret will be up and they should not drive.
                    drivemap.setLeftMotorSpeed(0);  //Stop left wheels
                    drivemap.setRightMotorSpeed(0);  //Stop right wheels
                }
                ballSystems.drive(driverController);  //this always needs to run, regardless of driving
                
                
                
                //this is all the other code
                
                
                
//                if(gunnerController.getButton(GamepadButton.B)){
//                    //System.out.println("Roller in!");
//                    intake.setRollerSpeed(.5);
//                }else if(gunnerController.getButton(GamepadButton.X)){
//                    //System.out.println("Roller eject!");
//                    intake.setRollerSpeed(-0.5);
//                }else{
//                    //System.out.println("Roller Stopped!");
//                    intake.setRollerSpeed(0);
//                }
//                
//                if(gunnerController.getButton(GamepadButton.Y)){
//                    //System.out.println("Intake Up!");
//                    intake.setPitchSpeed(1);
//                }else if(gunnerController.getButton(GamepadButton.A)){
//                    //System.out.println("Intake Down!");
//                    intake.setPitchSpeed(-1);
//                }else{
//                    //System.out.println("Intake Stopped!");
//                    intake.setPitchSpeed(0);
//                }
//
//         if(gunnerController.getButton(GamepadButton.RT)){
//                    //System.out.println("Kicker forward!");
//                    shooter.setCamSpeed(1);
//                }else if(gunnerController.getButton(GamepadButton.LT)){
//                    //System.out.println("Kicker backward!");
//                    shooter.setCamSpeed(-1);
//                }else{
//                    //System.out.println("Kicker Stopped!");
//                    shooter.setCamSpeed(0);
//                }
//         
//          if(gunnerController.getButton(GamepadButton.LB)){
//                    //System.out.println("Shooter In!");
//                    shooter.setCurrentWheelSpeed(1);
//                }else if(gunnerController.getButton(GamepadButton.RB)){
//                    //System.out.println("Shooter Out!");
//                    shooter.setCurrentWheelSpeed(-1);
//                }else{
//                    //System.out.println("Shooter Stopped!");
//                    shooter.setCurrentWheelSpeed(0);
//                }
//          
//          
//          //double gunnerControllerLYvalue = gunnerController.getLY();
//          //if(gunnerControllerLYvalue==1){ //do this
//          
//          
//          if(gunnerController.getLY()==1){
//                    //System.out.println("Pitch Turret Down!");
//                    turret.setRawPitchSpeed(.5);
//         }else if(gunnerController.getLY()==-1){
//                    //System.out.println("Pitch Turret Up!");
//                    turret.setRawPitchSpeed(-.5);
//         }else{
//                    //System.out.println("Pitch Turret Stopped!");
//                    turret.setRawPitchSpeed(0);
//         }
//          
//         if(gunnerController.getLX()==1) {
//                //System.out.println("Turret spinning Left!");
//                turret.setRawYawSpeed(-.5);}
//             else if(gunnerController.getLX()==-1){
//                //System.out.println("Turret Spinning Right!");
//                turret.setRawYawSpeed(.5);}
//             else {
//                //System.out.println("Turret Spinning Stopped!");
//                turret.setRawYawSpeed(0);
//         }
//         if (intake.LIMIT_SWITCH_UP_IS_OPEN.get()==true)
//        
//        
//        ///encoder tests
//        //turret.getPitchEncPosition()
//        System.out.println(); //blank line
//       /* System.out.println("Shooter Pitch Encoder: "+turret.getPitchEncPosition());
//        System.out.println("Shooter Pitch Angle: "+turret.getPitchEncDistance());*/
//        
//        System.out.println("Cam Encoder: "+shooter.getCamEncValue());
//        System.out.println("Cam Encoder Angle: "+shooter.getCamEncDist());
//        
//        System.out.println("Flywheel: "+shooter.getFlyEncValue());
//        System.out.println("Turret Spin Encoder: "+turret.getYawEncPosition());
//        
//        System.out.println("Limit Switch Up: "+intake.LIMIT_SWITCH_UP_IS_OPEN.get());
//        System.out.println("Limit Switch Dwn: "+intake.LIMIT_SWITCH_DOWN_IS_OPEN.get());

        }
}   