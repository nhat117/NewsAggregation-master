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

public class NhanDanExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document document = Jsoup.connect(linkPage).get();
            String firstImage = document.getElementsByClass("box-detail-thumb uk-text-center").select("img").attr("src");
            ARTICLE_FACTORY.add(new ArticleFactory(firstImage, "img"));

            Elements elements = document.getElementsByClass("detail-content-body").select("*");

            System.out.println(elements.size());

            for (Element element: elements) {
                if (element.tagName().equals("img")) {
                    ARTICLE_FACTORY.add( new ArticleFactory(element.attr("src"),"img"));
                    System.out.println(element.attr("src"));
                } else if (element.tagName().equals("p")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "p"));
                } else if (element.tagName().matches("h\\d")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "h"));
                }
            }
            //Add the author element
            ARTICLE_FACTORY.add(new ArticleFactory(document.select("div.box-author strong").text(),"author"));

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DisplayNhanDan");
        }
        return ARTICLE_FACTORY;
    }
}
