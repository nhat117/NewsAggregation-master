/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Nguyen Dich Long s3879052
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Ho Le Minh Thach s3877980, Truong Nhat Anh s3878231
        Acknowledgement:
        1. CSS selector
        https://www.w3schools.com/cssref/css_selectors.asp
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        2. HTML parser
        https://openplanning.net/10399/jsoup-java-html-parser
        https://nira.com/chrome-developer-tools/#:~:text=From%20the%20Chrome%20menu%3A,web%20page%20you're%20on.

 */
package model.collector;

import model.Article;
import model.ArticleFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NhanDanCollector extends ArticleCollector implements Runnable {

    private final String URL;
    private final List<Article> articleList;

    public NhanDanCollector(String URL, List<Article> articleList) {
        this.URL = URL;
        this.articleList = articleList;
    }

    @Override
    public void scrapeArticle() {
        try {
            Document doc = Jsoup.connect(URL).timeout(10000).get();
            for (Element element : doc.select("article")) { // Fetch all links
                try {
                    // get article url and handle exception
                    String tempLink = element.select("a").attr("href"); // Join links
                    if (!tempLink.contains("https://")) {
                        tempLink = WebsiteURL.NHANDAN.getUrl() + tempLink;
                    }
                    // get article title, if not exists, skip
                    String title = element.getElementsByClass("box-title").text();
                    if (title == null) { continue; }
                    // get article date and img
                    String date = element.select("div[class*=box-meta]").text();
                    Date tempDate = new SimpleDateFormat("HH:mm dd/MM/yyyy").parse(date);
                    String imageURL = element.select("img").attr("data-src");
                    final int CATTOKENA = 3; // define token position of category component in web path
                    String category = title + " " + scrapeCategory(URL, CATTOKENA); // concatenate category + title as category data because cannot reach for category metadata (would result in significant thread runtime
                    if (tempDate == null || imageURL == null) { continue; }
                    Article article = new Article(title, tempLink, tempDate, imageURL, WebsiteURL.NHANDAN, category);
                    synchronized(this) {
                        if (ArticleFilter.filterArticle(article)) {
                            articleList.add(article);
                        }
                    }
                } catch (Exception ignored) {
//                    System.out.println("Cannot parse date in GetNhanDan");
                }
            }
        } catch (Exception ignored) {
//            System.out.println("Failed to connect in GetNhanDan");
        }
    }

    @Override
    public void run() {
        scrapeArticle();
    }

}