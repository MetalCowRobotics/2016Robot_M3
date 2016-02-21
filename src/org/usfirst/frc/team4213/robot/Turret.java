package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.command.PIDSubsystem;

public class Turret extends PIDSubsystem{

	public Turret(double p, double i, double d) {
		super(p, i, d);
		// TODO Auto-generated constructor stub
	}
	
	public void setYaw(int yaw){
		
		int maxYaw = 600;
		int minYaw = 400;
		
		
		
	}

}
