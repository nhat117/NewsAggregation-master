/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174
        Acknowledgement:
        https://webfocusinfocenter.informationbuilders.com/wfappent/TLs/TL_srv_dm/source/regex.htm
        https://www.tutorialspoint.com/java/java_files_io.htm
        https://www.javatpoint.com/java-io
 */
package model.collector;

public enum WebsiteURL {
    VNEXPRESS("https://vnexpress.net/"),
    ZINGNEWS("https://zingnews.vn/"),
    TUOITRE("https://tuoitre.vn/"),
    THANHNIEN("https://thanhnien.vn/"),
    NHANDAN("https://nhandan.vn/");

    private final String url;

    WebsiteURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
