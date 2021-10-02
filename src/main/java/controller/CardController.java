/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Acknowledgement:
        Thank you, Professor Nick Wergeles for explaining the concept of Javafx RunLater
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */

package controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import model.Article;
import model.collector.WebsiteURL;

public class CardController {
    @FXML
    private Text source;

    @FXML
    private Text title;

    @FXML
    private Text time;

    @FXML
    private ImageView imageView;

    public String websiteLink;

    public WebsiteURL websiteSource;

    private Article cardArticle;

    // This function will change the content of the cards including time, resource, title, and image
    public void setData(Article article) {
        this.cardArticle = article;
        // Get the title
        String titleStr = article.getTITLE_PAGE();
        String timeStr = null;
        // Get the link
        websiteLink = article.getLINK_PAGE();
        // Get the source
        websiteSource = article.getSOURCE();
        // If time is not null then get the duration
        if (time != null) {
            // If the duration is not null then get the time duration in string
            if (article.getDuration() != null) {
                // Get the duration in string by using a static method getFriendlyDate()
                timeStr = Article.getFriendlyDate(article.getDuration());
            } else {
                // If no time then return string no time (rare case)
                // because the filter already remove articles without date
                timeStr = "No Time";
            }
        }
        Image imageURL = null;
        // If the imageview is not null then get the image url
        if (imageView != null) {
            // If the image url is not null and the url is also not empty
            if (article.getIMAGE_URL() != null && !article.getIMAGE_URL().isEmpty()) {
                // Assign the image into the imageURL
                imageURL = new Image(article.getIMAGE_URL());
            } else {
                // Else give an empty image
                imageURL = null;
            }
        }
        String sourceName = "";
        // Indicate the source by switch case
        switch (cardArticle.getSOURCE()) {
            case VNEXPRESS:
                sourceName = "VNExpress";
                break;
            case NHANDAN:
                sourceName = "NhanDan";
                break;
            case TUOITRE:
                sourceName = "TuoiTre";
                break;
            case ZINGNEWS:
                sourceName = "ZingNews";
                break;
            case THANHNIEN:
                sourceName = "ThanhNien";
                break;
        }
        // Get the final variable to run in the thread platform runlater of Javafx
        String finalSourceName = sourceName;
        String finalTimeStr = timeStr;
        Image finalImageURL = imageURL;
        Platform.runLater(() -> {
            // Set up all the title, source, time, and image
            title.setText(titleStr);
            source.setText(finalSourceName);
            time.setText(finalTimeStr);
            imageView.setImage(finalImageURL);
        });
    }

    // Function direct the primarypage to switch to secondary page and show the content
    @FXML
    private void clickCard() {
        Main.setRoot(cardArticle);
    }
}
