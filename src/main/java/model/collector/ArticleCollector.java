/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat_s3878174, Nguyen Dich Long s3879052
        Acknowledgement:
https://stackoverflow.com/questions/3809401/what-is-a-good-regular-expression-to-match-a-url
https://jsoup.org/cookbook/extracting-data/dom-navigation
 */
package model.collector;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.regex.Pattern;

public abstract class ArticleCollector {

    // Get the sources base on the string scraped
    protected static WebsiteURL getSource(String source) {
        if (source.contains("VnExpress")) {
            return WebsiteURL.VNEXPRESS;
        } else if (source.contains("Tuổi Trẻ Online")) {
            return WebsiteURL.TUOITRE;
        } else if (source.contains("Thanh Niên")) {
            return WebsiteURL.THANHNIEN;
        } else if (source.contains("ZingNews")) {
            return WebsiteURL.ZINGNEWS;
        } else if (source.contains("nhandan")) {
            return WebsiteURL.NHANDAN;
        }
        return null;
    }

    // method to scrape category data from the url
    public static String scrapeCategory(String url, int token) {
        String regex = "/"; // set the delimiter
        Pattern pattern = Pattern.compile(regex); // detect delimiter
        String[] result = pattern.split(url); // collect tokens
        return result[token].equals("rss") ? "" : result[token]; // return a particular token if it is not rss
    }
    // Tools for getArticle
    // get image link
    protected static String getImage(String description) {
        //Create a storage document
        Document document = Jsoup.parse(description);
        Elements images = document.select("img");
        // Cleanup image url
        String urlForm = "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})";
        for (Element image : images) {
            if (image.hasAttr("src") && !image.attr("src").isBlank() && image.attr("src").matches(urlForm)) {
                return image.attr("src");
            } else if(image.hasAttr("data-src")) {
                return image.attr("data-src");
            }
        }
        return null;
    }

    public abstract void scrapeArticle();

}
