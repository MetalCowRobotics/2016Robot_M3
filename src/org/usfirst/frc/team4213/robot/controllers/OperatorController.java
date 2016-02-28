package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap.IntakeState;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap.TurretState;

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
	// TODO MOVE STATIC BUTTON ASSIGNMENTS TO MAP
	public void drive(Xbox360Controller controller){
		
		// INTAKE
		if(controller.getButtonTripped(2) && getIntakeReady() && state == OperatorState.IDLE){
			shooter.intake();
			intake.intake();
			turret.idle();
			state = OperatorState.INTAKE;
		}else if(controller.getButtonReleased(2) && state == OperatorState.INTAKE){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		// EJECT
		if(controller.getButtonTripped(4) && getIntakeReady() && state == OperatorState.IDLE){
			shooter.eject();
			intake.eject();
			turret.idle();
			state = OperatorState.EJECT;
		}else if(controller.getButtonReleased(4) && state == OperatorState.EJECT){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		
		// Shoot the Ball
		if(controller.getButtonTripped(6) && state == OperatorState.TURRET_ENGAGED){
			shooter.shoot();
		}
		
		// Raise Arm
		if(controller.getPOV() == 0 && state == OperatorState.IDLE){
			intake.raise();
			state = OperatorState.INTAKE_RAISED;
		}
		
		// Lower Arm
		if(controller.getPOV() == 180 && state == OperatorState.INTAKE_RAISED){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		// Engage Turret
		if(controller.getButtonTripped(7) && state == OperatorState.IDLE){
			turret.engage();
			state = OperatorState.TURRET_ENGAGED;
		}else if(controller.getButtonReleased(7) && state == OperatorState.TURRET_ENGAGED){
			idleAll();
			state = OperatorState.IDLE;
		}
		
		// Arm Shooter
		if(controller.getButtonTripped(8) && state == OperatorState.TURRET_ENGAGED){
			shooter.arm();
		}else if(controller.getButtonReleased(8) && state == OperatorState.TURRET_ENGAGED){
			shooter.idle();
		}
		
		// Turret Motion
		if(state == OperatorState.TURRET_ENGAGED){
			// UP / DOWN
			if(controller.getLY() < 0){
				turret.bumpTurretUp();
			}else if(controller.getLY() > 0){
				turret.bumpTurretDown();
			}
				
			// RIGHT / LEFT
			if(controller.getRX() > 0){
				turret.bumpTurretRight();
			}else if(controller.getRX() < 0){
				turret.bumpTurretLeft();
			}
		}
		
		
		// TODO VISION
		
		
		
	}
	
	public void idleAll(){
		shooter.idle();
		intake.idle();
		turret.idle();
	}
	
	// Checks if Intake is Ready or is Already Intaking
	public boolean getIntakeReady(){
		return intake.getState() == IntakeState.DOWN &&
				shooter.getState() == ShooterState.IDLE &&
				turret.getState() == TurretState.IDLE;
	}
	
	// Checks if Raising is Ready or Is Already Raising
	
	
	
}
