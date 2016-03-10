package org.team4213.lib14;

import org.opencv.core.Mat;

public interface ImageProcessingTask extends Runnable{

	public Mat getImage();
	
	public Target getTarget();
	
}
