/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Acknowledgement:
        1. MVC observable pattern
        https://www.baeldung.com/java-observer-pattern
        2. Thank you, Professor Nick Wergeles for explaining the concept of:
            - JavaThread Service and Task
            - MVC model
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */

package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.effect.Lighting;
import javafx.scene.input.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;

public class NavigationController implements Initializable, PropertyChangeListener {

    private int currentPage;

    @FXML
    private Button page1, page2, page3, page4, page5, prevPage, nextPage;

    private PropertyChangeSupport propertyChangeSupport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup the init button pressed
        setCurrentButton();
        // Make this instance become the observable
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    // This method is to get the primary controller instance and to add this class as an observer into the controller
    void injectController(PrimaryController primaryController) {
        primaryController.addPropertyChangeListener(this);
    }

    // This function will clean the effect of selecting button
    private void cleanEffect() {
        switch (currentPage) {
            case 0:
                page1.setEffect(null);
                break;
            case 1:
                page2.setEffect(null);
                break;
            case 2:
                page3.setEffect(null);
                break;
            case 3:
                page4.setEffect(null);
                break;
            case 4:
                page5.setEffect(null);
                break;
        }
    }

    // This function will set the effect of selecting the button
    private void setButtonEffect() {
        Lighting lighting = new Lighting();
        switch (currentPage) {
            case 0:
                page1.setEffect(lighting);
                break;
            case 1:
                page2.setEffect(lighting);
                break;
            case 2:
                page3.setEffect(lighting);
                break;
            case 3:
                page4.setEffect(lighting);
                break;
            case 4:
                page5.setEffect(lighting);
                break;
        }
    }

    // This function will set the init button which is page 1
    void setCurrentButton() {
        cleanEffect();
        currentPage = 0;
        setButtonEffect();
    }

    // This function will listen for the mouse click on the button and decide which page to change
    @FXML
    private void setCurrentButton(MouseEvent event) {
        int oldPage = currentPage;
        cleanEffect();
        Object source = event.getSource();
        // If they click on nextpage and still in the range between page 1 and 4 then change
        if (source == nextPage) {
            if (currentPage + 1 >= 0 && currentPage + 1 <= 4) {
                currentPage++;
            }
        }
        // If they click on previous page and still in the range between page 2 and 5 then change
        if (source == prevPage) {
            if (currentPage - 1 >= 0 && currentPage - 1 <= 4) {
                currentPage--;
            }
        }
        // If the usher click page 1 then change to page 1
        if (source == page1) {
            currentPage = 0;
        }
        // If the user click page 2 then change to page 2
        if (source == page2) {
            currentPage = 1;
        }
        // If the user click page 3 then change to page 3
        if (source == page3) {
            currentPage = 2;
        }
        // If the user click page 4 then change to page 4
        if (source == page4) {
            currentPage = 3;
        }
        // If the user click page 5 then change to page 5
        if (source == page5) {
            currentPage = 4;
        }
        // Set the selected button
        setButtonEffect();
        // Notify primary contorller if the page is changing base on the oldpage save at the beginning
        doNotify(oldPage);
    }

    // Add the observer
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    // Remove the observer not use yet but maybe for further development usage
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    // This function will notify the listener (observer) to get the update information in this case current page
    private void doNotify(int oldPage) {
        propertyChangeSupport.firePropertyChange("currentPage", oldPage, currentPage);
    }

    // This function will receive the notify from the primary whether user has change the category or not
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("currentPage")) {
            // If category change then reset to page 1
            setCurrentButton();
        }
    }
}
