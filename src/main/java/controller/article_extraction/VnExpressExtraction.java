/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Truong Nhat Anh s3878231
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

import java.util.List;

public class VnExpressExtraction extends ArticleExtractor {
    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Element article;
            //Chekc if the size of the element article
            if (doc.select("article.fck_detail").size() > 0) {
                //Selecting the first element of the article
                article = doc.select("article.fck_detail").first();
            } else {
                article = doc.select("div[class*=fck_detail]").first();
            }
            // Add  element description  to the Article Factory
            ARTICLE_FACTORY.add(new ArticleFactory(doc.select("p.description").text(), "p"));
            // ChEck the div tag of VNEXPRESS
            divChecker(article);
            // Add author label
            String tmp = article.select("p[style*=text-align:right]").text();
            if (tmp.equals("")) {
                tmp = article.select("p[class*=author]").text();
            }
            ARTICLE_FACTORY.add(new ArticleFactory(tmp, "author"));

            return ARTICLE_FACTORY;
        } catch (Exception e) {
            System.out.println("Failed connection to the destination page");
            return null;
        }

    }

    private void divChecker(Element div) {
        for (Element element : div.select("> *")) {
            // If element is text not author
            if (element.tagName().equals("p") && !element.attr("style").contains("text-align:right;") && !element.attr("class").contains("author")) {
                String type = "p";
                if (element.select("strong").size() > 0)
                    type = "h";
                ARTICLE_FACTORY.add(new ArticleFactory(element.text(), type));

            } else if (element.tagName().equals("h2")) {
                ARTICLE_FACTORY.add(new ArticleFactory(element.text(), "h"));
            }
            // If element is image
            else if (element.tagName().equals("figure") && element.select("img").size() > 0) {
                String imgTmp = element.select("img").attr("data-src");
                if (imgTmp.equals("")) {
                    imgTmp = element.select("img").attr("src");
                }
                ARTICLE_FACTORY.add(new ArticleFactory(imgTmp, "img"));
                //Image Caption
                ARTICLE_FACTORY.add(new ArticleFactory(element.select("figcaption").text(), "caption"));
            }
            // If element is gallery
            else if (element.attr("class").contains("clearfix")) {
                if (element.select("img").size() > 0) {
                    String imagTmp = element.select("img").attr("data-src");
                    if (imagTmp.equals("")) {
                        imagTmp = element.select("img").attr("src");
                    }
                    ARTICLE_FACTORY.add(new ArticleFactory(imagTmp, "img"));
                    //Image Caption
                    ARTICLE_FACTORY.add(new ArticleFactory(element.select("p").text(), "caption"));
                }
                //If meeting an inner element
            } else if (element.tagName().equals("div") && element.attr("class").equals("box_brief_info")) {
                for (Element innerEle : element.select("> *")) {
                    if (innerEle.tagName().equals("p")) {
                        ARTICLE_FACTORY.add(new ArticleFactory(innerEle.text(), "p"));
                    }
                }
            } else if (element.tagName().equals("div")) {
                //Initialize checkdiv again
                divChecker(element);
            }
        }
    }
}
