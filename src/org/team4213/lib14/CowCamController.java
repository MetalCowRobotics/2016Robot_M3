package org.team4213.lib14;

import java.io.IOException;
import java.util.Optional;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReadWriteLock;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;





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

public class CowCamController extends TimerTask{

	// The Video Stream from which we Get the Images
	private VideoCapture videoCapture = new VideoCapture();;
	// The Image Being Streamed from the Camera
	private Mat camImage = new Mat();
	// Boolean to Handle Running State
	private volatile boolean isRunning = true;
	// The Locks for the Two Cross Thread Accessed Variables
	
	private boolean opened = false;
	private int fps;
	private int cameraPort;
	
	private static final MatOfInt ENC_PARAMS = new MatOfInt(Highgui.IMWRITE_JPEG_QUALITY, 10 * 50);
	
	private static final MatOfByte TMP_MAT_BYTE = new MatOfByte();
	

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
	 * 
	 * @see DriverStation
	 * @see VideoCapture
	 * @see Optional
	 * @see Callable
	 */
	public CowCamController(int cameraPort, int fps) {
		this.cameraPort = cameraPort;
		this.fps = fps;

		// Reports that a Camera is Being Started to the Driver Station
		
		// Attempts to Open the Camera
		
		// Set the Camera's Settings
		

		// Reads first Image
		// videoCapture.read(camImage);

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
		
		String[] auto_exp_cmd = {"v4l2-ctl","--set-ctrl=exposure_auto=1"};
		
		try {
			Runtime.getRuntime().exec(auto_exp_cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setTrackingSettings();
		
	}
	
	public void setTrackingSettings() {
		setExposure((int)CowDash.getNum("Vision_Tracking_Exposure", 25));
	}
	
	public void setHumanFriendlySettings() {
		setExposure((int)CowDash.getNum("Vision_Human_Exposure", 500));
	}

	private void setExposure(int exposure){
		DriverStation.reportError("CamController.setExposure call", true);
		String[] set_exp_cmd = {"v4l2-ctl","--set-ctrl=exposure_absolute=" + exposure};
		try {
			Runtime.getRuntime().exec(set_exp_cmd);
		} catch (IOException e) {
			DriverStation.reportError("CamController.setExposure error", true);
			e.printStackTrace();
		}
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
	public byte[] getImgAsBytes( Mat img ) {
		// Encoded the Image into JPEG and Stores it in the Mat of Bytes
		//imageLock.readLock().lock();
		Highgui.imencode(".jpg", img, TMP_MAT_BYTE, ENC_PARAMS);
		//imageLock.readLock().unlock();
		
		// Returns the Mat of Bytes as an Array
		byte[] imgArray = TMP_MAT_BYTE.toArray();
		TMP_MAT_BYTE.release();
		return imgArray;
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
		// Mat img = camImage.clone();
		//imageLock.readLock().unlock();
		
		return camImage.clone();
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
	public void start() {
		// Sets Current Running State to True
		isRunning = true;
		
		// Then We run the Infinite Camera Read Loop
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
	@Override
	public void run() {
		if (!opened) {
			try {
				videoCapture.open(cameraPort, 320, 240, fps);
				setCameraOptions();
				
			} catch (Exception ex) {
				return;
			}
			opened=true;
			DriverStation.reportError("Started camera at: " + cameraPort, false);
		}
		if (isRunning) {
			videoCapture.read(camImage);
			
		}
	}
	
}
