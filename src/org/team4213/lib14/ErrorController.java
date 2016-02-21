/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;

/**
 * This is a nifty little class to encapsulate the dirty work of a control loop.
 *
 * @author hughest1
 */
public abstract class ErrorController {
    // The current target this controller should seek
    public double target;
    // A hashtable (dictionary) that maps named setpoints (I.E. "North"->0, "East"->90) to values
    Hashtable targetsMap;

    // Prefix/identifier when logging/tweaking this controller
    protected String name;
    
    public ErrorController(String name) {
        this.name=name;
        this.targetsMap = new Hashtable();
        this.target=0;
    }
    
    /**
     * Adds a named target to the targetsMap
     * @param targetKey the name of the setpoint
     * @param targetValue the value of the setpoint
     */
    public void addTarget(String targetKey, double targetValue) {
        targetsMap.put(targetKey, new Double(targetValue));
    }
    
    /**
     * Sets the current target to this value
     * @param currentTarget the new setpoint
     */
    public void setTarget(double currentTarget) {
        this.target = currentTarget;
    }
    
    /**
     * Computes a new response based on current value
     * @param currentValue the current system position
     * @return the response to create
     */
    abstract public double feedAndGetValue(double currentValue);
    
    /**
     * Sets the current target to the value mapped to the key
     * @param targetKey the named setpoint to target
     */
    public void setTarget(String targetKey) {
        Double res = (Double) targetsMap.get(targetKey);
        if (res!=null) target = res.doubleValue();
    }
}
