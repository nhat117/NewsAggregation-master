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
        1. Java OOP fundamentals
        https://www.w3schools.com/java/java_oop.asp
 */
package model;


import model.collector.WebsiteURL;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Article {
    // article attributes
    private final String TITLE_PAGE;
    private final String LINK_PAGE;
    private final Date DATE;
    private final String IMAGE_URL;
    private final WebsiteURL SOURCE;
    private final String CATEGORY; // Raw category data to filter
    private final List<Integer> CATEGORIES; // store category indices
    //    Category indices are as follows
    //    1 Covid
    //    2 Politics
    //    3 Business
    //    4 Technology
    //    5 Health
    //    6 Sport
    //    7 Entertainment
    //    8 World
    //    9 Others

    // Constructor
    public Article(String TITLE_PAGE, String LINK_PAGE, Date DATE, String IMAGE_URL, WebsiteURL SOURCE, String CATEGORY) {
        this.TITLE_PAGE = TITLE_PAGE;
        this.LINK_PAGE = LINK_PAGE;
        this.DATE = DATE;
        this.IMAGE_URL = IMAGE_URL;
        this.SOURCE = SOURCE;
        this.CATEGORY = CATEGORY;
        CATEGORIES = new ArrayList<>();
    }

    // add a category to the category list
    public void addCategory(int category) { CATEGORIES.add(category); }

    // getters
    public String getTITLE_PAGE() {
        return TITLE_PAGE;
    }

    public String getLINK_PAGE() {
        return LINK_PAGE;
    }

    public Date getDuration() {
        return DATE;
    }

    public String getIMAGE_URL() {
        return IMAGE_URL;
    }

    public WebsiteURL getSOURCE() {
        return SOURCE;
    }

    public String getCATEGORY() {
        return CATEGORY;
    }

    public List<Integer> getCATEGORIES() {
        return CATEGORIES;
    }

    // method to format date
    public static String getFriendlyDate(Date date) {
        // Turn normal date format to friendly date
        Date now = new Date();
        Duration duration = null;

        // Handle Duration.between order works differently on Linux
        String tempOS = System.getProperty("os.name").toLowerCase();
        if (tempOS.contains("nix") || tempOS.contains("nux") || tempOS.contains("aix")) {
            duration = Duration.between(now.toInstant(), date.toInstant());
        } else {
            duration = Duration.between(date.toInstant(), now.toInstant());
        }
        if (duration == null) return null;

        // get time quantities in units of convention (day, hour, minute)
        long minute = duration.toMinutes();
        long hour = minute / 60;
        long day = hour / 24;
        long remaining_hour = hour % 24;
        long remaining_minute = minute % 60;
        String format = "";

        if (day == 1) {
            format += day + " day ";
        } else if (day > 1) {
            format += day + " days ";
        }
        if (remaining_hour == 1) {
            format += remaining_hour + " hour ";
        } else if (hour > 1) {
            format += remaining_hour + " hours ";
        }
        if (remaining_minute > 1) {
            format += remaining_minute + " minutes";
        } else {
            format += remaining_minute + " minute";
        }
        return format + " ago";
    }
}
