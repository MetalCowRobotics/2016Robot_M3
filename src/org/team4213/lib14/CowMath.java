/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.team4213.lib14;

/**
 *
 * @author Thaddeus Hughes
 */
public class CowMath {
    /**
     * Copies sign from one variable into the other
     * @param from variable to copy sign from
     * @param to variable to apply it to
     * @return abs(to) if from positive, -abs(to) if from negative, 0 if from=0
     */
    public static double copySign(double from, double to) {
        if (from>0) return Math.abs(to);
        else if (from<0) return -Math.abs(to);
        else return 0;
    }
    
    /**
     * Transforms the input to a power curve in (0, infinity), mirrored about x and y axes.
     * @param in the input variable
     * @param power the power to raise it to
     * @return sign(in) * abs(in)^power
     */
    public static double expScale(double in, double power) {
        return copySign(in, Math.pow(Math.abs(in), power));
    }
    
    public static double limitToSignless(double in, double limit) {
        if (in>limit) return limit;
        else if (in<-limit) return -limit;
        else return in;
    }
    
    /**
     * Normalizes powers. Also known as "proportional constrain"
     * Finds the largest magnitude power, and if it is over the limit, scales all powers down by a factor so they fit within the limit.
     * Why? Useful to produce "proper" responses for drivetrains that preserve relative power, but not absolute power (you can't output 200% power)
     * 
     * @param powers array of powers to normalize
     * @param to maximum value allowable
     * @return array of powers, normalized
     */
    public static double[] normalize(double[] powers, double to) {
        double maxMag = to;
        for (int i=0; i<powers.length; i++) {
            if (Math.abs(powers[i]) > maxMag)
                maxMag = Math.abs(powers[i]);
        }
        if (maxMag <= to) return powers;
        for (int i=0; i<powers.length; i++) {
            powers[i] = powers[i] * to/maxMag;
        }
        return powers;
    }
    
    /**
     * Normalizes powers to 1.
     * @param powers
     * @return normalize(powers, 1);
     */
    public static double[] normalize(double[] powers){
        return normalize(powers, 1);
    }
}
