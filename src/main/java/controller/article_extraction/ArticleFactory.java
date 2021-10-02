/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor : Ho Le Minh Thach s3877980, Truong Nhat Anh s3878231
        Acknowledgement:
        1. CSS selector
        https://www.w3schools.com/cssref/css_selectors.asp
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        2. HTML parser
        https://openplanning.net/10399/jsoup-java-html-parser
        3. Font formatting
        https://docs.microsoft.com/en-us/windows/apps/design/signature-experiences/typography
        https://docs.oracle.com/javase/8/javafx/api/javafx/scene/text/Font.html
 */

package controller.article_extraction;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import model.Article;

import java.util.List;

public class ArticleFactory {
    private final String context;
    private final String type;

    public ArticleFactory(String context, String type) {
        this.context = context;
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    //Extract the article in the content list
    public static List<ArticleFactory> articleSwitcher(Article article) {
        ArticleExtractor display;
        switch (article.getSOURCE()) {
            case VNEXPRESS:
                display = new VnExpressExtraction();
                break;
            case TUOITRE:
                display = new TuoiTreExtraction();
                break;
            case ZINGNEWS:
                display = new ZingNewsExtraction();
                break;
            case NHANDAN:
                display = new NhanDanExtraction();
                break;
            case THANHNIEN:
                display = new ThanhNienExtraction();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + article.getSOURCE());
        }
//        System.out.println(source.getUrl());
        return display.getContent(article.getLINK_PAGE());
    }

    //Add everything to vbox
    public static ScrollPane dispArt(List<ArticleFactory> articleFactoryList) {
        final double PAD = 100;
        System.out.println("Working");
        ScrollPane scroll = new ScrollPane();
        // Apply the styling for the scrollpane
        // the function need to have '/' and it is not related to the OS
        // If using '\\' then it cannot find the style on window because it reads URL not file path
        scroll.getStylesheets().add("style/style.css");
        scroll.getStyleClass().add("edge-to-edge");
        scroll.setPrefHeight(Region.USE_COMPUTED_SIZE);
        scroll.setPrefWidth(Region.USE_COMPUTED_SIZE);
        scroll.setFitToWidth(true);
        VBox articleVbox = new VBox();
        // Apply the styling for the article box
        // the function need to have '/' and it is not related to the OS
        // If using '\\' then it cannot find the style on window because it reads URL not file path
        articleVbox.getStylesheets().add("style/style.css");
        articleVbox.getStyleClass().add("dark_background");
        articleVbox.setAlignment(Pos.CENTER);
        articleVbox.setSpacing(20);
        articleVbox.setPadding(new Insets(10, 10, 10, 10));
        for (ArticleFactory cnt : articleFactoryList) {
            if (cnt.getType().equals("p")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 20pt");
                articleVbox.getChildren().add(text);
                // Listen for changes in width when user resize
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            } else if (cnt.getType().equals("caption")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 18pt; -fx-font-style: italic;");
                // Listen for changes in width when user resize
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
               StackPane stackPane = new StackPane();
               stackPane.getChildren().add(text);
               stackPane.setPadding(new Insets(10, 0, 10, 0));
               articleVbox.getChildren().add(stackPane);
            } else if (cnt.getType().equals("img")) {
                try {
                    ImageView imageView = new ImageView();
                    imageView.setSmooth(true);
                    imageView.setPreserveRatio(true);
                    // Listen for changes in width when user resize
                    imageView.fitWidthProperty().bind(scroll.widthProperty().subtract(PAD));
                    imageView.setImage(new Image(cnt.getContext()));
                    imageView.setStyle("-fx-padding: 10px");
                    StackPane stackPane = new StackPane();
                    stackPane.getChildren().add(imageView);
                    stackPane.setPadding(new Insets(20, 0,10,0));
                    articleVbox.getChildren().add(stackPane);
                } catch (Exception e) {
                    System.out.println("Failed to load img");
                }
            }else if (cnt.getType().equals("author")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 17pt; -fx-font-weight: bold;");
                articleVbox.getChildren().add(text);
                // Listen for changes in width when user resize
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            } else if (cnt.getType().equals("h")) {
                Text text = new Text(cnt.getContext());
                text.setStyle("-fx-font-size: 18pt; -fx-font-weight: bold;");
                articleVbox.getChildren().add(text);
                // Listen for changes in width when user resize
                text.wrappingWidthProperty().bind(scroll.widthProperty().subtract(PAD));
            }
        }
        scroll.setContent(articleVbox);
        return scroll;
    }
}
