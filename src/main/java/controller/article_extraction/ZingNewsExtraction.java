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

public class ZingNewsExtraction extends ArticleExtractor {

    @Override
    public List<ArticleFactory> getContent(String linkPage)  {
        try {
            ARTICLE_FACTORY.clear();
            Document doc = Jsoup.connect(linkPage).get();
            Elements elements = doc.select("div.the-article-body > *");

            //Extract the summary
            ArticleFactory tmp = new ArticleFactory(doc.select("p.the-article-summary").text(), "h");
            ARTICLE_FACTORY.add(tmp);
            divChecker(elements);
            //Get author info
            ArticleFactory author = new ArticleFactory(doc.getElementsByClass("author").text(), "author");
            ARTICLE_FACTORY.add(author);
        } catch (Exception e) {
            System.out.println("Cannot connect to the page from DtagName().equalsplayZingNews");
        }
        return ARTICLE_FACTORY;
    }
//Scrape content of ZingNews
    private void divChecker(Elements div) {
        for (Element ele : div) {
            try {
                if (ele.tagName().equals("p")) { //Check if element not author
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "p");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.tagName().equals("div") && ele.attr("class").equals("notebox ncenter")) {
                    divChecker(ele.select("> *"));
                } else if (ele.tagName().equals("h3")) { //Check header
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "h");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.tagName().equals("table") && ele.attr("class").contains("picture")) { //For full picture post
                    for (Element inner : ele.select("td.pic > *")) {
                        //Extract the URL
                        String imageURL = inner.select("img").attr("data-src");
                        if (imageURL.equals("")) imageURL = inner.select("img").attr("src");
                        ArticleFactory img = new ArticleFactory(imageURL, "img");
                        ARTICLE_FACTORY.add(img);
                        //Image caption
                        ArticleFactory tmp = new ArticleFactory(ele.select("td[class=\"pCaption caption\"]").text(), "caption");
                        ARTICLE_FACTORY.add(tmp);
                    }
                } else if (ele.tagName().equals("h1") && ele.select("img").size() > 0) {
                    ArticleFactory tmp = new ArticleFactory(ele.select("img").attr("data-src"), "img");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.tagName().equals("div") && ele.attr("class").contains("widget")) { //Check for the COVID Widget
                    ArticleFactory tmp = new ArticleFactory(ele.attr("data-src"), "img");
                    ARTICLE_FACTORY.add(tmp);
                } else if (ele.tagName().equals("ul") || ele.tagName().equals("div")) { //If see a block of tag
                    divChecker(ele.select("> *"));
                } else if (ele.hasText() && ele.tagName().equals("li")) {
                    ArticleFactory tmp = new ArticleFactory(ele.text(), "p");
                } else if (ele.tagName().equals("blockquote")) {
                    divChecker(ele.select("> *"));
                }
            } catch (Exception e) {
//                System.out.println("Error Display Zingnews the article");
            }
        }
    }

}
