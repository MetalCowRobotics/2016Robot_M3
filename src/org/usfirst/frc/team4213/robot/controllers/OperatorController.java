package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.CowMath;
import org.team4213.lib14.GamepadButton;
import org.team4213.lib14.PIDController;
import org.team4213.lib14.Target;
import org.usfirst.frc.team4213.image_processor.ShooterImageProcessor;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap;
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
	private PIDController visionPIDX;
	private PIDController visionPIDY;

	private enum OperatorState {
		IDLE,INTAKE_RAISED,TURRET_ENGAGED,INTAKE,EJECT;
	}
	
	private enum VisionState {
		OFF,LONG;
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
		visionPIDX = new PIDController("Vision_PID_X", 10, 0, 0, 1);
		visionPIDY = new PIDController("Vision_PID_Y", 10, 0, 0, 1);
		visionPIDY.setTarget(0);
		visionPIDX.setTarget(0);

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
		
		if(shooter.getSwitchHit() && state == OperatorState.INTAKE){
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
		if (shooter.readyToFire()) {
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
			double speedMod = 0.8;
			switch(visionState) {
			case OFF:
				if(imageProcessor.getTarget() != null){
					speedMod = 0.5;
				}
				// Turret Motion by Operator Directly
				manualTurretDrive(controller,speedMod);
				break;
			case LONG:
				try{
					Target curTarget = imageProcessor.getTarget();
					if(curTarget != null){
						visionDrive(curTarget);
					}else{
						manualTurretDrive(controller,speedMod);
					}
				}catch(Exception ex){
					
				}
				break;
			}
		}
		
		turret.endstep();
		shooter.step();
		//intake.step();
		
		// TODO Stitch in vision
		
		
		
	}
	
	public void manualTurretDrive(CowGamepad controller ,double speedMod){
		if(Math.abs(controller.getLY()) > 0.15) {
			turret.manualPitchOverride(-controller.getLY()*speedMod);
		}
		if(Math.abs(controller.getRX()) > 0.15) {
			turret.manualYawOverride(controller.getRX()*speedMod);
		}
	}
	
	public void visionDrive(Target curTarget){
		
		double yawSpeedMod = 1;
		if(shooter.getState() == ShooterState.ARMING || shooter.getState() == ShooterState.ARMED){
			yawSpeedMod = CowDash.getNum("Vision_Yaw_Slowdown", 0.75);
		}
		String visionYawState;
		// Turret Yaw Movement
		if(Math.abs(curTarget.angleX) > CowDash.getNum("Vision_X_Hi_Limit", 15) ){
			visionYawState="HIGH";
			turret.manualYawBumpOverride(yawSpeedMod * CowMath.copySign(curTarget.angleX, Math.sqrt(Math.abs(Math.atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Hi", 1))))*CowDash.getNum("Vision_Tracking_X_Kp1_Hi", 1.5)));
		}else if(Math.abs(curTarget.angleX) > CowDash.getNum("Vision_X_Med_Limit", 10) ){
			visionYawState="MED";
			turret.manualYawBumpOverride(yawSpeedMod * CowMath.copySign(curTarget.angleX, Math.sqrt(Math.abs(Math.atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Med", 1))))*CowDash.getNum("Vision_Tracking_X_Kp1_Med", 1.5)));
		}else{
			visionYawState="LOW";
			turret.manualYawBumpOverride(yawSpeedMod * CowMath.copySign(curTarget.angleX, Math.sqrt(Math.abs(Math.atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2", .6))))*CowDash.getNum("Vision_Tracking_X_Kp1", .6)));
		}
		CowDash.setString("Vision_Yaw_Setting", visionYawState);

		
		double pitchSpeedMod = 1;
		// Turret Pitch Movement
		if(curTarget.angleY < 0){
			pitchSpeedMod = CowDash.getNum("Vision_Pitch_Slowdown", 0.5);
		}
		String visionPitchState;
		if(Math.abs(curTarget.angleY)> CowDash.getNum("Vision_Y_Hi_Limit", 15)){
			visionPitchState="HIGH";
			turret.manualPitchBumpOverride(pitchSpeedMod*CowMath.copySign(curTarget.angleY, Math.sqrt(Math.abs(Math.atan(curTarget.angleY * CowDash.getNum("Vision_Tracking_Y_Kp2_Hi", .6))))*CowDash.getNum("Vision_Tracking_Y_Kp1_Hi", .6)));
		}else if(Math.abs(curTarget.angleY)> CowDash.getNum("Vision_Y_Med_Limit", 7)){
			visionPitchState="MED";
			turret.manualPitchBumpOverride(pitchSpeedMod*CowMath.copySign(curTarget.angleY, Math.sqrt(Math.abs(Math.atan(curTarget.angleY * CowDash.getNum("Vision_Tracking_Y_Kp2_Med", .6))))*CowDash.getNum("Vision_Tracking_Y_Kp1_Med", .6)));
		}else{
			visionPitchState="LOW";
			turret.manualPitchBumpOverride(pitchSpeedMod*CowMath.copySign(curTarget.angleY, Math.sqrt(Math.abs(Math.atan(curTarget.angleY * CowDash.getNum("Vision_Tracking_Y_Kp2", .6))))*CowDash.getNum("Vision_Tracking_Y_Kp1", .6)));
		}
		CowDash.setString("Vision_Pitch_Setting", visionPitchState);
//		turret.manualPitchOverride(-visionPIDY.feedAndGetValue(curTarget.center.y));
//		turret.manualYawOverride(-visionPIDX.feedAndGetValue(curTarget.center.x));
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
