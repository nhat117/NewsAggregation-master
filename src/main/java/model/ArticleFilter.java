/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Bui Minh Nhat s3878174
        Last modified date: 10/09/2021
        Contributor: Nguyen Dich Long s3879052, Ho Le Minh Thach s3877980
        Acknowledgement:
        1. Java Regex
        https://webfocusinfocenter.informationbuilders.com/wfappent/TLs/TL_srv_dm/source/regex.htm
        2, Java file IO
        https://www.tutorialspoint.com/java/java_files_io.htm
        https://www.javatpoint.com/java-io
 */

package model;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;


public class ArticleFilter {
    //Load prdefined world from file -> Store on file for future upgradability,
    public static String[] loadDictionary(String dictionaryFile) {
        String[] dictionary;
        try {
            // Handle the OS file path
            String fileSeparator = FileSystems.getDefault().getSeparator();
            if (fileSeparator.equals("\\")) {
                fileSeparator = "\\\\";
            }
            // dictionary input
            File input = new File(("src/main/java/model/dictionary/" + dictionaryFile).replaceAll("/", fileSeparator));
            String delim = ", ";
            String src = Files.readString(Path.of(input.getPath()));
            //Need to split the string contain delim
            dictionary = src.split(delim);
            return dictionary;
        } catch (Exception e) {
            System.out.println("Error with " + dictionaryFile);
            return null;
        }
    }

    public static boolean isMatch(String rawCategory, String dictionaryFile) {
        String[] dictionary = loadDictionary(dictionaryFile);
        //  Check if the dictionary is false
        if (dictionary == null) {
            return false;
        }
        for (int i = 0; i < dictionary.length; i++) {
            if (isWordMatches(rawCategory, dictionary[i])) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWordMatches(String rawCategory, String words) { // check if articles's raw category data matches this database
        //Don't need t worry about the case of the file
        Pattern key = Pattern.compile(words, Pattern.CASE_INSENSITIVE);
        //We only need to match 1 time
        String tmp = rawCategory.toLowerCase();
        if (key.matcher(tmp).find()) {
            return true;
        }
        //Don't find any matches
        return false;
    }

    //Use this to filter the article
    synchronized public static boolean filterArticle(Article article) {
        String[] category = {"Covid", "Politics", "Business", "Technology", "Health", "Sport", "Entertainment", "World"};

        boolean hasCategory = false;
        article.addCategory(0); // add to category "latest" with index 0
        for (int i = 0; i < category.length; i++) {
            // Avoid video article
            if(isWordMatches(article.getLINK_PAGE(),"video")) {
                return false;
            }
            // loop through all dictionaries, check if articles match any
            if (isMatch(article.getCATEGORY(), (category[i] + ".txt"))) {
                article.addCategory(i + 1); // if yes then update category list + update counter
                hasCategory = true;
            }
        }
        if (!hasCategory) {
            final int others = 8;
            article.addCategory(others + 1); // update category list and counter
        }
        return article.getCATEGORIES().size() > 1; // return whether article belongs to any category
    }

    public static boolean filterArticle(String folderUrl) {
        // return true if the directory is in the acceptable range (limit the wasteful directory that is not related to any category)
        return isMatch(folderUrl, "NavigationFolder.txt")
                && !folderUrl.contains("video")  && !folderUrl.contains("game") && !folderUrl.contains("viec-lam");
    }

}
