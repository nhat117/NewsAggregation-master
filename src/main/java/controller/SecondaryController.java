package controller;
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
       1. Magnification in JavaFX
       https://stackoverflow.com/questions/29506156/javafx-8-zooming-relative-to-mouse-pointer
       2. CSS
       https://docs.oracle.com/javafx/2/api/javafx/scene/doc-files/cssref.html
       https://www.howkteam.vn/course/lap-trinh-javafx-co-ban/dinh-dang-bang-css-trong-javafx-2648
 */

import controller.article_extraction.ArticleFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import model.Article;

public class SecondaryController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button secondaryButton;

    @FXML
    private Label title;

    private Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    // THis function will listen to the click of the return button and change back to primary view
    @FXML
    private void switchToPrimary() {
        Main.setRoot();
    }

    // This function will set up the content
    public void setupView() {
        //Setup title and the scrollpane as the center of the border pane
        borderPane.setCenter(ArticleFactory.dispArt(ArticleFactory.articleSwitcher(article)));
        title.setFont(new Font(18));
        title.setStyle("-fx-font-weight: bold");
        title.setText(article.getTITLE_PAGE());
    }

    // Make the title zoom in
    @FXML
    private void zoomIn() {
        title.setScaleX(1.2);
        title.setScaleY(1.2);
    }

    // Make the title zoom out
    @FXML
    private void zoomOut() {
        title.setScaleX(1);
        title.setScaleY(1);
    }
}
