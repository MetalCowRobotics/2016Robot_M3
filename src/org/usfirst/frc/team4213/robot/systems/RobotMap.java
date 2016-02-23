package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.DigitalSource;

public final class RobotMap {
	
	//TODO - to extrapolate to a .txt file
	//make these into private variables with public getters() and setters()
	//Then in robotInit() read in the text file and use all the setter() to build the robotMap
	//Then rather than reading FINALS actually call the getters()

	public final class Drivetrain {
		public static final int LEFT_MOTOR_CHANNEL = 8;
		public static final int RIGHT_MOTOR_CHANNEL = 9;
		public static final double SPRINT_SPEED = 1;
		public static final double NORMAL_SPEED = 0.6;
		public static final double CRAWL_SPEED = 0.4;
	}

	public final class Turret {
		public final class Yaw_Motor {
			public static final int MOTOR_CHANNEL = 1;
			public static final int STRING_POT = 0;
			public static final int LIMIT_SWITCH = 8;
			public static final int OFFSET = 0;
			public static final int START_COUNT = 500 + OFFSET;
			public static final int MIN_COUNT = 400 + OFFSET;
			public static final int MAX_COUNT = 600 + OFFSET;
			private static final int COUNT_PER_DEG = 100 / 360;

		}

		public final class Pitch_Motor {
			public static final int MOTOR_CHANNEL = 3;
			private static final int GEARSGR = 3;
			private static final int PLANETARYGR = 71;
			private static final int PPR = 5; // Keep Track of this. This may
												// not be accurate.
			public static final int COUNT_PER_DEG = (GEARSGR * PLANETARYGR * PPR) / 360;
		}

	}

	// Probably need to Add More Here.
	public final class Intake {
		public static final int ROLLER_MOTOR_CHANNEL = 2;
		public static final int PITCH_MOTOR_CHANNEL = 6; 
		public static final double INTAKE_SPEED = -1;
		public static final double EJECT_SPEED = 1;
		public static final int ENCODER_CH_A = 0;
		public static final int ENCODER_CH_B = 1;
		private static final double GEARSGR = 4.5;
		private static final double PLANETARYGR = 71;
		private static final int PPR = 7; 
		
		public static final double COUNT_PER_DEG = (GEARSGR * PLANETARYGR * PPR) / 360;
		public static final int RAISE_ANGLE = 90;
	}

	public final class Shooter {
		public static final int FLYWHEEL_CHANNEL = 3;
		public static final int CAM_CHANNEL = 0;
		public static final int ENC_CH_A = 4;
		public static final int ENC_CH_B = 5;
		private static final int GEARSGR = 3;
		private static final int PLANETARYGR = 27;
		private static final int PPR = 5; // TODO Potentially need to Change
		public static final int COUNT_PER_DEG = (GEARSGR * PLANETARYGR * PPR) / 360;

	}
}