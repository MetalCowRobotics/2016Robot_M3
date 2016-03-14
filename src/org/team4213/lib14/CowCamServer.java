package org.team4213.lib14;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.TimerTask;


import org.opencv.core.Mat;

// Import WPILib's Driver Station Item
import edu.wpi.first.wpilibj.DriverStation;

public class CowCamServer extends TimerTask{

	// The Camera Controller to Stream From
	private CowCamController cameraController;
	ImageProcessingTask task;
	// Boolean Representing Streaming State
	private volatile boolean isStreaming = false;
	// Socket to Stream From
	private ServerSocket socket;
	// Bytes to Use for Streaming
	private static final byte[] kMagicNumber = { 0x01, 0x00, 0x00, 0x00 };
	
	private Socket s;
	//private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean reconnectNeeded;
	//private int fps;
	// NOT USED ATM >>> COULD BE ? 
	//private int compression;


	/*
	 * Initializer for Camera Server . Takes a Server Port
	 */
	public CowCamServer(int port, CowCamController cam, ImageProcessingTask task) {
		setupSocket(port);
		reconnectNeeded = true;
		this.cameraController = cam;
		this.task = task;
		isStreaming = true;
		
		
	}

	/*
	 * Creates a Socket for the Camera Server on the Desired port ( 1180 )
	 */
	private void setupSocket(int port) {
		try {
			socket = new ServerSocket();
			socket.setReuseAddress(true);
			InetSocketAddress address = new InetSocketAddress(port);
			socket.bind(address);
		} catch (IOException ex) {
			DriverStation.reportError("Socket Failed", true);
		}
		
	}

	private void reconnectSocket(){
		try {
			// Starts Socket + Waits for Communication
			s = socket.accept();

			// Acquires the Data Streams
			//inputStream = new DataInputStream(s.getInputStream());
			outputStream = new DataOutputStream(s.getOutputStream());

			// Reads the Data Input Stream
			// fps = inputStream.readInt();
			// compression = inputStream.readInt();
			// Resolution Setting that Could be Implemented Later
			// int size = is.readInt();
			
			reconnectNeeded = false;
		}
		catch (Exception ex){
			reconnectNeeded = true;
		}
	}
	
	/*
	 * Starts the Camera Server with the Desired Camera
	 */
	public void resume() {

		// Sets streaming to True Again
		isStreaming = true;
	}

	/*
	 * Stops the Camera Server
	 */
	public void pause() {
		isStreaming = false;
	}

	/*
	 * Switches the Camera that is Being Streamed
	 */
	public void setCam(CowCamController cam) {
		cameraController = cam;
	}

	/*
	 * Returns a Runnable Function that Runs the Camera Server
	 */
	@Override
	public void run() {
		//DriverStation.reportError("CowCamServer.run\n", false);
		//DriverStation.reportError("CamServer.run", false);
		if (reconnectNeeded){
			DriverStation.reportError("We need Reconnection", false);
			reconnectSocket();
			return;
		}
		
		if(cameraController.getImg() == null){
			return;
		}
		
		if (isStreaming) {
			//DriverStation.reportError("we are streaming", false);
			// Stream Periodically based on FPS
			// long period = (long) (1000 / (1.0 * fps));
			if (isStreaming && cameraController.isRunning()) {
				//long t0 = System.currentTimeMillis();
				
				Mat img;
				// FIGURE OUT STREAMING
				if(task == null){
					img = cameraController.getImg();
					CowDash.setBool("Vision_debug", false);
				}else if (CowDash.getBool("Vision_debug", false)){
					img = task.getImage();
				} else {
					img = cameraController.getImg();
				}
				
				byte[] videoBits = cameraController.getImgAsBytes(img);
				img.release();
				
				// Streams data to Client
				try {
					outputStream.write(kMagicNumber);
					outputStream.writeInt(videoBits.length);
					outputStream.write(videoBits);
					outputStream.flush();
					Thread.yield();
					//long dt = System.currentTimeMillis() - t0;

					// Sleeps to Delay for FPS Setting
//					if (dt < period) {
//						Thread.sleep(period - dt);
//					}

				} catch (IOException | UnsupportedOperationException ex) {
					DriverStation.reportError(ex.getMessage(), true);
					reconnectNeeded = true;
					return;
				}
			}
		}
	}
}
