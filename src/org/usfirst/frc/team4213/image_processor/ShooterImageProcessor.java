package org.usfirst.frc.team4213.image_processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.ImageProcessingTask;
import org.team4213.lib14.Target;

import edu.wpi.first.wpilibj.DriverStation;

public class ShooterImageProcessor implements ImageProcessingTask{
	public final static int THRESH = 100;
	public final static int THRESH_MAX = 255;
	public final static int FRAME_WIDTH = 320;
	public final static int FRAME_HEIGHT = 240;
	public final static double CAM_FOV_DIAG = 68.5;
	public final static double DEG_PER_PX = CAM_FOV_DIAG
			/ Math.sqrt(Math.pow(FRAME_WIDTH, 2) + Math.pow(FRAME_HEIGHT, 2));
	
	private Mat latestImage;
	private Target latestTarget;
	private CowCamController camera;
	
	/**
	 * Runs the Image Processing for the Shooter Image and outputs a
	 * ShooterTarget The ShooterTarget object contains X/Y angles to move,
	 * Distance from Target, and Feasability of Shot
	 * 
	 * @return <code>Optional ShooterTarget</code> object that represents a
	 *         target of the shooter
	 * 
	 * @see Callable
	 */
	public ShooterImageProcessor(CowCamController camera){
		this.camera = camera;
	}
	
	@Override
	public void run() {
		Mat editImage = camera.getImg();
		//DriverStation.reportError("ShooterImageProcessor.run\n", false);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		// Filters for Correct Color
		filterImage(editImage);
		// Finds Contours :D
		Imgproc.findContours(editImage, contours, new Mat(), Imgproc.RETR_TREE,
				2, new Point(0, 0)); // 2 is Chain_Approx_Simple
		
		// Lots of Arrays to Hold Various Things
		MatOfPoint2f[] contours_poly = new MatOfPoint2f[contours.size()];
		Rect[] boundingRects = new Rect[contours.size()];
		Point[] center = new Point[contours.size()];

		int biggestRectIndex = -1;
		double biggestRectArea = 0;

		// A Temporary MatOfPoint(s) to use to convert between types in Loop
		MatOfPoint contour_point = new MatOfPoint();
		MatOfPoint2f contour = new MatOfPoint2f();

		// Loops through Contours in the List
		for (int i = 0; i < contours.size(); i++) {
			// Initializes a New MatOfPoint2f
			contours_poly[i] = new MatOfPoint2f();
			// Converts to MatOfPoint2f
			contour.fromList(contours.get(i).toList());
			// Approximate a Polygon Curve
			Imgproc.approxPolyDP(contour, contours_poly[i], 3, true);
			// Converts to MatOfPoint
			contour_point.fromList(contours_poly[i].toList());
			// Finds Rectangles that Surround Contours
			boundingRects[i] = Imgproc.boundingRect(contour_point);
			// Gets the Centers of the Rectangles
			center[i] = new Point(boundingRects[i].x + boundingRects[i].width
					/ 2, boundingRects[i].y + boundingRects[i].height / 2);
			// Finds the Biggest Rectangle
			if (boundingRects[i].area() > biggestRectArea) {
				biggestRectArea = boundingRects[i].area();
				biggestRectIndex = i;
			}
		}

		if (/* threshold Conditions go here ... need to test */boundingRects.length > 0) {
			final double angleX = DEG_PER_PX
					* (center[biggestRectIndex].x - FRAME_WIDTH / 2);
			final double angleY = DEG_PER_PX
					* (center[biggestRectIndex].y - FRAME_HEIGHT / 2);
			final double distance = 0; // x = angle of Shooter (FROM ENCODER);
										// (77.5/12)/Math.atan(x+angleY);
			latestTarget = new Target(angleX, angleY, distance, boundingRects[biggestRectIndex], editImage);
		} else {
			latestTarget = null;
		}
		
		latestImage = editImage;

	}

	/**
	 * Blurs the Image a bit, Converts it to HSV, Applies an RGB Filter, and
	 * Then a Threshold
	 */
	private void filterImage(Mat image) {
		Imgproc.medianBlur(image, image, 3);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
		Core.inRange(image, new Scalar(0, 0, 0), new Scalar(100, 100, 100),
				image);
		Imgproc.threshold(image, image, THRESH, THRESH_MAX,
				Imgproc.THRESH_BINARY);
	}

	@Override
	public Mat getImage() {
		return latestImage;
	}

	@Override
	public Target getTarget() {
		return latestTarget;
	}
	
	// TO SAVE IMAGE CODE
	/*
	 * Scalar color = new Scalar(Math.random() * 255, Math.random() * 255,
	 * Math.random() * 255); Mat drawing = new Mat(camImage.size(),
	 * CvType.CV_8UC3); Imgproc.drawContours(drawing, contours,
	 * biggestRectIndex, color); Imgproc.rectangle(drawing, new
	 * Point(boundingRects[biggestRectIndex].x,
	 * boundingRects[biggestRectIndex].y), new Point(
	 * boundingRects[biggestRectIndex].x +
	 * boundingRects[biggestRectIndex].width,
	 * boundingRects[biggestRectIndex].y +
	 * boundingRects[biggestRectIndex].height), color);
	 * 
	 * Imgcodecs.imwrite("C:\\Users\\MetalCow\\Downloads\\testimgout2.jpg",
	 * drawing);
	 */
}
