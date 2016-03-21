package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.CowGamepad;
import org.team4213.lib14.GamepadButton;
import org.usfirst.frc.team4213.robot.systems.IntakeMap;
import org.usfirst.frc.team4213.robot.systems.IntakeMap.IntakeState;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.ShooterMap.ShooterState;
import org.usfirst.frc.team4213.robot.systems.TurretMap;

public class OperatorController {
	private TurretMap turret;
	private ShooterMap shooter;
	private IntakeMap intake;
	// private PIDController visionPIDX;
	// private PIDController visionPIDY;

	private enum OperatorState {
		IDLE, INTAKE_RAISED, TURRET_ENGAGED, INTAKE, EJECT;
	}

	// private enum VisionState {
	// OFF, LONG;
	// }

	private OperatorState state;
	// private VisionState visionState;

	public OperatorController(TurretMap turret, ShooterMap shooter, IntakeMap intake) {
		this.turret = turret;
		this.shooter = shooter;
		this.intake = intake;
		state = OperatorState.INTAKE_RAISED;
		// visionState = VisionState.OFF;
		// visionPIDX = new PIDController("Vision_PID_X", 10, 0, 0, 1);
		// visionPIDY = new PIDController("Vision_PID_Y", 10, 0, 0, 1);
		// visionPIDY.setTarget(0);
		// visionPIDX.setTarget(0);
		// visionState = VisionState.OFF;

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
			state = OperatorState.IDLE;
		}

		// EJECT
		if (state == OperatorState.IDLE && controller.getButton(GamepadButton.Y)) {
			shooter.eject();
			intake.eject();
			turret.idle();
			state = OperatorState.EJECT;
		} else if (state == OperatorState.EJECT && (!controller.getButton(GamepadButton.Y))) {
			idleAll();
			state = OperatorState.IDLE;
		}

		// Shoot the Ball
		if (state == OperatorState.TURRET_ENGAGED && controller.getButton(GamepadButton.RB)) {
			shooter.shoot();
		}

		// Raise Arm
		if (state == OperatorState.IDLE && controller.getPOV() == 0) {
			// DriverStation.reportError("\n RAISING", false);
			intake.raise();
			state = OperatorState.INTAKE_RAISED;
		}

		// Lower Arm
		if (state == OperatorState.INTAKE_RAISED && controller.getPOV() == 180) {
			// DriverStation.reportError("\n LOWERING", false);
			idleAll();
			state = OperatorState.IDLE;
		}

		// ENGAGING TURRET
		if (state == OperatorState.IDLE && intake.getState() == IntakeState.DOWN
				&& controller.getButton(GamepadButton.LT)) {
			// DriverStation.reportError("\n ENGAGING", false);
			turret.engage();
			state = OperatorState.TURRET_ENGAGED;
		} else if (state == OperatorState.TURRET_ENGAGED && (!controller.getButton(GamepadButton.LT))) {
			// DriverStation.reportError("\n DISENGAGING", false);
			idleAll();
			state = OperatorState.IDLE;
		}

		// Arm Shooter
		if (state == OperatorState.TURRET_ENGAGED && shooter.getState() == ShooterState.IDLE
				&& controller.getButton(GamepadButton.RT)) {
			shooter.arm();
			// DriverStation.reportError("\n ARMING", false);
		} else if (state == OperatorState.TURRET_ENGAGED
				&& (shooter.getState() == ShooterState.ARMED || shooter.getState() == ShooterState.ARMING)
				&& (!controller.getButton(GamepadButton.RT))) {
			// DriverStation.reportError("\n DISARMING", false);
			shooter.idle();
		}

		// Cycle state
		// if (controller.getButtonTripped(GamepadButton.BACK)) {
		// if (visionState == VisionState.LONG) {
		// cameraController.setHumanFriendlySettings();
		// visionState = VisionState.OFF;
		// } else {
		// cameraController.setTrackingSettings();
		// visionState = VisionState.LONG;
		// }
		// }

		if (state == OperatorState.TURRET_ENGAGED) {
			double speedMod = 0.8;
			manualTurretDrive(controller, speedMod);
			// switch (visionState) {
			// case OFF:
			// if (imageProcessor.getTarget() != null) {
			// speedMod = 0.5;
			// }
			// // Turret Motion by Operator Directly
			// manualTurretDrive(controller, speedMod);
			// break;
			// case LONG:
			// try {
			// Target curTarget = imageProcessor.getTarget();
			// if (curTarget != null) {
			//
			// visionDrive(curTarget);
			// } else {
			// manualTurretDrive(controller, speedMod);
			// }
			// } catch (Exception ex) {
			//
			// }
			// break;
			// }
		}

		turret.endstep();
		shooter.step();
		intake.step();

	}

	public void manualTurretDrive(CowGamepad controller, double speedMod) {
		if (Math.abs(controller.getLY()) > 0.15) {
			turret.manualPitchOverride(-controller.getLY() * speedMod);
		} else {
			turret.manualPitchOverride(0);
		}
		if (Math.abs(controller.getRX()) > 0.15) {
			turret.manualYawOverride(controller.getRX() * speedMod);
		} else {
			turret.manualYawOverride(0);
		}
	}

	// public void visionDrive(Target curTarget) {
	//
	// double yawSpeedMod = 1;
	// if (shooter.getState() == ShooterState.ARMING || shooter.getState() ==
	// ShooterState.ARMED) {
	// yawSpeedMod = CowDash.getNum("Vision_Yaw_Slowdown", 0.75);
	// }
	// String visionYawState;
	// // Turret Yaw Movement
	// if (Math.abs(curTarget.angleX) > CowDash.getNum("Vision_X_Hi_Limit", 15))
	// {
	// visionYawState = "HIGH";
	// turret.manualYawBumpOverride(
	// yawSpeedMod
	// * CowMath.copySign(curTarget.angleX,
	// Math.sqrt(Math.abs(Math
	// .atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Hi", 1))))
	// * CowDash.getNum("Vision_Tracking_X_Kp1_Hi", 1.5)));
	// } else if (Math.abs(curTarget.angleX) >
	// CowDash.getNum("Vision_X_Med_Limit", 10)) {
	// visionYawState = "MED";
	// turret.manualYawBumpOverride(
	// yawSpeedMod
	// * CowMath.copySign(curTarget.angleX,
	// Math.sqrt(Math.abs(Math
	// .atan(curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2_Med",
	// 1))))
	// * CowDash.getNum("Vision_Tracking_X_Kp1_Med", 1.5)));
	// } else {
	// visionYawState = "LOW";
	// turret.manualYawBumpOverride(
	// yawSpeedMod
	// * CowMath
	// .copySign(curTarget.angleX,
	// Math.sqrt(Math.abs(Math.atan(
	// curTarget.angleX * CowDash.getNum("Vision_Tracking_X_Kp2", .6))))
	// * CowDash.getNum("Vision_Tracking_X_Kp1", .6)));
	// }
	// CowDash.setString("Vision_Yaw_Setting", visionYawState);
	//
	// // double deg_pp = CowDash.getNum("Deg_Per_PX", 0.14);
	// // double dist = 64/ Math.tan(turret.getPitchEncDistance() +
	// // curTarget.angleY - 39);
	// // CowDash.setNum("Distance_From_Target", dist);
	// // double yOffset = (5*Math.pow(10,-10)*Math.pow(dist,5) -
	// // 4*Math.pow(10,-7)*Math.pow(dist,4)
	// // - (0.0001)*Math.pow(dist,3) - (0.0163)*Math.pow(dist,2) -
	// // (1.1415)*dist + 51.707);
	// // CowDash.setNum("Desired_angle", yOffset);
	// // yOffset -= turret.getPitchEncDistance() - curTarget.angleY ;
	// // double targetPitch = yOffset ;
	// double yOffset = 0;
	// double pitchSpeedMod = 1;
	// int bracket = 1;
	//
	// if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_1", 90)) {
	// yOffset = CowDash.getNum("Vision_Offset_1", -10);
	// bracket = 1;
	// } else if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_2", 85)) {
	// yOffset = CowDash.getNum("Vision_Offset_2", -10);
	// bracket = 2;
	// } else if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_3", 80)) {
	// yOffset = CowDash.getNum("Vision_Offset_3", -10);
	// bracket = 3;
	// } else if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_4", 76)) {
	// yOffset = CowDash.getNum("Vision_Offset_4", -10);
	// bracket = 4;
	// } else if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_5", 73)) {
	// yOffset = CowDash.getNum("Vision_Offset_5", -10);
	// bracket = 5;
	// } else if (turret.getPitchEncDistance() + curTarget.angleY >
	// CowDash.getNum("Vision_Offset_Ang_6", 70)) {
	// yOffset = CowDash.getNum("Vision_Offset_6", -10);
	// bracket = 6;
	// } else {
	// yOffset = 0;
	// bracket = -1;
	// }
	// CowDash.setString("Current_Shooting_Bracket", "Bracket " + bracket);
	//
	// yOffset *= Camera.DEG_PER_PX;
	// double targetPitch = yOffset + curTarget.angleY;
	// // Turret Pitch Movement
	// if (targetPitch < 0) {
	// pitchSpeedMod = CowDash.getNum("Vision_Pitch_Slowdown", 0.5);
	// }
	// String visionPitchState;
	// if (Math.abs(targetPitch) > CowDash.getNum("Vision_Y_Hi_Limit", 15)) {
	// visionPitchState = "HIGH";
	// turret.manualPitchBumpOverride(
	// pitchSpeedMod
	// * CowMath
	// .copySign(targetPitch,
	// Math.sqrt(Math.abs(Math.atan(
	// targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2_Hi", .6))))
	// * CowDash.getNum("Vision_Tracking_Y_Kp1_Hi", .6)));
	// } else if (Math.abs(targetPitch) > CowDash.getNum("Vision_Y_Med_Limit",
	// 7)) {
	// visionPitchState = "MED";
	// turret.manualPitchBumpOverride(
	// pitchSpeedMod
	// * CowMath
	// .copySign(targetPitch,
	// Math.sqrt(Math.abs(Math.atan(
	// targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2_Med", .6))))
	// * CowDash.getNum("Vision_Tracking_Y_Kp1_Med", .6)));
	// } else {
	// visionPitchState = "LOW";
	// turret.manualPitchBumpOverride(
	// pitchSpeedMod
	// * CowMath
	// .copySign(targetPitch,
	// Math.sqrt(Math.abs(Math
	// .atan(targetPitch * CowDash.getNum("Vision_Tracking_Y_Kp2", .6))))
	// * CowDash.getNum("Vision_Tracking_Y_Kp1", .6)));
	// }
	// CowDash.setString("Vision_Pitch_Setting", visionPitchState);
	// //
	// turret.manualPitchOverride(-visionPIDY.feedAndGetValue(curTarget.center.y));
	// //
	// turret.manualYawOverride(-visionPIDX.feedAndGetValue(curTarget.center.x));
	// if (turret.getPitchEncDistance() > CowDash.getNum("Vision_Shoot_BR_1",
	// 100)) {
	// shooter.setShootWheelSpeed(CowDash.getNum("Vision_Shoot_Speed_BR_1",
	// 74));
	// } else if (turret.getPitchEncDistance() >
	// CowDash.getNum("Vision_Shoot_BR_2", 85)) {
	// shooter.setShootWheelSpeed(CowDash.getNum("Vision_Shoot_Speed_BR_2",
	// 90));
	// } else {
	// shooter.setShootWheelSpeed(100);
	// }
	// }

	public void idleAll() {
		shooter.idle();
		intake.idle();
		turret.idle();
	}

}
