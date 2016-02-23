
package org.usfirst.frc.team4213.robot;

import org.team4213.lib14.AIRFLOController;

import org.team4213.lib14.Xbox360Controller;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;

import edu.wpi.first.wpilibj.Timer; //TODO: What does this do?

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    
    
    AIRFLOController driverController;
    Xbox360Controller gunnerController;
    DriveController driveTrain;
    
    
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        
        driverController = new AIRFLOController(0);
        gunnerController = new Xbox360Controller(1);
        
        //TODO: Read-in and Populate the RobotMap from a textFile
        
        driveTrain = new DriveController(new DriveMap());

        
        
    }
    
	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString line to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the switch structure below with additional strings.
	 * If using the SendableChooser make sure to add them to the chooser code above as well.
	 */
    public void autonomousInit() {
    	autoSelected = (String) chooser.getSelected();
//		autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	switch(autoSelected) {
    	case customAuto:
        //Put custom auto code here   
            break;
    	case defaultAuto:
    	default:
    	//Put default auto code here
            break;
    	}
    }

    /**
     * This function is called periodically during operator control
     * Working Calls for Drivers to do their stuff
     */
    public void teleopPeriodic() {
    	
//    	driveTrain.drive(driverController, true);
    	
    	if(Math.abs(gunnerController.getLT())>0.5){
    		gunnerController.setRumble(RumbleType.kLeftRumble, (float) gunnerController.getLT());
    	}
    		gunnerController.setRumble(RumbleType.kLeftRumble, (float) 0.0);
        
    	
    	if(Math.abs(gunnerController.getRT())>0.5){
    		gunnerController.setRumble(RumbleType.kRightRumble, (float) gunnerController.getRT());
    	}
    		gunnerController.setRumble(RumbleType.kRightRumble, (float) 0.0);
    	
    	
    	DriverStation.reportError("RightTrigger : "+gunnerController.getRT(), false);
    	DriverStation.reportError("LeftTrigger : "+gunnerController.getLT(), false);
    	
    	
    }
    
    /**
     * This function is called periodically during test mode
     * Coders and Developers use this during their tests
     */
    public void testPeriodic() {
    	
    	//driveTrain.drive(driverController, true);
    	
    	if(Math.abs(gunnerController.getLT())>0){
    	gunnerController.setRumble(RumbleType.kLeftRumble, (float) gunnerController.getLT());
    	DriverStation.reportError("LeftTrigger : "+gunnerController.getLT(), false);
    	}
    	if(Math.abs(gunnerController.getRT())>0){
    	gunnerController.setRumble(RumbleType.kRightRumble, (float) gunnerController.getRT());
    	DriverStation.reportError("RightTrigger : "+gunnerController.getRT(), false);
    	}
    
    }
    
}
