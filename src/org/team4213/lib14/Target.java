package org.team4213.lib14;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;

public class Target {
	public double angleX;
	public double angleY;
	public double distance;
	public Rect goal;
	public Mat img;
	public Point center;

	public Target(double angleX, double angleY, double distance, Rect goal, Mat img ) {
		this.angleX = angleX;
		this.angleY = angleY;
		this.distance = distance;
		this.goal = goal;
		this.img = img;
		
		center = new Point((goal.x+goal.width/2.0)-img.width()/2.0, (goal.y+goal.height/2.0)-img.height()/2.0);
	}
}
