package org.team4213.lib14;

// Import Various Java Network Classes
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

// Import WPILib's Driver Station Item
import edu.wpi.first.wpilibj.DriverStation;

public class CowCamServer {

	// The Camera Controller to Stream From
	private CowCamController cameraController;
	// Boolean Representing Streaming State
	private volatile boolean isStreaming = false;
	// Socket to Stream From
	private ServerSocket socket;
	// Bytes to Use for Streaming
	private static final byte[] kMagicNumber = { 0x01, 0x00, 0x00, 0x00 };
	
	//public ExecutorService executor = Executors.newSingleThreadExecutor();


	/*
	 * Initializer for Camera Server . Takes a Server Port
	 */
	public CowCamServer(int port) {
		setupSocket(port);
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

	/*
	 * Starts the Camera Server with the Desired Camera
	 */
	public void start(CowCamController cam,ExecutorService executor) {
		
		// Sets Camera
		cameraController = cam;
		// Stops existing Process if there is One
		stop();

		// Sets streaming to True Again
		isStreaming = true;

		// Checks if Camera is Running , Turns it on If Not
		if (!cam.isRunning()) {
			
			cam.start(executor);
		}

		// Sends the Camera Server to be Run
		executor.submit(runServer());
	}

	/*
	 * Stops the Camera Server
	 */
	public void stop() {
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
	private Runnable runServer() {
		return () -> {
			// Runs an Infinite Loop for the Server
			DriverStation.reportError("Starting Server", false);
			//DriverStation.reportError(""+isStreaming, false);
			DriverStation.reportError(""+cameraController.isRunning(), false);
			
			while (isStreaming && cameraController.isRunning()) {
				try {
					DriverStation.reportError("Server Started", false);
					// Starts Socket + Waits for Communication
					Socket s = socket.accept();

					// Acquires the Data Streams
					DataInputStream is = new DataInputStream(s.getInputStream());
					DataOutputStream os = new DataOutputStream(s.getOutputStream());

					// Reads the Data Input Stream
					int fps = is.readInt();
					int compression = is.readInt();

					// Resolution Setting that Could be Implemented Later
					// int size = is.readInt();

					// Checks if Dashboard is Set to HW Camera and Alerts Driver
					// if Not
					if (compression != -1) {
						DriverStation.reportError("Choose \"USB Camera HW\" on the dashboard", false);
						s.close();
						continue;
					}

					// Stream Periodically based on FPS
					long period = (long) (1000 / (1.0 * fps));
					while (isStreaming && cameraController.isRunning()) {
						DriverStation.reportError("reading cam", false);
						long t0 = System.currentTimeMillis();
						byte[] videoBits = cameraController.getImgAsBytes();

						// Streams data to Client
						try {
							os.write(kMagicNumber);
							os.writeInt(videoBits.length);
							os.write(videoBits);
							os.flush();
							Thread.yield();
							long dt = System.currentTimeMillis() - t0;

							// Sleeps to Delay for FPS Setting
							if (dt < period) {
								Thread.sleep(period - dt);
							}

						} catch (IOException | UnsupportedOperationException | InterruptedException ex) {
							DriverStation.reportError(ex.getMessage(), true);
							break;
						}
					}

				} catch (IOException ex) {
					DriverStation.reportError(ex.getMessage(), true);
					continue;
				}

			}
		};
	}

}
