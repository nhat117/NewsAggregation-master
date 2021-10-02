package controller.article_extraction;
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
 */

import java.util.ArrayList;
import java.util.List;

public abstract class ArticleExtractor {

    // Workflow
    // Class Content has context and type
    // Creates 5 class to scrap content of each webpage
    // Store the main information of each link into a list called CONTENT
    //
    // Display to the UI:
    // To display, loop through the CONTENT array
    // If type is text then create a new label
    // If type is Image then create a ImageView
    // If type is Video then create a MediaView

    final static List<ArticleFactory> ARTICLE_FACTORY = new ArrayList<>();
    public abstract List<ArticleFactory> getContent(String linkPage);
}
