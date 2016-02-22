package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.systems.RobotMap.Shooter;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

public class ShooterMap {
	public static final CANTalon camMotor = new CANTalon(Shooter.CAM_CHANNEL);
	public static final CANTalon flywheelMotor = new CANTalon(Shooter.FLYWHEEL_CHANNEL);
	public static final Encoder camEncoder = new Encoder(Shooter.ENC_CH_A, Shooter.ENC_CH_B, false,
			CounterBase.EncodingType.k4X);
	public static final DigitalInput ballLimitSwitch = new DigitalInput(Shooter.LIMIT_SWITCH);

	public void setCamSpeed(double speed) {
		camMotor.set(speed);
	}

	public void setWheelSpeed(double speed) {
		flywheelMotor.set(speed);
	}

	public boolean getSwitchHit() {
		return ballLimitSwitch.get();
	}

	public double getEncValue() {
		return camEncoder.get();
	}

	public double getEncDist() {
		return camEncoder.getDistance();
	}

	public void resetEnc() {
		camEncoder.reset();
	}
}
