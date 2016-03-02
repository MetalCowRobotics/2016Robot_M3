package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.GamepadButton;
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
	
	private enum OperatorState {
		IDLE,INTAKE_RAISED,TURRET_ENGAGED,INTAKE,EJECT;
	}
	
	private OperatorState state;
	
	public OperatorController(TurretMap turret, ShooterMap shooter, IntakeMap intake){
		this.turret = turret;
		this.shooter = shooter;
		this.intake = intake;
		state = OperatorState.IDLE;
	}
	
	
	// TODO KEEP TRACK OF THE FANCY TRANSITION STATEEESSSSSSSSSSSSSSSS
	public void drive(CowGamepad controller){
		
		// INTAKE
		if(controller.getButtonTripped(GamepadButton.B) && isAllIdle() && state == OperatorState.IDLE){
			//DriverStation.reportError("\n INTAKING", false);
			shooter.intake();
			//intake.intake();
			turret.idle();
			state = OperatorState.INTAKE;
		}else if((controller.getButtonReleased(GamepadButton.B) && state == OperatorState.INTAKE)){
			//DriverStation.reportError("\n STOP INTAKING", false);
			idleAll();
			state = OperatorState.IDLE;
		}
		
		if(shooter.getSwitchHit() && state==OperatorState.INTAKE){
			controller.rumbleLeft((float) 0.3);
		}
		
		// EJECT
		if(controller.getButtonTripped(GamepadButton.Y) && isAllIdle() && state == OperatorState.IDLE){
			//DriverStation.reportError("\n EJECTING", false);
			shooter.eject();
			//intake.eject();
			turret.idle();
			state = OperatorState.EJECT;
		}else if((controller.getButtonReleased(GamepadButton.Y) && state == OperatorState.EJECT)){
			DriverStation.reportError("\n STOP EJECTING", false);
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
//		
//		// Engage Turret
//		if(controller.getLT() > 0 && state == OperatorState.IDLE){
//			turret.engage();
//			state = OperatorState.TURRET_ENGAGED;
//		}else if(controller.getLT() == 0 && state == OperatorState.TURRET_ENGAGED){
//			idleAll();
//			state = OperatorState.IDLE;
//		}
		
		// Arm Shooter
		if(controller.getButtonTripped(GamepadButton.RT) && state == OperatorState.TURRET_ENGAGED){
			shooter.arm();
			//DriverStation.reportError("\n ARMING", false);
		}else if(controller.getButtonReleased(GamepadButton.RT) && state == OperatorState.TURRET_ENGAGED){
			//DriverStation.reportError("\n DISARMING", false);
			shooter.idle();
		}
		
		
		
		// Turret Motion
		if(state == OperatorState.TURRET_ENGAGED){
			// UP / DOWN
			if(controller.getLY() < -0.2){
				//DriverStation.reportError("\n BUMPING UP", false);
				turret.bumpTurretUp();
			}else if(controller.getLY() > 0.2){
				//DriverStation.reportError("\n BUMPING DOWN", false);
				turret.bumpTurretDown();
			}
				
			// RIGHT / LEFT
			if(controller.getRX() > 0.2){
				//DriverStation.reportError("\n BUMPING RIGHT", false);
				turret.bumpTurretRight();
			}else if(controller.getRX() < -0.2){
				//DriverStation.reportError("\n BUMPING LEFT", false);
				turret.bumpTurretLeft();
			}
		}
		
		turret.step();
		shooter.step();
		//intake.step();
		
		// TODO VISION
		
		
		
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
	
	// Checks if Raising is Ready or Is Already Raising
	
	
	
}
