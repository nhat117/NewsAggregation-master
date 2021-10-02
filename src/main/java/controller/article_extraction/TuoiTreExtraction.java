/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052
        Acknowledgement:
        1. CSS selector
        https://www.w3schools.com/cssref/css_selectors.asp
        https://www.youtube.com/watch?v=l1mER1bV0N0&ab_channel=WebDevSimplified
        https://jsoup.org/cookbook/extracting-data/selector-syntax
        2. HTML parser
        https://openplanning.net/10399/jsoup-java-html-parser
        https://nira.com/chrome-developer-tools/#:~:text=From%20the%20Chrome%20menu%3A,web%20page%20you're%20on.
        Thank you Mr Khoi To Hoang S3698211 for guidance.
 */

package controller.article_extraction;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class TuoiTreExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document document = Jsoup.connect(linkPage).get();
            Elements elements = document.select("div#mainContentDetail");
            //Add Description
            ArticleFactory des = new ArticleFactory(elements.select("h2").text(), "p");
            ARTICLE_FACTORY.add(des);
            //Get the main body of the article in the div tag
            divChecker( document.select("div#main-detail-body > *"));
            //Get tauthor
            if (elements.select("div.author").size() > 0) {
                ArticleFactory author = new ArticleFactory(elements.select("div.author").text(), "author");
                ARTICLE_FACTORY.add(author);
            }

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayTuoiTre");
        }
        return ARTICLE_FACTORY;
    }

    private void divChecker(Elements div) {
        for (Element element : div) {
            try {
                if (element.tagName().equals("p") && element.hasText()) { //If element is a ordinary p element
                    ArticleFactory tmp = new ArticleFactory(element.text(), "p");
                    ARTICLE_FACTORY.add(tmp);
                } else if (element.tagName().equals("div")) { //If element is a p tag
                    // Add image if element is image
                    if (element.attr("type").equals("Photo")) {
                        String imgSrc = element.select("img").attr("src");
                        try {
                            //Clean up image url
                            imgSrc = imgSrc.replace("thumb_w/586/", "");
                        } catch (Exception ignored) {}
                        ArticleFactory img = new ArticleFactory(imgSrc, "img");
                        ARTICLE_FACTORY.add(img);
                        //Add caption of the image
                        ArticleFactory cap = new ArticleFactory(element.select("p").text(), "caption");
                        ARTICLE_FACTORY.add(cap);
                    } else if (element.attr("type").equals("wrapnote")) { //Check the wrapnote Element
                        divChecker(element.select("> *"));
                    }
                }
            } catch (Exception e) {
                System.out.println("Error display tuoitre");
                continue;
            }
        }
    }
}
