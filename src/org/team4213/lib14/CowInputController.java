package org.team4213.lib14;

public interface CowInputController {
	public double getRX();

	public double getRY();

	public double getLX();

	public double getLY();

	public boolean getButton(int n);

	public int getDpadY();

	public int getDpadX();

	public double getThrottle(double topSpeedNormal, double topSpeedCrawl, double topSpeedSprint);

	public void prestep();

	public void endstep();

	public boolean getButtonTripped(int n);

	public boolean getButtonReleased(int n);

	public boolean getButtonToggled(int n);

}
