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
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML
    private VBox sidebar;

    @FXML
    private Button newBar, covidBar, politicsBar, businessBar, technologyBar, healthBar, sportsBar, entertainmentBar, worldBar, othersBar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set the sidebar to be invisible init
        sidebar.setVisible(false);
        newBar.setVisible(false);
        covidBar.setVisible(false);
        politicsBar.setVisible(false);
        businessBar.setVisible(false);
        technologyBar.setVisible(false);
        healthBar.setVisible(false);
        sportsBar.setVisible(false);
        entertainmentBar.setVisible(false);
        worldBar.setVisible(false);
        othersBar.setVisible(false);
    }

    // This function will clean the popup by make it invisible
    public void cleanEffect(int n) {
        sidebar.setVisible(false);
        switch (n) {
            case 0:
                newBar.setVisible(false);
                break;
            case 1:
                covidBar.setVisible(false);
                break;
            case 2:
                politicsBar.setVisible(false);
                break;
            case 3:
                businessBar.setVisible(false);
                break;
            case 4:
                technologyBar.setVisible(false);
                break;
            case 5:
                healthBar.setVisible(false);
                break;
            case 6:
                sportsBar.setVisible(false);
                break;
            case 7:
                entertainmentBar.setVisible(false);
                break;
            case 8:
                worldBar.setVisible(false);
                break;
            case 9:
                othersBar.setVisible(false);
                break;
        }
    }

    // This function will make the sidebar popup by make it visible
    public void setButtonEffect(int n) {
        sidebar.setVisible(true);
        switch (n) {
            case 0:
                newBar.setVisible(true);
                break;
            case 1:
                covidBar.setVisible(true);
                break;
            case 2:
                politicsBar.setVisible(true);
                break;
            case 3:
                businessBar.setVisible(true);
                break;
            case 4:
                technologyBar.setVisible(true);
                break;
            case 5:
                healthBar.setVisible(true);
                break;
            case 6:
                sportsBar.setVisible(true);
                break;
            case 7:
                entertainmentBar.setVisible(true);
                break;
            case 8:
                worldBar.setVisible(true);
                break;
            case 9:
                othersBar.setVisible(true);
                break;
        }
    }
}
