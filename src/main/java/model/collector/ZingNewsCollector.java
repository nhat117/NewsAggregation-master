/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052, Ho Le Minh Thach s3877980, Truong Nhat Anh s3878231
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
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ZingNewsCollector extends ArticleCollector implements Runnable{

    private final String URL;
    private final List<Article> articleList;

    public ZingNewsCollector(String URL, List<Article> articleList) {
        this.URL = URL;
        this.articleList = articleList;
    }

    @Override
    public void scrapeArticle() {
        try {
            Document doc = Jsoup.connect(URL).get();
            //Get all the article in the element
            Elements elements = doc.getElementsByTag("article");
            //Looping through each element
            for (Element element : elements) {
                // get title
                String title = element.child(1).getElementsByClass("article-title").select("a").text();
                // get link
                String linkPage = WebsiteURL.ZINGNEWS.getUrl() + element.getElementsByClass("article-title").select("a").attr("href");
                // get and format date
                String tempDate = element.getElementsByClass("date").text() + " " + element.getElementsByClass("time").text();
                Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(tempDate);
                // get image url
                String image = getImage(element.getElementsByClass("article-thumbnail").select("img").toString());
                // get article meta category
                String category = element.getElementsByClass("article-meta").select("a").attr("title");

                // eliminate false article
                if (title == null || date == null || image == null) { continue; }

                // construct article
                Article article = new Article(title, linkPage, date, image, getSource("ZingNews"), category);
                // thread-safe handling article
                synchronized(this) {
                    if (ArticleFilter.filterArticle(article)) {
                        articleList.add(article);
                    }
                }
            }
        } catch (IOException | ParseException ignored) {
//            System.out.println("URL error in GetZingNews");
        }
    }


    @Override
    public void run() {
        scrapeArticle();
    }
}
