package org.usfirst.frc.team4213.robot.controllers;

import org.team4213.lib14.AIRFLOController;
import org.usfirst.frc.team4213.robot.systems.ShooterMap;
import org.usfirst.frc.team4213.robot.systems.TurretMap;

public class OperatorController {
	public TurretMap turret;
	public ShooterMap shooter;
	
	public OperatorController(TurretMap turret, ShooterMap shooter){
		this.turret = turret;
		this.shooter = shooter;
	}
	
	public void drive(AIRFLOController controller){
		
	}
}
