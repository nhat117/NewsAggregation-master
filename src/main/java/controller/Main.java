/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        1. JavaFX FXML Loader
        https://docs.oracle.com/javase/8/javafx/api/javafx/fxml/FXMLLoader.html
        2. Decide Stage size by knowing screen resolution
        https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java
        3. Find the window taskbar height
        https://stackoverflow.com/questions/6844996/windows-taskbar-height-width
 */
package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Article;

import java.awt.*;
import java.util.Locale;

public class Main extends Application {

    private static Scene scene;
    private static FXMLLoader primaryFxmlLoader;
    private static FXMLLoader secondaryFxmlLoader;

    @Override
    public void start(Stage stage) throws Exception {
        //Update
        // Load and save the primaryFxmlLoader and secondaryFxmlLoader for further reusable
        // This is the URL so no need to change file separate
        primaryFxmlLoader = new FXMLLoader(Main.class.getResource("/PrimaryView.fxml"));
        secondaryFxmlLoader = new FXMLLoader(Main.class.getResource("/SecondaryView.fxml"));
        secondaryFxmlLoader.load();
        scene = new Scene(primaryFxmlLoader.load());
        // Get the stage into the primary controller to listen for app shutdown
        PrimaryController primaryController = primaryFxmlLoader.getController();
        primaryController.ready(stage);
        stage.setScene(scene);
        // Set up the screen size base on the monitor's screen resolution and size
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        // Check the application os
        String OS = System.getProperty("os.name", "unknown").toLowerCase(Locale.ROOT);
        if(OS.contains("win")) {
            //Check taskbar size
            Dimension scrnSize = Toolkit.getDefaultToolkit().getScreenSize();
            Rectangle winSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            int taskBarHeight = scrnSize.height - winSize.height;
            stage.setWidth(width);
            stage.setHeight(height - taskBarHeight + 15);
        } else {
            stage.setWidth(width);
            stage.setHeight(height);
        }
        // Set the minimum width and height of the application
        stage.setMinWidth(720);
        stage.setMinHeight(730);
        stage.setTitle("L I T");
        stage.show();
    }


    // Change the root to secondary view
    public static void setRoot(Article article) {
        SecondaryController secondaryController = secondaryFxmlLoader.getController();
        secondaryController.setArticle(article);
        secondaryController.setupView();
        scene.setRoot(secondaryFxmlLoader.getRoot());
    }

    // Change the root to the primary view
    public static void setRoot() {
        scene.setRoot(primaryFxmlLoader.getRoot());
    }

    // Run main
    public static void main(String[] args) {
        launch();
    }
}
