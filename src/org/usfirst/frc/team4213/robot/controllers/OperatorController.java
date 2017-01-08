package org.usfirst.frc.team4213.robot.controllers;

import org.usfirst.frc.team4213.lib14.CowDash;
import org.usfirst.frc.team4213.lib14.CowGamepad;
import org.usfirst.frc.team4213.lib14.CowMath;
import org.usfirst.frc.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.lib14.PIDController;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.RobotMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap.IntakeRaiseState;
import org.usfirst.frc.team4213.robot.systems.IntakeMap.IntakeRollerState;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Camera;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Turret;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap.TurretState;

import edu.wpi.first.wpilibj.DriverStation;

public class OperatorController {
	private TurretMap turret;
	private ShooterMap shooter;
	private IntakeMap intake;
	
        private class OperatorState {
            public static final int IDLE = 1;
            public static final int INTAKE_RAISED = 2;
            public static final int TURRET_ENGAGED = 3;
            public static final int INTAKE = 4;
            public static final int EJECT = 5;
        }
	/*private enum OperatorState {
		IDLE, INTAKE_RAISED, TURRET_ENGAGED, INTAKE, EJECT;
	}*/


	private int state;
	
	private double speedMod = 0.8;
	private boolean runTurretPreset = false;

	public OperatorController(TurretMap turret, ShooterMap shooter, IntakeMap intake) {
		this.turret = turret;
		this.shooter = shooter;
		this.intake = intake;
		state = OperatorState.INTAKE_RAISED;

	}

	public void drive(CowGamepad controller) {
		turret.prestep();
                
		// INTAKE
		if (state == OperatorState.IDLE && controller.getButton(GamepadButton.B)) {
			shooter.intake();
			intake.intake();
			turret.idle();
			state = OperatorState.INTAKE;
		} else if (state == OperatorState.INTAKE && (!controller.getButton(GamepadButton.B))) {
			idleAll();
			intake.idleRoller();
			state = OperatorState.IDLE;
		}



		// EJECT
		if ((state == OperatorState.IDLE || state == OperatorState.INTAKE_RAISED) && controller.getButton(GamepadButton.Y)) {
			shooter.eject();
			turret.idle();
			state = OperatorState.EJECT;
		} else if (state == OperatorState.EJECT && (!controller.getButton(GamepadButton.Y))) {
			idleAll();
			intake.idleRoller();
			if(intake.getRaiseState() == IntakeRaiseState.UP || intake.getRaiseState() == IntakeRaiseState.RAISING){
				state = OperatorState.INTAKE_RAISED;
			}else{
				state = OperatorState.IDLE;
			}
		}

		// Shoot the Ball
		if (state == OperatorState.TURRET_ENGAGED && controller.getButton(GamepadButton.RB)) {
			shooter.shoot();
		}

		// Raise Arm
		if (state == OperatorState.IDLE && controller.getButton(GamepadButton.START)) {
			// DriverStation.reportError("\n RAISING", false);
			intake.raise();
			state = OperatorState.INTAKE_RAISED;
		}

		// Lower Arm
		if (state == OperatorState.INTAKE_RAISED && controller.getButton(11)) { // Try Select Button ?
			// DriverStation.reportError("\n LOWERING", false);
			idleAll();
			intake.idleRoller();
			intake.lower();
			state = OperatorState.IDLE;
		}

		// ENGAGING TURRET
		if (state == OperatorState.IDLE && intake.getRaiseState() == IntakeRaiseState.DOWN && controller.getButton(GamepadButton.LT)) {
			// DriverStation.reportError("\n ENGAGING", false);
			turret.engage();
			state = OperatorState.TURRET_ENGAGED;
		} else if (state == OperatorState.TURRET_ENGAGED && !controller.getButton(GamepadButton.LT)){
			// DriverStation.reportError("\n DISENGAGING", false);
			idleAll();
			state = OperatorState.IDLE;
		}

		// Arm Shooter
		if (state == OperatorState.TURRET_ENGAGED && shooter.getState() == ShooterState.IDLE && controller.getButton(GamepadButton.RT))  {
			shooter.arm();
			// DriverStation.reportError("\n ARMING", false);
		} else if (state == OperatorState.TURRET_ENGAGED 
				&& (shooter.getState() == ShooterState.ARMING || shooter.getState() == ShooterState.ARMED ) 
				&& (!controller.getButton(GamepadButton.RT))) {
			// DriverStation.reportError("\n DISARMING", false);
			shooter.idle();
		}
		
		/*if(state == OperatorState.TURRET_ENGAGED && shooter.getState() == ShooterState.IDLE && controller.getPOV() == 90 && turret.getState() == TurretState.ENGAGED){
			// 90
			int angleX = 90;
			turret.setYawTarget(angleX);

			// 39
			int angleY = 39;
			turret.setPitchTarget(angleY + Turret.Pitch_Motor.MIN_ANGLE);
			runTurretPreset = true;

		}else if(state == OperatorState.TURRET_ENGAGED && shooter.getState() == ShooterState.IDLE && controller.getPOV() == 270 && turret.getState() == TurretState.ENGAGED){
			// 90
			int angleX = 270;
			turret.setYawTarget(angleX);

			// 39
			int angleY = 39;
			turret.setPitchTarget(angleY + Turret.Pitch_Motor.MIN_ANGLE);
			runTurretPreset = true;

		}else {
			runTurretPreset = false;
		}*/
                runTurretPreset=false;
		

		if (state == OperatorState.TURRET_ENGAGED && turret.getState() == TurretState.ENGAGED) {
			
			if(!runTurretPreset){
				if(controller.getButton(GamepadButton.LB)){
					speedMod = 0.4;
				}
			
				manualTurretDrive(controller, speedMod);
			}
		}

		turret.endstep();
		shooter.step();
		intake.step();

		// TODO Stitch in vision

	}

	public void manualTurretDrive(CowGamepad controller, double speedMod) {
		if (Math.abs(controller.getLY()) > 0.15) {
			turret.manualPitchOverride(-controller.getLY() * speedMod);
		}
		if (Math.abs(controller.getRX()) > 0.15) {
			turret.manualYawOverride(controller.getRX() * speedMod);
		}
	}

	public void idleAll() {
		shooter.idle();
//		intake.idle();
		turret.idle();
	}

}
