package org.team4213.lib14;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;


import org.opencv.imgproc.Imgproc;

//Import WPILib's Driver Station Item
import edu.wpi.first.wpilibj.DriverStation;

/**
 * <p>
 * CowCamController is a very simple class that handles most of what one would
 * do with a Camera. It reads the images and then runs a task upon them. The
 * implementation is simple consisting of a few methods that one must deal with
 * and is ready to handle multiple types of data using generics. It also uses
 * <code>Optional</code>s to keep <code>null</code> at bay.
 * <code>Optional</code>s also make it easy to not worry about an Image
 * Processing Task if one simply wanted camera reading functionality.
 * </p>
 * <p>
 * The class manages the Camera quite nicely and tries to abstract as much as
 * possible. For more raw level access to the camera, you should simply go
 * straight to the core OpenCV.
 * </p>
 * 
 * @author KrithikR
 */

public class CowCamController {

	// The Video Stream from which we Get the Images
	private VideoCapture videoCapture = new VideoCapture();;
	// The Image Being Streamed from the Camera
	private Mat camImage = new Mat();
	// Future for the Executed Image Processing
	//private Optional<Future<T>> processFuture;
	// Output from the Image Process Task
	private Optional<Target> dataOutput;
	// Task Given to Execute
	private Optional<ImageProcessingTask> task;
	// Boolean to Handle Running State
	private volatile boolean isRunning = false;
	// The Locks for the Two Cross Thread Accessed Variables
	//private ReadWriteLock imageLock = new ReentrantReadWriteLock();
	//private ReadWriteLock dataOutputLock = new ReentrantReadWriteLock();
	
	//public ExecutorService executor = Executors.newSingleThreadExecutor();

	

	/**
	 * Initializes a Camera Controller with a Port , Camera FPS , And an
	 * Optional Task to Run. The port and fps are <code>int</code>(s) whereas
	 * the Optional Task is a <code>Callable T</code>. The constructor is
	 * created such that a camera exist upon calling the constructor, as
	 * otherwise an infinite loop would be entered. One must also pass it a
	 * <code>Optional.empty()</code> if they intended on not having a task.
	 * 
	 * @param cameraPort
	 *            the integer to specify upon which port the Camera is
	 * @param fps
	 *            the integer to specify the frames per second of the Camera
	 *            Read
	 * @param imgProcess
	 *            <code>Optional Callable T</code> that runs on each Camera
	 *            Read, which the output of can be retrieved by the
	 *            <code>getDataOutput</code> method
	 * 
	 * @see DriverStation
	 * @see VideoCapture
	 * @see Optional
	 * @see Callable
	 */
	public CowCamController(int cameraPort, int fps, ImageTask tsk) {

		// Reports that a Camera is Being Started to the Driver Station
		DriverStation.reportError("Starting a camera at : " + cameraPort, false);
		// Attempts to Open the Camera
		while (true) {
			try {
				videoCapture.open(cameraPort, 320, 240, fps);
			} catch (Exception ex) {
				continue;
			}
			break;
		}

		// Set the Camera's Settings
		setCameraOptions();

		// Sets the Task to The Task being passed In
		switch (tsk){
		case SHOOTER:
			task = Optional.of(new ShooterImageProcessor());
			break;
		default:
			task = Optional.empty();
		}
		// Reads first Image
		videoCapture.read(camImage);

	}
	public CowCamController(int cameraPort, int fps) {

		// Reports that a Camera is Being Started to the Driver Station
		DriverStation.reportError("Starting a camera at : " + cameraPort, false);
		// Attempts to Open the Camera
		while (true) {
			try {
				videoCapture.open(cameraPort, 320, 240, fps);
			} catch (Exception ex) {
				continue;
			}
			break;
		}

		// Set the Camera's Settings
		setCameraOptions();

		// Reads first Image
		videoCapture.read(camImage);

	}
	

	/**
	 * Sets the Camera's Settings when being read. Current implementation sets
	 * the resolution to 320*240px. This method is private and is used mostly
	 * for clarity purposes when reading the constructor :
	 * {@link #CowCamController(int, int, Optional)}.
	 * 
	 * @see VideoCapture
	 */
	private void setCameraOptions() {
		// Sets Frame Width to 320px
		videoCapture.set(Highgui.CV_CAP_PROP_FRAME_WIDTH, 320);
		// Sets Frame Height to 240px
		videoCapture.set(Highgui.CV_CAP_PROP_FRAME_HEIGHT, 240);
	}

	/**
	 * Returns a <code>byte[]</code> from the Camera's Image. This method is
	 * safe for usage by multiple threads. This should only be used if you want
	 * a byte array. If you wanted a standard image <code>Mat</code> use
	 * {@link #getImg()} instead.
	 * 
	 * 
	 * @return <code>byte[]</code> created from the Camera's Image
	 * 
	 * @see MatOfInt
	 * @see MatOfByte
	 * @see Highgui
	 * @see ReadWriteLock
	 */
	public byte[] getImgAsBytes() {

		// Sets JPEG Quality to 10*50
		MatOfInt params = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10 * 50);

		// Creates a Mat of Bytes
		MatOfByte matByte = new MatOfByte();

		// Encoded the Image into JPEG and Stores it in the Mat of Bytes
		//imageLock.readLock().lock();
		Highgui.imencode(".jpg", camImage.clone(), matByte, params);
		//imageLock.readLock().unlock();

		// Returns the Mat of Bytes as an Array
		return matByte.toArray();
	}

	/**
	 * Returns a Clone of the Camera's Image as a <code>Mat</code>. It's
	 * implementation is meant to be thread safe. If you need a byte array from
	 * the image use {@link #getImgAsBytes()} instead.
	 * 
	 * @return <code>Mat</code> that is a clone of the latest camera image
	 * 
	 * @see Mat
	 * @see ReadWriteLock
	 */
	public Mat getImg() {

		//imageLock.readLock().lock();
		Mat img = camImage.clone();
		//imageLock.readLock().unlock();

		return img;
	}

	/**
	 * Returns the Output of the Image Processing Task. It's implementation is
	 * meant to be thread safe. Upon return the Optional must be unwrapped and
	 * checked for existence when reading the data inside.
	 * 
	 * @return Object of type <code>Optional T</code> that contains periodic
	 *         output of the image processing task
	 * 
	 * @see ReadWriteLock
	 * @see #task
	 */
	public Optional<Target> getDataOutput() {

		//dataOutputLock.readLock().lock();
		Optional<Target> output = dataOutput;
		//dataOutputLock.readLock().unlock();

		return output;

	}

	/**
	 * Returns the Current State of the Camera Controller. It is thread safe for
	 * reading and returns {@link #isRunning}, a variable that represents the
	 * run state of the Camera Controller. {@link #isRunning} should be only
	 * writable by the class itself which is why this method exists.
	 * 
	 * @return <code>boolean</code> that represents the current state of the
	 *         camera controller ( whether the camera is being read or not )
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * Adds a New Camera Read and Process Loop to the Inputted Executor and sets
	 * Running to True. The method handles the creation of a infinite loop that
	 * is run on a seperate thread pool created by an Executor. Calling this
	 * method if the controller is already running is pointless.
	 * 
	 * @param executor
	 *            an <code>ExecutorService</code> that can be used to run the
	 *            Camera Loop
	 * 
	 * @see ExecutorService
	 * @see #readCamera(ExecutorService)
	 */
	public void start(ExecutorService executor) {
		// Stops old Execution
		stop();
		// Sets Current Running State to True
		isRunning = true;
		
		// Then We run the Infinite Camera Read Loop
		executor.submit(readCamera(executor));
	}

	/**
	 * Stops the infinite camera read and process loop running in the
	 * background. The current implementation does this through usage of a
	 * volatile variable accessable by the camera loop.
	 * 
	 * @see #readCamera(ExecutorService)
	 */
	public void stop() {
		// Sets running to false
		isRunning = false;
	}

	/**
	 * Returns the Runnable for the Infinite Camera Reading Loop. The returned
	 * Runnable handles many cases of <code>Optional</code>s not existing and is
	 * properly made thread safe. The method is private so it's only called upon
	 * at a reasonable time by the {@link #start(ExecutorService)} function.
	 * 
	 * @param executor
	 *            an <code>ExecutorService</code> that can be used to run the
	 *            Image Processing Task
	 * @return <code>Runnable</code> that contains the infinite camera loop
	 * 
	 * @see #start(ExecutorService)
	 * @see Future
	 * @see Callable
	 * @see VideoCapture
	 * @see Optional
	 * @see ReadWriteLock
	 * @see Runnable
	 */
	private Runnable readCamera(ExecutorService executor) {
		// Returns a Runnable Instance and Returns it
		return () -> {
			// Runs Task for the First Time
			/*if(task.isPresent()){
				task.get().setImage(getImg());
				processFuture = Optional.of(executor.submit(task.get()));
			}else{
				processFuture = Optional.empty();
			}*/
			DriverStation.reportError("Camera Started", false);
			while (isRunning) {
				// Reads Camera to an Image Variable
				//imageLock.writeLock().lock();
				videoCapture.read(camImage);
				//imageLock.writeLock().unlock();

				// Submits Task to Run Async + Get its future if The Process
				// Finished.
				/*if (task.isPresent() && processFuture.isPresent() && processFuture.get().isDone()) {
					// Updates Data from the Future
					//dataOutputLock.writeLock().lock();
					task.get().setImage(getImg());
					try {
						dataOutput = Optional.of(processFuture.get().get());
					} catch (InterruptedException e) {
						DriverStation.reportError(e.getMessage(), true);
					} catch (ExecutionException e) {
						DriverStation.reportError(e.getMessage(), true);
					}
					//dataOutputLock.writeLock().unlock();
					// Replaces the Old Future with a New One
					processFuture = Optional.of(executor.submit(task.get()));
					
				}*/
				task.get().setImage(getImg());
				dataOutput = task.get().call();
				Thread.yield();
			}
		};
	}

	private static class ShooterImageProcessor implements ImageProcessingTask{
		public final static int THRESH = 100;
		public final static int THRESH_MAX = 255;
		public final static int FRAME_WIDTH = 320;
		public final static int FRAME_HEIGHT = 240;
		public final static double CAM_FOV_DIAG = 68.5;
		public final static double DEG_PER_PX = CAM_FOV_DIAG
				/ Math.sqrt(Math.pow(FRAME_WIDTH, 2) + Math.pow(FRAME_HEIGHT, 2));
		Mat camImage;
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
		
		public Optional<Target> call() {
			
			List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
			// Filters for Correct Color
			filterImage();
			// Finds Contours :D
			Imgproc.findContours(camImage, contours, new Mat(), Imgproc.RETR_TREE,
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

			// Write to File Code if Needed For Testing Later
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

			Optional<Target> target;

			if (/* threshold Conditions go here ... need to test */boundingRects.length > 0) {
				final double angleX = DEG_PER_PX
						* (center[biggestRectIndex].x - FRAME_WIDTH / 2);
				final double angleY = DEG_PER_PX
						* (center[biggestRectIndex].y - FRAME_HEIGHT / 2);
				final double distance = 0; // x = angle of Shooter (FROM ENCODER);
											// (77.5/12)/Math.atan(x+angleY);
				target = Optional.of(new Target(angleX, angleY, distance, true));
			} else {
				target = Optional.empty();
			}
			return target;
		}

		/**
		 * Blurs the Image a bit, Converts it to HSV, Applies an RGB Filter, and
		 * Then a Threshold
		 */
		private void filterImage() {
			Imgproc.medianBlur(camImage, camImage, 3);
			Imgproc.cvtColor(camImage, camImage, Imgproc.COLOR_BGR2HSV);
			Core.inRange(camImage, new Scalar(0, 0, 0), new Scalar(100, 100, 100),
					camImage);
			Imgproc.threshold(camImage, camImage, THRESH, THRESH_MAX,
					Imgproc.THRESH_BINARY);
		}

		@Override
		public void setImage(Mat img) {
			camImage = img;
		}
	}
	
	public enum ImageTask{
		SHOOTER,BALL;
	}
	
}
