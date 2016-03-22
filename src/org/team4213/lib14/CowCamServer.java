package org.team4213.lib14;

import java.util.TimerTask;






import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

public class CowCamServer extends TimerTask{

	int session;
    Image frame;


	/*
	 * Initializer for Camera Server . Takes a Server Port
	 */
	public CowCamServer() {
		
		frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web interface
        session = NIVision.IMAQdxOpenCamera("cam1",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
    	NIVision.IMAQdxStartAcquisition(session);
    	
    	CameraServer.getInstance().setQuality(50);
    	CameraServer.getInstance().setSize(1);
		
	}
	
	/*
	 * Returns a Runnable Function that Runs the Camera Server
	 */
	@Override
	public void run() {
        NIVision.IMAQdxGrab(session, frame, 1);
        CameraServer.getInstance().setImage(frame);

	}
}
