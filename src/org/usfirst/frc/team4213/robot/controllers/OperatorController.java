package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.GamepadButton;
import org.team4213.lib14.Target;
import org.usfirst.frc.team4213.image_processor.ShooterImageProcessor;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap.TurretState;

import edu.wpi.first.wpilibj.DriverStation;

public class OperatorController {
	private TurretMap turret;
	private ShooterMap shooter;
	private IntakeMap intake; 
	private ShooterImageProcessor imageProcessor;
	private CowCamController cameraController;
	
	private enum OperatorState {
		IDLE,INTAKE_RAISED,TURRET_ENGAGED,INTAKE,EJECT;
	}
	
	private enum VisionState {
		OFF,LONG
	}
	
	private OperatorState state;
	private VisionState visionState;
	
	public OperatorController(TurretMap turret, ShooterMap shooter, IntakeMap intake, ShooterImageProcessor processor, CowCamController cameraController){
		this.turret = turret;
		this.shooter = shooter;
		this.intake = intake;
		this.imageProcessor = processor;
		this.cameraController = cameraController;
		state = OperatorState.IDLE;
		visionState = VisionState.OFF;
	}
	
	public void drive(CowGamepad controller){
		turret.prestep();
		
		// INTAKE
		if(controller.getButtonTripped(GamepadButton.B) && isAllIdle() && state == OperatorState.IDLE){
			shooter.intake();
			//intake.intake();
			turret.idle();
			state = OperatorState.INTAKE;
		}else if((controller.getButtonReleased(GamepadButton.B) && state == OperatorState.INTAKE)){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		if(shooter.getSwitchHit() && state==OperatorState.INTAKE){
			controller.rumbleLeft((float) 1.0);
		}
		
		// EJECT
		if(controller.getButtonTripped(GamepadButton.Y) && isAllIdle() && state == OperatorState.IDLE){
			shooter.eject();
			//intake.eject();
			turret.idle();
			state = OperatorState.EJECT;
		}else if((controller.getButtonReleased(GamepadButton.Y) && state == OperatorState.EJECT)){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		
		// Shoot the Ball
		if(controller.getButton(GamepadButton.RB)){ // && state == OperatorState.TURRET_ENGAGED){
			boolean success = shooter.shoot();
		}
		
		// Raise Arm
		if(controller.getPOV() == 0 && state == OperatorState.IDLE){
			//DriverStation.reportError("\n RAISING", false);
			//intake.raise();
			state = OperatorState.INTAKE_RAISED;
		}
		
		// Lower Arm
		if(controller.getPOV() == 180 && state == OperatorState.INTAKE_RAISED){
			//DriverStation.reportError("\n LOWERING", false);
			idleAll();
			state = OperatorState.IDLE;
		}
		
		// ENGAGING TURRET
		if( controller.getButtonTripped(GamepadButton.LT) && state == OperatorState.IDLE){
			//DriverStation.reportError("\n ENGAGING", false);
			turret.engage();
			state = OperatorState.TURRET_ENGAGED;
		}else if(controller.getButtonReleased(GamepadButton.LT) && state == OperatorState.TURRET_ENGAGED){
			//DriverStation.reportError("\n DISENGAGING", false);
			idleAll();
			state = OperatorState.IDLE;
		}
		
		// Arm Shooter
		if(controller.getButtonTripped(GamepadButton.RT) && state == OperatorState.TURRET_ENGAGED){
			shooter.arm();
			//DriverStation.reportError("\n ARMING", false);
		}else if(controller.getButtonReleased(GamepadButton.RT) && state == OperatorState.TURRET_ENGAGED){
			//DriverStation.reportError("\n DISARMING", false);
			shooter.idle();
		}
		if (shooter.getState()==ShooterState.ARMED) {
			controller.rumbleRight((float) 0.5);
		}
		
		// Cycle state
		if(controller.getButtonTripped(GamepadButton.BACK)) {
			if(visionState==VisionState.LONG){
				cameraController.setHumanFriendlySettings();
				visionState=VisionState.OFF;
			} else {
				cameraController.setTrackingSettings();
				visionState=VisionState.LONG;
			}
		}
		
		if(state == OperatorState.TURRET_ENGAGED){
			switch(visionState) {
			case OFF:
				
				// Turret Motion by Operator Directly
				
					if(Math.abs(controller.getLY()) > 0.15) {
						turret.manualPitchOverride(-controller.getLY()*0.8);
					}
					if(Math.abs(controller.getRX()) > 0.15) {
						turret.manualYawOverride(controller.getRX()*0.8);
					}
				
				break;
			case LONG:
				Target curTarget = imageProcessor.getTarget();
				try{
					turret.manualPitchOverride(-curTarget.center.y*0.01);
					turret.manualYawOverride(curTarget.center.x*0.01);
				}catch(NullPointerException npe){
					
				}
				break;
			}
		}
		
		turret.endstep();
		shooter.step();
		//intake.step();
		
		// TODO Stitch in vision
		
		
		
	}
	
	public void idleAll(){
		shooter.idle();
		//intake.idle();
		turret.idle();
	}
	
	// Checks if Intake is Ready or is Already Intaking
	public boolean isAllIdle(){
		return //intake.getState() == IntakeState.DOWN &&
				shooter.getState() == ShooterState.IDLE &&
				turret.getState() == TurretState.IDLE;
	}
	
	
	
}
