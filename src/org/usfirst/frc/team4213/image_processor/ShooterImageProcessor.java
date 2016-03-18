package org.usfirst.frc.team4213.image_processor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.team4213.lib14.CowCamController;
import org.team4213.lib14.CowDash;
import org.team4213.lib14.ImageProcessingTask;
import org.team4213.lib14.Target;
import org.usfirst.frc.team4213.robot.systems.RobotMap.Camera;

import edu.wpi.first.wpilibj.DriverStation;

public class ShooterImageProcessor implements ImageProcessingTask{
	public final static int THRESH = 100;
	public final static int THRESH_MAX = 255;
	
	
	public final static Scalar BLUE = new Scalar(255,0,0);
	public final static Scalar GREEN = new Scalar(0,255,0);
	public final static Scalar ORANGE = new Scalar(0,160,255);
	
	private final static List<MatOfPoint> CONTOURS = new ArrayList<MatOfPoint>();
	private final static List<Rect> BOUNDING_RECTS = new ArrayList<Rect>();
	private final static ExecutorService exec =  Executors.newWorkStealingPool();
	
	int hue_lo;
	int sat_lo;
	int lum_lo;
	int hue_hi;
	int sat_hi;
	int lum_hi;
	
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
		hue_lo = (int)CowDash.getNum("Vision_Hue_Lo", 50);
		sat_lo = (int)CowDash.getNum("Vision_Sat_Lo", 150);
		lum_lo = (int)CowDash.getNum("Vision_Lum_Lo", 115);
		hue_hi = (int)CowDash.getNum("Vision_Hue_Hi", 100);
		sat_hi = (int)CowDash.getNum("Vision_Sat_Hi", 255);
		lum_hi = (int)CowDash.getNum("Vision_Lum_Hi", 255);
		
	}
	
	@Override
	public void run() {
		try{
			if(camera.getImg() == null){
				return;
			}
			Mat editImage = camera.getImg();
			Mat debugImage = editImage.clone();
			
			//TODO: Scrap this and/or make something new to make this work
			
			// Filters for Correct Color
			filterImage(editImage);
			// Finds Contours :D
			Imgproc.findContours(editImage.clone(), CONTOURS, new Mat(), Imgproc.RETR_LIST,
					2, new Point(0, 0)); // 2 is Chain_Approx_Simple
			
			editImage.release();
			
			
			for (int i=0;i<CONTOURS.size();i++) {
				Rect bbox= Imgproc.boundingRect(CONTOURS.get(i));
				BOUNDING_RECTS.add(bbox);
				exec.submit(()->{
					//if (CowDash.getBool("Vision_debug", true)) Imgproc.drawContours(debugImage, CONTOURS, x, ORANGE, 2);
					if (CowDash.getBool("Vision_debug", true)) Core.rectangle(debugImage, new Point(bbox.x,bbox.y), new Point(bbox.x+bbox.width, bbox.y+bbox.height), BLUE, 2);
				});
			}
			
			CONTOURS.clear();
	
			
			BOUNDING_RECTS.sort(new Comparator<Rect>() {
				@Override
				public int compare(Rect lhs, Rect rhs) {
					if(lhs.area() < rhs.area()){
						return 1;
					}else if(lhs.area() > rhs.area()){
						return -1;
					}else if(lhs.area() == rhs.area()){
						return 0;
					}else{
						return 0;
					}
				}
			});
			
	
			if (BOUNDING_RECTS.size() > 0) {
				
//				Rect biggestRect = null;
				List<Rect> possibleRects = new ArrayList<Rect>();
				for (int i=0; i<BOUNDING_RECTS.size(); i++) {
					Rect thisRect = BOUNDING_RECTS.get(i);
					double AR = ((double)thisRect.width)/((double)thisRect.height);
					if (AR > CowDash.getNum("Vision_min_ratio", 1) &&  AR < CowDash.getNum("Vision_max_ratio", 2) && thisRect.area() > CowDash.getNum("Min Rect Area", 500)){
						possibleRects.add(thisRect);
//						biggestRect = thisRect;
					}
				}	
				BOUNDING_RECTS.clear();
				
				if (!possibleRects.isEmpty()) {
					possibleRects.sort(new Comparator<Rect>() {
						@Override
						public int compare(Rect lhs, Rect rhs) {
							if(lhs.tl().x < rhs.tl().x){
								return -1;
							}else if(lhs.tl().x > rhs.tl().x){
								return 1;
							}else if(lhs.tl().x == rhs.tl().x){
								return 0;
							}else{
								return 0;
							}
						}
					});
					
					Rect biggestRect = possibleRects.get(0);
					
					Point center = new Point(biggestRect.x + biggestRect.width/2, biggestRect.y + biggestRect.height/2);
					CowDash.setNum("Vision_target_x", center.x);
					CowDash.setNum("Vision_target_y", center.y);
					if (CowDash.getBool("Vision_debug", true)) Core.circle(debugImage, center, 2, GREEN, -1);
					final double angleX = Camera.DEG_PER_PX * (center.x - (Camera.FRAME_WIDTH / 2));
					final double angleY = -Camera.DEG_PER_PX * (center.y - (Camera.FRAME_HEIGHT / 2));
					DriverStation.reportError("Angle X :" + angleX + " (Angle Y) :" + angleY , false);
					final double distance = 0; // x = angle of Shooter (FROM ENCODER);
												// (77.5/12)/Math.tan(x+angleY);
					DriverStation.reportError("\n Area : " + biggestRect.area() , false);
					latestTarget = new Target(angleX, angleY, distance, biggestRect, debugImage);
				} else {
					latestTarget = null;
				}
				
			} else {
				latestTarget = null;
			}
			drawCrosshairs(debugImage);
			latestImage = debugImage;
		
		}catch(NullPointerException e){
			DriverStation.reportError("ShooterImageProcessor.run error", true);
			BOUNDING_RECTS.clear();
			CONTOURS.clear();
		}
	}

	private void drawCrosshairs(Mat img){
		int radius = (int)CowDash.getNum("Vision_Crosshair_Rad", 5);
		int x_offset = (int)CowDash.getNum("Vision_Crosshair_X", 0);
		int y_offset = (int)CowDash.getNum("Vision_Crosshair_Y", 0);
		// Horizontal Line
		Core.line(img, 
				new Point(Camera.FRAME_WIDTH/2 - radius + x_offset, Camera.FRAME_HEIGHT/2 - y_offset),  
				new Point(Camera.FRAME_WIDTH/2 + radius + x_offset, Camera.FRAME_HEIGHT/2 - y_offset)
		, ORANGE, 1);
		// Vertical Line
		Core.line(img, 
				new Point(Camera.FRAME_WIDTH/2 + x_offset, Camera.FRAME_HEIGHT/2 - radius - y_offset),  
				new Point(Camera.FRAME_WIDTH/2 + x_offset, Camera.FRAME_HEIGHT/2 + radius - y_offset)
		, ORANGE, 1);
		// Circle
//		Core.circle(img, new Point(FRAME_WIDTH/2 + (int)CowDash.getNum("Vision_Crosshair_X", 0),FRAME_HEIGHT/2 - (int)CowDash.getNum("Vision_Crosshair_Y", 0)), radius, ORANGE);
	}
	/**
	 * Blurs the Image a bit, Converts it to HSV, Applies an RGB Filter, and
	 * Then a Threshold
	 */
	private void filterImage(Mat image) {
		
		hue_lo = (int)CowDash.getNum("Vision_Hue_Lo", 50);
		sat_lo = (int)CowDash.getNum("Vision_Sat_Lo", 150);
		lum_lo = (int)CowDash.getNum("Vision_Lum_Lo", 115);
		hue_hi = (int)CowDash.getNum("Vision_Hue_Hi", 100);
		sat_hi = (int)CowDash.getNum("Vision_Sat_Hi", 255);
		lum_hi = (int)CowDash.getNum("Vision_Lum_Hi", 255);
		
		// TODO: GREEN
		Imgproc.medianBlur(image, image, 3);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2HSV);
		Core.inRange(image, new Scalar(hue_lo, sat_lo, lum_lo), new Scalar(hue_hi, sat_hi, lum_hi),
				image);
		
		//int thresh = (int) CowDash.getNum("thresh_lo", 100);
		//int thresh_max = (int) CowDash.getNum("thresh_hi", 200);
//		Imgproc.threshold(image, image, thresh, thresh_max,
//				Imgproc.THRESH_BINARY);
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