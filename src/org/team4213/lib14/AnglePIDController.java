/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 * PID Controller for a system such as a turntable, where quickly hitting the heading is desired, and wraparound isn't an issue.
 * I.E. error between 270 and 0 is -90, not 270.
 * @author hughest1
 */
public class AnglePIDController extends PIDController {

    public AnglePIDController(String prefix, double kp, double ki, double maxIInfluence, double kd) {
        super(prefix, kp, ki, maxIInfluence, kd);
    }
    
    /**
     * Like stock PID feedAndGetValue, but optimized for rotations.
     * @param currentAngle the current angle value
     * @return response
     */
    public double feedAndGetValue(double currentAngle) {
        while(target-currentAngle > 180) currentAngle+=360;
        while(target-currentAngle < -180) currentAngle-=360;
        return super.feedAndGetValue(currentAngle);
    }
}
