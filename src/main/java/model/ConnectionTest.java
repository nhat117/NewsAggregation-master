/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Truong Nhat Anh s3878231
        Last modified date: 10/09/2021
        Contributor:
        Acknowledgement:
        1. Checking network connectivity in Java
        https://www.tutorialspoint.com/Checking-internet-connectivity-in-Java
        https://www.geeksforgeeks.org/checking-internet-connectivity-using-java/
 */
package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.net.URLConnection;

public class ConnectionTest implements Runnable {

    // Workflow:
    // Run in the background and try to connect to given urls
    // If connection is normal/abnormal then notify PrimaryController

    private final PropertyChangeSupport propertyChangeSupport;
    private boolean stopThread;

    // Constructor
    public ConnectionTest() {
        stopThread = false;
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    private void checkConnection() {
        while (true) {
            try {
                if (stopThread) { // To stop thread
                    return;
                }
                // Check the connection for every 10 seconds
                Thread.sleep(10_000);
                URL url1 = new URL("https://vnexpress.net/rss");
                URL url2 = new URL("https://tuoitre.vn/");
                URL url3 = new URL("https://thanhnien.vn/rss.html");
                URL url4 = new URL("https://nhandan.vn/");
                URL url5 = new URL("https://zingnews.vn/");
                URLConnection url1Connection = url1.openConnection();
                url1Connection.connect();
                URLConnection url2Connection = url2.openConnection();
                url2Connection.connect();
                URLConnection url3Connection = url3.openConnection();
                url3Connection.connect();
                URLConnection url4Connection = url4.openConnection();
                url4Connection.connect();
                URLConnection url5Connection = url5.openConnection();
                url5Connection.connect();
                doNotify(false);
            } catch (Exception e) {
                // If the connection is bad
                doNotify(true);
            }
        }
    }

    @Override
    public void run() {
        checkConnection();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void doNotify(boolean newValue) {
        propertyChangeSupport.firePropertyChange("Bad internet connection", null, newValue);
    }

    public void end() {
        stopThread = true;
    }
}
