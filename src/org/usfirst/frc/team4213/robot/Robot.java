
package org.usfirst.frc.team4213.robot;

import org.team4213.lib14.AIRFLOController;

import org.team4213.lib14.Xbox360Controller;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick.RumbleType;

import org.usfirst.frc.team4213.robot.controllers.DriveController;
import org.usfirst.frc.team4213.robot.systems.DriveMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;

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
    
    IntakeMap intake;
    AIRFLOController driverController;
    Xbox360Controller gunnerController;
    DriveController driveTrain;
    
    
    
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
        driverController = new AIRFLOController(0);
        gunnerController = new Xbox360Controller(1);
        
        //TODO: Read-in and Populate the RobotMap from a textFile
        intake = new IntakeMap();
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

    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    }

    /**
     * This function is called periodically during operator control
     * Working Calls for Drivers to do their stuff
     */
    public void teleopPeriodic() {
    	
    	driveTrain.drive(driverController, true);
    	
    }
    
    /**
     * This function is called periodically during test mode
     * Coders and Developers use this during their tests
     */
    public void testPeriodic() {
    	intake.setPitchSpeed(-gunnerController.getRY()*0.3);
    	if(gunnerController.getButton(Xbox360Controller.XBOX_BTN_A)){
    		intake.setRollerSpeed(-.8);
    	}else{
    		intake.setRollerSpeed(0);
    	}    	
    	DriverStation.reportError("\n Position = " + intake.getEncPosition(), false);
    	DriverStation.reportError("POV TEST : " + gunnerController.getPOV() , false);
    }
    
}
