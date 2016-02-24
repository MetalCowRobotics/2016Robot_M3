package org.team4213.lib14;

public class Target {
	public double angleX;
	public double angleY;
	public double distance;
	public boolean targetable;

	public Target(double angleX, double angleY, double distance, boolean shootable) {
		this.angleX = angleX;
		this.angleY = angleY;
		this.distance = distance;
		this.targetable = shootable;
	}
}
