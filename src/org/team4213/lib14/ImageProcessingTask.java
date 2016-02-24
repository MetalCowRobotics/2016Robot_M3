package org.team4213.lib14;

import java.util.Optional;

import org.opencv.core.Mat;

public interface ImageProcessingTask{

	public void setImage(Mat img);

	abstract Optional<Target> call();
}
