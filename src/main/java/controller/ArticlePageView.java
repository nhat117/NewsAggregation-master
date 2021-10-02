/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Contributor: Bui Minh Nhat s3878174
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

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticlePageView extends ScrollPane {
    public final List<FXMLLoader> FXML_LOADER_LIST;

    private final int PAGE;

    // init the object ArticlePageView
    public ArticlePageView(int PAGE) {
        // Create a list of cards holder
        FXML_LOADER_LIST = new ArrayList<>(10);
        this.PAGE = PAGE;
        // Applying CSS style for the page
        // the function need to have '/' and it is not related to the OS
        // If using '\\' then it cannot find the style on window because it reads URL not file path
        getStylesheets().add("style/style.css");
        getStyleClass().add("edge-to-edge");
        // Apllying property to ensure the layout of the object is pretty
        setFitToWidth(true);
        vbarPolicyProperty().setValue(ScrollBarPolicy.AS_NEEDED);
        createPage();
    }

    // This method will create a page including cards
    private void createPage() {
        // Create structure base on stackpane
        StackPane stackPane = new StackPane();
        // Apply CSS for the stackpane, apply layout property to make it looks pretty
        // the function need to have '/' and it is not related to the OS
        // If using '\\' then it cannot find the style on window because it reads URL not file path
        stackPane.getStylesheets().add("style/style.css");
        stackPane.getStyleClass().add("dark_background");
        stackPane.setPadding(new Insets(10, 10, 10, 10));
        // Create a flowpane to store all the cards
        FlowPane flowPane = new FlowPane();
        // Set the property so that the cards can layout in the center and in horizontal order
        flowPane.setOrientation(Orientation.HORIZONTAL);
        flowPane.setAlignment(Pos.CENTER);

        // Start creating the cards base on the page position
        int change = 0;
        // If page is 0 mean the first page
        if (PAGE == 0) {
            // This will be different layout than normal layout
            // Add the main big card
            flowPane.getChildren().add(createSpecialCard(0));
            VBox subSection = new VBox();
            subSection.setAlignment(Pos.CENTER);
            subSection.setSpacing(10);
            // Add the 3 sub-cards
            for (int i = 0; i < 3; i++) {
                subSection.getChildren().add(createSpecialCard(1));
            }
            flowPane.getChildren().add(subSection);
            // Set up the card already add to the flowpane
            change = 4;
        }
        stackPane.getChildren().add(flowPane);
        setContent(stackPane);
        try {
            // Create a list of cards from either 4th position or 1st position
            for (int i = change; i < 10; i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("MainCardView.fxml"));
                FXML_LOADER_LIST.add(fxmlLoader);
                AnchorPane pane = fxmlLoader.load();
                flowPane.getChildren().add(pane);
            }
            flowPane.setHgap(10);
            flowPane.setVgap(10);
            flowPane.setPadding(new Insets(0, 10, 0, 10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // This function responsible for create special cards (main 1 big card, and 3 sub-cards)
    private AnchorPane createSpecialCard(int i) {
        FXMLLoader fxmlLoader = new FXMLLoader();
        String cardType;
        switch (i) {
            case 0:
                cardType = "MainCardView.fxml";
                break;
            case 1:
                cardType = "SubCardView.fxml";
                break;
            default:
                return null;
        }
        fxmlLoader.setLocation(getClass().getResource(cardType));
        FXML_LOADER_LIST.add(fxmlLoader);
        AnchorPane anchorPane = null;
        try {
            anchorPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create the main card
        if (i == 0) {
            AnchorPane articleMain = new AnchorPane();
            articleMain.getChildren().clear();
            articleMain.getChildren().add(anchorPane);
            return articleMain;
        } else {
            // create the sub-card
            return anchorPane;
        }
    }
}
