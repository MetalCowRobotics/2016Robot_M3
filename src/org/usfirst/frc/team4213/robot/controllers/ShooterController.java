package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.Xbox360Controller;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;

public class ShooterController {
	private ShooterMap shooter;
	private int desiredCamAngle;
	public ShooterState state;
	private boolean intakeReady;

	public ShooterController(ShooterMap shooter) {
		this.shooter = shooter;
		state = ShooterState.IDLE;
		intakeReady = true;
		shooter.resetEnc();
		shooter.camEncoder.setDistancePerPulse(1 / Shooter.COUNT_PER_DEG);

	}

	public enum ShooterState {
		INTAKE, IDLE, ARMING, ARMED, SHOOTING;
	}

	public void arm() {
		state = ShooterState.ARMING;
	}

	public void intake() {
		state = ShooterState.INTAKE;
	}

	public void shoot() {
		state = ShooterState.SHOOTING;
	}

	public void run(Xbox360Controller controller, int tState) {
		// TODO IMPLEMENT STATE HANDLING AND CONTROLLER INPUT
	}

	public void isolatedRun(Xbox360Controller controller) {
		// TODO IMPLEMENT STATE HANDLING AND CONTROLLER INPUT
	}

}
