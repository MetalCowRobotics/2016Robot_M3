package org.usfirst.frc.team4213.robot.systems;

import org.team4213.lib14.CowDash;

public final class RobotMap {

	// IDEA: - to extrapolate to a .txt file
	// make these into private variables with public getters() and setters()
	// Then in robotInit() read in the text file and use all the setter() to
	// build the robotMap
	// Then rather than reading FINALS actually call the getters()

	public final class Drivetrain {
		public static final int LEFT_MOTOR_CHANNEL = 0;
		public static final int RIGHT_MOTOR_CHANNEL = 1;
		public static final double SPRINT_SPEED = 1;
		public static final double NORMAL_SPEED = 0.7;
		public static final double CRAWL_SPEED = 0.5;
	}

	public final class Turret {
		public final class Yaw_Motor {
			public static final int MOTOR_CHANNEL = 1;
			public static final int LIMIT_SWITCH = 8;
			public static final int OFFSET = 0;
			public static final double PPR = 1024;
			public static final double GR = 10;
			public static final double COUNT_PER_DEG = (PPR * GR) / 360;
			public static final int ENC_CH_A = 6;
			public static final int ENC_CH_B = 7;
			public static final int MAX_ANGLE = 30;
			public static final int MIN_ANGLE = -360;
			public static final double ABS_TOLERANCE = 3;
			public static final double BUMP_AMT = 3;
			public static final double MAX_SPEED = 0.45;
		}

		public final class Pitch_Motor {
			public static final int MOTOR_CHANNEL = 3;
			private static final int GEARSGR = 3;
			private static final int PLANETARYGR = 188;
			private static final int PPR = 7;
			public static final double COUNT_PER_DEG = (GEARSGR * PLANETARYGR * PPR) / 360;
			private static final int START_ANGLE = -38;
			public static final int MAX_ANGLE = 80 - START_ANGLE;
			public static final int MIN_ANGLE = 20 - START_ANGLE;
			public static final double ABS_TOLERANCE = 3;
			public static final double BUMP_AMT = 1;
			public static final int ENC_CH_A = 2;
			public static final int ENC_CH_B = 3;

		}

	}

	// Probably need to Add More Here.
	public final class Intake {
		public static final int ROLLER_MOTOR_CHANNEL = 2;

		public static final int PITCH_MOTOR_CHANNEL = 6;
		public static final double INTAKE_SPEED = -1;
		public static final double EJECT_SPEED = 1;

		public static final int ENCODER_CH_A = 4;
		public static final int ENCODER_CH_B = 5;
		public static final int LIMIT_SWITCH_CH = 8;

		private static final double GEARSGR = 4.5;
		private static final double PLANETARYGR = 71;
		private static final int PPR = 7;

		public static final double COUNT_PER_DEG = (GEARSGR * PLANETARYGR * PPR) / 360;
		public static final double LOWER_SPEED = -.6;
		public static final int RAISE_ANGLE = 100;
		public static final double PID_P_VAL = 1.0 / 120;

	}

	public final class Shooter {
		public static final int CAM_CHANNEL = 0;
		public static final int CAM_ENC_CH_A = 0;
		public static final int CAM_ENC_CH_B = 1;
		private static final int CAM_GEARSGR = 3;
		private static final double CAM_PLANETARYGR = 27.0;
		private static final double CAM_PPR = 7.0;
		public static final double CAM_PPD = (CAM_GEARSGR * CAM_PLANETARYGR * CAM_PPR) / 360.0;

		public static final int FLYWHEEL_CHANNEL = 4;
		public static final int FLYWHEEL_CHANNEL_2 = 5;
		public static final int FLYWHEEL_ENC_CH_A = 8;
		public static final int FLYWHEEL_ENC_CH_B = 9;
		public static final double FLYWHEEL_PPR = 100;

	}

	public final static class Camera {
		public static final int FRAME_WIDTH = 320;
		public static final int FRAME_HEIGHT = 240;
		public final static double CAM_FOV_DIAG = 60;
		public static final double DEG_PER_PX = CAM_FOV_DIAG
				/ Math.sqrt(Math.pow(FRAME_WIDTH, 2) + Math.pow(FRAME_HEIGHT, 2));
	}
}