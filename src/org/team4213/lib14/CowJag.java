/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.Jaguar;

/**
 * Convenience class for Jaguar Motor Controller
 *
 * @author Thaddeus Hughes
 */
public class CowJag extends Jaguar {
    private double exponentialScaling;
    private boolean invert;
    
    public CowJag(int port) {
        super(port);
        this.exponentialScaling=1;
        this.invert=false;
    }
    public CowJag(int port, boolean invert) {
        super(port);
        this.exponentialScaling=1;
        this.invert=invert;
    }
    
    /**
     * 
     * @param port what PWM port the Victor is on
     * @param invert whether or not to flip the polarity of the output
     * @param exponentialScaling the exponential scaling factor to apply to this. I.E. a factor of 0.5 will make output scale like sqrt(power) rather than power.
     */
    public CowJag(int port, boolean invert, double exponentialScaling) {
        super(port);
        this.exponentialScaling=exponentialScaling;
        this.invert=invert;
    }
    
    /**
     * Set power output of the Jaguar
     * @param power 
     */
    public void set (double power) {
        super.set((invert ? -1:1)*CowMath.expScale(power, exponentialScaling));
    }
}
