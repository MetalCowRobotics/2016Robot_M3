package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.CowDash;
import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.CowMath;
import org.team4213.lib14.GamepadButton;
import org.team4213.lib14.PIDController;
import org.team4213.lib14.Target;
import org.usfirst.frc.team4213.robot.raw_systems.RawTurret;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap.IntakeRaiseState;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Camera;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;

public class OperatorController {

	private static PIDController visionPIDX;
	private static PIDController visionPIDY;

	private static enum OperatorState {
		IDLE, IntakeMap_RAISED, TurretMap_ENGAGED, IntakeMap, EJECT;
	}

	private static enum VisionState {
		OFF, LONG;
	}

	private static OperatorState state;
	private static VisionState visionState;

	private OperatorController() {}
	static {
		
		state = OperatorState.IntakeMap_RAISED;
		visionState = VisionState.OFF;
		visionPIDX = new PIDController("Vision_PID_X", 10, 0, 0, 1);
		visionPIDY = new PIDController("Vision_PID_Y", 10, 0, 0, 1);
		visionPIDY.setTarget(0);
		visionPIDX.setTarget(0);
		visionState = VisionState.OFF;

	}

	public static void drive(CowGamepad controller) {
		TurretMap.prestep();

		// IntakeMap
		if (state == OperatorState.IDLE && controller.getButton(GamepadButton.B)) {
			ShooterMap.intake();
			IntakeMap.intake();
			TurretMap.idle();
			state = OperatorState.IntakeMap;
		} else if (state == OperatorState.IntakeMap && (!controller.getButton(GamepadButton.B))) {
			idleAll();
			IntakeMap.idleRoller();
			state = OperatorState.IDLE;
		}



		// EJECT
		if ((state == OperatorState.IDLE || state == OperatorState.IntakeMap_RAISED) && controller.getButton(GamepadButton.Y)) {
			ShooterMap.eject();
			TurretMap.idle();
			state = OperatorState.EJECT;
		} else if (state == OperatorState.EJECT && (!controller.getButton(GamepadButton.Y))) {
			idleAll();
			IntakeMap.idleRoller();
			if(IntakeMap.getRaiseState() == IntakeRaiseState.UP || IntakeMap.getRaiseState() == IntakeRaiseState.RAISING){
				state = OperatorState.IntakeMap_RAISED;
			}else{
				state = OperatorState.IDLE;
			}
		}

		// Shoot the Ball
		if (state == OperatorState.TurretMap_ENGAGED && controller.getButton(GamepadButton.RB)) {
			ShooterMap.shoot();
		}

		// Raise Arm
		if (state == OperatorState.IDLE && controller.getPOV() == 0) {
			// DriverStation.reportError("\n RAISING", false);
			IntakeMap.raise();
			state = OperatorState.IntakeMap_RAISED;
		}

		// Lower Arm
		if (state == OperatorState.IntakeMap_RAISED && controller.getPOV() == 180) {
			// DriverStation.reportError("\n LOWERING", false);
			idleAll();
			IntakeMap.idleRoller();
			IntakeMap.lower();
			state = OperatorState.IDLE;
		}

		// ENGAGING TurretMap
		if (state == OperatorState.IDLE && IntakeMap.getRaiseState() == IntakeRaiseState.DOWN && controller.getButton(GamepadButton.LT)) {
			// DriverStation.reportError("\n ENGAGING", false);
			TurretMap.engage();
			state = OperatorState.TurretMap_ENGAGED;
		} else if (state == OperatorState.TurretMap_ENGAGED && !controller.getButton(GamepadButton.LT)){
			// DriverStation.reportError("\n DISENGAGING", false);
			idleAll();
			state = OperatorState.IDLE;
		}

		// Arm ShooterMap
		if (state == OperatorState.TurretMap_ENGAGED && ShooterMap.getState() == ShooterState.IDLE && controller.getButton(GamepadButton.RT))  {
			ShooterMap.arm();
			// DriverStation.reportError("\n ARMING", false);
		} else if (state == OperatorState.TurretMap_ENGAGED 
				&& (ShooterMap.getState() == ShooterState.ARMING || ShooterMap.getState() == ShooterState.ARMED ) 
				&& (!controller.getButton(GamepadButton.RT))) {
			// DriverStation.reportError("\n DISARMING", false);
			ShooterMap.idle();
		}
		
		// Cycle state
//		if (controller.getButtonTripped(GamepadButton.BACK)) {
//			if (visionState == VisionState.LONG) {
//				cameraController.setHumanFriendlySettings();
//				visionState = VisionState.OFF;
//			} else {
//				cameraController.setTrackingSettings();
//				visionState = VisionState.LONG;
//			}
//		}

		if (state == OperatorState.TurretMap_ENGAGED) {
			double speedMod = 0.8;
			if(controller.getButton(GamepadButton.LB)){
				speedMod = 0.4;
			}
			manualTurretMapDrive(controller, speedMod);
//			switch (visionState) {
//			case OFF:
////				if (imageProcessor.getTarget() != null) {
////					speedMod = 0.5;
////				}	
//				// TurretMap Motion by Operator Directly
//				manualTurretMapDrive(controller, speedMod);
//				break;
//			case LONG:
//				try {
//					Target curTarget = imageProcessor.getTarget();
//					if (curTarget != null) {
//
//						visionDrive(curTarget);
//					} else {
//						manualTurretMapDrive(controller, speedMod);
//					}
//				} catch (Exception ex) {
//
//				}
//				break;
//			}
		}

		TurretMap.endstep();
		ShooterMap.step();
		IntakeMap.step();

		// TODO Stitch in vision

	}

	public static void manualTurretMapDrive(CowGamepad controller, double speedMod) {
		if (Math.abs(controller.getLY()) > 0.15) {
			TurretMap.manualPitchOverride(-controller.getLY() * speedMod);
		}
		if (Math.abs(controller.getRX()) > 0.15) {
			TurretMap.manualYawOverride(controller.getRX() * speedMod);
		}
	}

	public void visionDrive(Target curTarget) {

		double yawSpeedMod = 1;
		if (ShooterMap.getState() == ShooterState.ARMING || ShooterMap.getState() == ShooterState.ARMED) {
			yawSpeedMod = CowDash.getNum("Vision_Yaw_Slowdown", 0.75);
		}
		String visionYawState;
		// TurretMap Yaw Movement
		if (Math.abs(curTarget.angleX) > CowDash.getNum("Vision_X_Hi_Limit", 15)) {
			visionYawState = "HIGH";
			TurretMap.manualYawBumpOverride(
					yawSpeedMod
							* CowMath.copySign(curTarget.angleX,
									Math.sqrt(Math.abs(Math
											.atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Hi", 1))))
							* CowDash.getNum("Vision_Tracking_X_Kp1_Hi", 1.5)));
		} else if (Math.abs(curTarget.angleX) > CowDash.getNum("Vision_X_Med_Limit", 10)) {
			visionYawState = "MED";
			TurretMap.manualYawBumpOverride(
					yawSpeedMod
							* CowMath.copySign(curTarget.angleX,
									Math.sqrt(Math.abs(Math
											.atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Med", 1))))
							* CowDash.getNum("Vision_Tracking_X_Kp1_Med", 1.5)));
		} else {
			visionYawState = "LOW";
			TurretMap.manualYawBumpOverride(
					yawSpeedMod
							* CowMath
									.copySign(curTarget.angleX,
											Math.sqrt(Math.abs(Math.atan(
													curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2", .6))))
									* CowDash.getNum("Vision_Tracking_X_Kp1", .6)));
		}
		CowDash.setString("Vision_Yaw_Setting", visionYawState);

		// double deg_pp = CowDash.getNum("Deg_Per_PX", 0.14);
		// double dist = 64/ Math.tan(TurretMap.getPitchEncDistance() +
		// curTarget.angleY - 39);
		// CowDash.setNum("Distance_From_Target", dist);
		// double yOffset = (5*Math.pow(10,-10)*Math.pow(dist,5) -
		// 4*Math.pow(10,-7)*Math.pow(dist,4)
		// - (0.0001)*Math.pow(dist,3) - (0.0163)*Math.pow(dist,2) -
		// (1.1415)*dist + 51.707);
		// CowDash.setNum("Desired_angle", yOffset);
		// yOffset -= TurretMap.getPitchEncDistance() - curTarget.angleY ;
		// double targetPitch = yOffset ;
		double yOffset = 0;
		double pitchSpeedMod = 1;
		int bracket = 1;

		if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_1", 90)) {
			yOffset = CowDash.getNum("Vision_Offset_1", -10);
			bracket = 1;
		} else if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_2", 85)) {
			yOffset = CowDash.getNum("Vision_Offset_2", -10);
			bracket = 2;
		} else if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_3", 80)) {
			yOffset = CowDash.getNum("Vision_Offset_3", -10);
			bracket = 3;
		} else if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_4", 76)) {
			yOffset = CowDash.getNum("Vision_Offset_4", -10);
			bracket = 4;
		} else if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_5", 73)) {
			yOffset = CowDash.getNum("Vision_Offset_5", -10);
			bracket = 5;
		} else if (RawTurret.getPitchEncDistance() + curTarget.angleY > CowDash.getNum("Vision_Offset_Ang_6", 70)) {
			yOffset = CowDash.getNum("Vision_Offset_6", -10);
			bracket = 6;
		} else {
			yOffset = 0;
			bracket = -1;
		}
		CowDash.setString("Current_Shooting_Bracket", "Bracket " + bracket);

		yOffset *= Camera.DEG_PER_PX;
		double targetPitch = yOffset + curTarget.angleY;
		// TurretMap Pitch Movement
		if (targetPitch < 0) {
			pitchSpeedMod = CowDash.getNum("Vision_Pitch_Slowdown", 0.5);
		}
		String visionPitchState;
		if (Math.abs(targetPitch) > CowDash.getNum("Vision_Y_Hi_Limit", 15)) {
			visionPitchState = "HIGH";
			TurretMap.manualPitchBumpOverride(
					pitchSpeedMod
							* CowMath
									.copySign(targetPitch,
											Math.sqrt(Math.abs(Math.atan(
													targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2_Hi", .6))))
									* CowDash.getNum("Vision_Tracking_Y_Kp1_Hi", .6)));
		} else if (Math.abs(targetPitch) > CowDash.getNum("Vision_Y_Med_Limit", 7)) {
			visionPitchState = "MED";
			TurretMap.manualPitchBumpOverride(
					pitchSpeedMod
							* CowMath
									.copySign(targetPitch,
											Math.sqrt(Math.abs(Math.atan(
													targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2_Med", .6))))
									* CowDash.getNum("Vision_Tracking_Y_Kp1_Med", .6)));
		} else {
			visionPitchState = "LOW";
			TurretMap.manualPitchBumpOverride(
					pitchSpeedMod
							* CowMath
									.copySign(targetPitch,
											Math.sqrt(Math.abs(Math
													.atan(targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2", .6))))
									* CowDash.getNum("Vision_Tracking_Y_Kp1", .6)));
		}
		CowDash.setString("Vision_Pitch_Setting", visionPitchState);
		// TurretMap.manualPitchOverride(-visionPIDY.feedAndGetValue(curTarget.center.y));
		// TurretMap.manualYawOverride(-visionPIDX.feedAndGetValue(curTarget.center.x));
		if (RawTurret.getPitchEncDistance() > CowDash.getNum("Vision_Shoot_BR_1", 100)) {
			ShooterMap.setShootWheelSpeed(CowDash.getNum("Vision_Shoot_Speed_BR_1", 74));
		} else if (RawTurret.getPitchEncDistance() > CowDash.getNum("Vision_Shoot_BR_2", 85)) {
			ShooterMap.setShootWheelSpeed(CowDash.getNum("Vision_Shoot_Speed_BR_2", 90));
		} else {
			ShooterMap.setShootWheelSpeed(100);
		}
	}

	public static void idleAll() {
		ShooterMap.idle();
		TurretMap.idle();
	}

}
