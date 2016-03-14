package org.team4213.lib14;

public class TBHController{
	
	private double lastPWM;
	private double desiredRPS;
	private double lastError;
	private double ki;
	private final String name;
	
	// Throw Half Back Controller ( Google The Name )
	
	public TBHController(String name){
		this.name = name;
		lastPWM = 0;
	}
	public void setTarget(double rps){
		desiredRPS = rps;
	}

	public double feedAndGetValue(double currentRPS, double currentPWM) {
		CowDash.setNum(name+"::currentRPS", currentRPS);
		CowDash.setNum(name+"::currentPWM", currentPWM);
		if(desiredRPS == 0){
			return 0;
		}
		
		if(lastError == 0){
			lastError =  desiredRPS - currentRPS;
		}
		final double newError = desiredRPS - currentRPS;
		ki = CowDash.getNum(name + "::ki", 1)/1000;
		final double newPWM;
		if(newError*lastError < 0){
			newPWM = (currentPWM+lastPWM)/2;
		}else if(newError * lastError > 0){
			newPWM = (lastError*ki) +currentPWM;
		}else{
			newPWM = currentPWM;
		}
		
		lastError = newError;
		lastPWM = currentPWM;
		return newPWM;
	}
	
}
