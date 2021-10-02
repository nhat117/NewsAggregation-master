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

public class ThanhNienExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage) {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements articleBody = doc.select("div[class~=.*content]");
            Elements elements = doc.select("div[id=abody] > *");
            if (articleBody.select("div.sapo").size() > 0) {
                ArticleFactory cont = new ArticleFactory(articleBody.select("div.sapo").text(),"p");
                ARTICLE_FACTORY.add(cont);
            }
            else {
                ArticleFactory cont = new ArticleFactory(doc.select("div.summary").text(),"p");
                ARTICLE_FACTORY.add(cont);
            }
//Scrape the image element
            for (Element element : elements) {
                if (element.tagName().equals("img")) {
                    ArticleFactory tempImg = new ArticleFactory(element.select("div[id=contentAvatar] img").attr("src"),"img");
                    ARTICLE_FACTORY.add(tempImg);
                } else if (element.tagName().equals("p")) {
                    ArticleFactory tempP = new ArticleFactory(element.text(), "p");
                    ARTICLE_FACTORY.add(tempP);
                } else if (element.tagName().matches("h\\d")) {
                    ArticleFactory tempH = new ArticleFactory(element.text(), "h");
                    ARTICLE_FACTORY.add(tempH);
                    System.out.println(tempH.getContext());
                } else  if (element.tagName().equals("div") && !element.className().equals("details__morenews")){
                    //Check the div part of Thanh Nien
                    divChecker(element);
                }
            }

            //Add author
            ArticleFactory cont= new ArticleFactory(doc.select("div.left h4").text(),"author");
            ARTICLE_FACTORY.add(cont);

        } catch (Exception e) {
            System.out.println("Cannot connect to the page from Display Thanh Nien");
        }
        return ARTICLE_FACTORY;
    }
//Check the div element of the Article
    private void divChecker(Element element) {
        // If element has 0 children and is not an ad div
        if (element.select("> *").size() == 0 && !element.className().contains("ads") && element.hasText()){
            ArticleFactory tmpdiv = new ArticleFactory(element.text(),"div");
            ARTICLE_FACTORY.add(tmpdiv);
            return;
        }
        // Loop through div elements
        for (Element ele : element.select("> *")) {
            try {
                if (ele.tagName().contains("div") && !ele.attr("class").contains("image")) {
                    //Access the tag one more time to parese the elements
                    divChecker(ele);
                }
                else if (ele.tagName().equals("p")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(ele.text(),"p"));
                } else if (ele.attr("class").contains("image")) { //Image are being devided and contains in two different class and tags
                    //Extract image and Caption
                    ArticleFactory tmpimg = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                    ArticleFactory labimg = new ArticleFactory(ele.select("p").text(),"caption");
                    ARTICLE_FACTORY.add(tmpimg);
                    ARTICLE_FACTORY.add(labimg);
                } else if (ele.tagName().contains ("figure") && ele.attr("class").equals("picture")) {
                    if (ele.select("img").size() > 0) {
                      ArticleFactory cont = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                      //Search for the caption of the image
                      ArticleFactory lab = new ArticleFactory(ele.select("figcaption").text(),"caption");
                      ARTICLE_FACTORY.add(cont);
                      ARTICLE_FACTORY.add(lab);
                    }
                    else if (ele.hasText()) {
                        ARTICLE_FACTORY.add(new ArticleFactory(ele.text(),"caption"));
                    }
                }
                else if (ele.tagName().matches("h\\d")) {
                    ARTICLE_FACTORY.add(new ArticleFactory(ele.text(), "h"));
                }
                else if (ele.hasText()) { //If the children content still have test
                   ARTICLE_FACTORY.add(new ArticleFactory(element.text(),"p"));
                }
            }
            catch (Exception ignored) {
//                System.out.println("Error display Thanh Nien");
            }
        }
    }

}
