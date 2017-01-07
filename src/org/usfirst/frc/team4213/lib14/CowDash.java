package org.usfirst.frc.team4213.lib14;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Hashtable;
import com.sun.squawk.io.BufferedWriter;
import com.sun.squawk.microedition.io.FileConnection;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Enumeration;
import javax.microedition.io.Connector;

/**
 * Convenience wrapper for SmartDashboard
 *
 * @author hughest1
 */
public class CowDash {
    protected static Hashtable values = new Hashtable();
    private static String uri = "./cowdash.table";
    
    static public void load() {
        
    }
    
    /**
     * Under construction
     * 
     * @author hughest1
     * 
     * Saves dash data to cRIO flash
     */
    static public void save() {
        FileConnection fileConnection = null;
        try {
            fileConnection = (FileConnection) Connector.open(uri);
        
            if (!fileConnection.exists()) {
              System.err.println("CowDash.save: Could not find specified file!");
              return;
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileConnection.openOutputStream()));
            for (Enumeration e = values.keys(); e.hasMoreElements();) {
                writer.write("\""+e+"\":");
                Object val = values.get(e);
                if (val instanceof Boolean) writer.write('b');
                if (val instanceof Double) writer.write('f');
                if (val instanceof String) writer.write('s');
                writer.write(val.toString());
                writer.newLine();
            }
            
            fileConnection.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("CowDash.save: Could not open file connection!");
        }
    }
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public double getNum(String key, double fallback) {
        try{
            return SmartDashboard.getNumber(key);
        }catch(Exception e) {
            CowDash.setNum(key, fallback);
            return fallback;
        }
    }
    
    static public void setNum(String key, double value) {
        values.put(key, Double.valueOf(value));
        SmartDashboard.putNumber(key, value);
    }
    
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public boolean getBool(String key, boolean fallback) {
        try{
            return SmartDashboard.getBoolean(key);
        }catch(Exception e) {
            CowDash.setBool(key, fallback);
            return fallback;
        }
    }
    
    static public void setBool(String key, boolean value) {
        values.put(key, Boolean.valueOf(value));
        SmartDashboard.putBoolean(key, value);
    }
    
    /**
     * Gets the value mapped to key from the dashboard.
     * If no value is mapped to this key, adds the fallback to the dashboard and returns this fallback.
     * @param key the key to look up
     * @param fallback the value to fall back to
     * @return the value mapped to key if it exists, otherwise fallback.
     */
    static public String getString(String key, String fallback) {
        try{
            return SmartDashboard.getString(key);
        }catch(Exception e) {
            CowDash.setString(key, fallback);
            return fallback;
        }
    }
    
    static public void setString(String key, String value) {
        values.put(key, value);
        SmartDashboard.putString(key, value);
    }
}
