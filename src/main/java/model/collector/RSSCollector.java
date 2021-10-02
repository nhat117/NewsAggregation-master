/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Nguyen Dich Long s3879052
        Acknowledgement:
        1. Reading XML file
        https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
        2. Synchronized collection
        https://www.baeldung.com/java-synchronized
 */
package model.collector;

import com.apptastic.rssreader.Item;
import com.apptastic.rssreader.RssReader;
import com.github.sisyphsu.dateparser.DateParserUtils;
import model.Article;
import model.ArticleFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RSSCollector extends ArticleCollector implements Runnable {

    private final String URL;
    private final List<Article> articles;

    // Get the articles clone of database and the url to access
    public RSSCollector(String url, List<Article> articles) {
        this.articles = articles;
        this.URL = url;
    }
    @Override
    public void scrapeArticle() {
        try {
            // Use the RssReader library to scrape articles
            RssReader reader = new RssReader();
            Stream<Item> rssFeed = reader.read(URL);
            List<Item> itemList = rssFeed.collect(Collectors.toList());
            for (Item item : itemList) {
                // Get article title, article url, date, image url and category
                String title = item.getTitle().isPresent() ? item.getTitle().get() : null;
                String link = item.getLink().isPresent() ? item.getLink().get() : null;
                String pubDate = item.getPubDate().isPresent() ? item.getPubDate().get() : null;
                String image = item.getDescription().isPresent() ? item.getDescription().get() : null;
                String source = item.getChannel().getTitle().isBlank() ? null : item.getChannel().getTitle();
                final int CATTOKENA = 3, CATTOKENB = 4; // define token position of category component in web path
                String category = title + " " + scrapeCategory(URL, CATTOKENA) + " " + scrapeCategory(URL, CATTOKENB); // concatenate title and category token
                if (title == null || link == null || pubDate == null || image == null || source == null) { continue; }
                Article article = new Article(title, link, DateParserUtils.parseDate(pubDate), ArticleCollector.getImage(image), getSource(source), category);
                // Stop all thread to write the array
                synchronized(this) { // handle selected articles
                    if (ArticleFilter.filterArticle(article)) {
                        articles.add(article);
                    }
                }
            }
        } catch (MalformedURLException ignored) {
//            System.out.println("Url Error in GetWithRSS");
        } catch (IOException ignored) {
//            System.out.println("XML parser error in GetWithRSS");
        }
    }

    @Override
    public void run() {
        scrapeArticle();
    }

}