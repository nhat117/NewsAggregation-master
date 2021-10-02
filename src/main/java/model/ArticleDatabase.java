/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Truong Nhat Anh s3878231
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052, Bui Minh Nhat s3878174, Ho Le Minh Thach s3877980
        Acknowledgement:
        1. Design pattern in Java Programming
        https://ofstack.com/Java/8204/detail-the-observer-design-pattern-for-java-programming.html
        2. Sort objects by date
        https://stackoverflow.com/questions/5927109/sort-objects-in-arraylist-by-date
        3. Lecture slides from the course INTE2512 on multithreading



 */
package model;

import model.collector.WebsiteURL;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ArticleDatabase implements Runnable {

    private final PropertyChangeSupport propertyChangeSupport;

    private final ScheduledExecutorService executor;

    private final HashSet<String> articlesCheck;

    private final List<Article> scrapeList;

    private final List<Article> articles;

    public ArticleDatabase() {
        executor = Executors.newSingleThreadScheduledExecutor();
        propertyChangeSupport = new PropertyChangeSupport(this);
        articlesCheck = new HashSet<>();
        scrapeList = new ArrayList<>();
        articles = new ArrayList<>();
    }

    @Override
    public void run() {
        System.out.println("Start execution");
        executor.scheduleAtFixedRate(() -> {
            try {
                scrapeList.clear();
                articlesCheck.clear();

                // Perform scraping new articles
                long start = System.currentTimeMillis();
                ExecutorService executorService = Executors.newCachedThreadPool();
                executorService.execute(new URLCrawler(WebsiteURL.VNEXPRESS.getUrl() + "rss", scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.TUOITRE.getUrl(), scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.THANHNIEN.getUrl() + "rss.html", scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.NHANDAN.getUrl(), scrapeList));
                executorService.execute(new URLCrawler(WebsiteURL.ZINGNEWS.getUrl(), scrapeList));
                executorService.shutdown();
                executorService.awaitTermination(15, TimeUnit.SECONDS);

                List<Article> tmpList = new ArrayList<>();
                for (int i = 0; i < scrapeList.size(); i++) {
                    String tmp = scrapeList.get(i).getTITLE_PAGE() + " " + scrapeList.get(i).getSOURCE().getUrl();
                    if (!articlesCheck.contains(tmp)) {
                        articlesCheck.add(tmp);
                        tmpList.add(scrapeList.get(i));
                    }
                }

                articles.clear();
                articles.addAll(tmpList);
                articles.sort(Comparator.comparing(Article::getDuration).reversed());

                long end = System.currentTimeMillis();
                long elapsed = end - start;

                System.out.println("Article size: " + articles.size());
                System.out.println("Scraping took: " + (elapsed / 1000) + " seconds");
                doNotify(true);
            } catch (Exception e) {
                System.out.println("Cannot perform background scraping");
            }
        }, 0, 20_000, TimeUnit.MILLISECONDS);
    }
    public void end() {
        executor.shutdown();
        executor.shutdownNow();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private void doNotify(boolean b) {
        System.out.println("Notify");
        propertyChangeSupport.firePropertyChange("updateScrapeDone", null, b);
    }

    public List<Article> getArticles() {
        return articles;
    }
}
