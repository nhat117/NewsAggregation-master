/*
        RMIT University Vietnam
        Course: INTE2512 Object-Oriented Programming
        Semester: 2021B
        Assessment: Final Project
        Created  date: 07/08/2021
        Author: Ho Le Minh Thach s3877980
        Last modified date: 10/09/2021
        Contributor: Bui Minh Nhat s3878174, Truong Nhat Anh s3878231
        Acknowledgement:
        1. MVC observable pattern
        https://www.baeldung.com/java-observer-pattern
        2. Thank you, Professor Nick Wergeles for explaining the concept of:
            - JavaThread Service and Task
            - MVC model
        https://youtube.com/playlist?list=PLpvL1C_oZsr-BMBvdtgipMCDZK14BNigd
 */
package controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import model.Article;
import model.ArticleDatabase;
import model.ConnectionTest;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class PrimaryController implements Initializable, PropertyChangeListener {
    // List of 5 pages
    private List<ArticlePageView> pageList;

    // Save the instance of navigationController
    @FXML
    private NavigationController navigationController;

    // Save the instance of categoryController
    @FXML
    private CategoryController categoryController;

    // Save the instance of sidebarController
    @FXML
    private SidebarController sidebarController;

    //Progress bar
    @FXML
    private ProgressBar progressBar;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Label statusLabel;

    @FXML
    private Circle connectionCircle;

    // Instance of the database
    private ArticleDatabase articleDatabase;

    private int currentPage;

    private int currentCategory;

    private final boolean[] HAVE_CLICK = new boolean[5];

    private ConnectionTest connectionTest;

    private PropertyChangeSupport propertyChangeSupport;

    // Service to do the thread job of loading article on UI and update the progressbar
    private Service<Integer> service;

    // Check if the page has clicked or not
    private void resetHaveClick() {
        Arrays.fill(HAVE_CLICK, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Init the connection status
        connectionCircle.setFill(Color.rgb(0, 194, 66));

        // Make this instance to be observable
        propertyChangeSupport = new PropertyChangeSupport(this);

        // Run the connection test
        connectionTest = new ConnectionTest();
        // Add primary controller as observer to the connection testing
        connectionTest.addPropertyChangeListener(this);
        // Run the connection testing in thread
        Thread backgroundConnectionTest = new Thread(connectionTest);
        backgroundConnectionTest.setDaemon(true);
        backgroundConnectionTest.start();

        // Setup the article database
        articleDatabase = new ArticleDatabase();
        // Make primary controller as observer to the database
        articleDatabase.addPropertyChangeListener(this);
        // Run the database scraping in thread
        Thread scrapingThread = new Thread(articleDatabase);
        scrapingThread.setDaemon(true);
        scrapingThread.start();

        // Init the service for later use when update the heading content and keeping track of progressbar
        service = new Service<>() {
            @Override
            protected Task<Integer> createTask() {
                return new Task<>() {
                    @Override
                    protected Integer call() {
                        // Check if the button has click or not
                        if (!HAVE_CLICK[currentPage]) {
                            int i = 0;
                            // If not then iterate through the database to get the article
                            for (Article article : articleDatabase.getArticles()) {
                                // If the number of article is greater than 10 for a page then break
                                if (i > (currentPage + 1) * 10 - 1) {
                                    break;
                                }
                                // If the article is match the current category of the page
                                if (article.getCATEGORIES().contains(currentCategory)) {
                                    // Check if the number of article is meet the minimum required
                                    // for instance page 2 will need to get the 10th position, page 1 will start form 0
                                    if (i >= currentPage * 10) {
                                        // Get the card controller to modify base on the article
                                        CardController cardController = pageList.get(currentPage).FXML_LOADER_LIST.get(i % 10).getController();
                                        // Modify the card base on information of the article
                                        cardController.setData(article);
                                        // Update the progressbar
                                        updateProgress((i % 10) + 1, 10);
                                    }
                                    // Update the article in that category
                                    i++;
                                }
                            }
                            // Set the page to have clicked
                            HAVE_CLICK[currentPage] = true;
                        }
                        // Return 1 if successful
                        return 1;
                    }
                };
            }
        };
        // Bind the progressbar to the service so that whenever service update the progressbar update
        progressBar.progressProperty().bind(service.progressProperty());
        // Add this instance to the navigation controller's observer
        navigationController.addPropertyChangeListener(this);
        // Inject this instance for navigation controller needed
        navigationController.injectController(this);
        // Add this instance to the category controller's observer
        categoryController.addPropertyChangeListener(this);
        // Inject this instance for category controller needed
        categoryController.injectController(this);
        // reset the have click button to set it all false
        resetHaveClick();

        // Create 5 pages
        pageList = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            // Create and store them in the pageList
            ArticlePageView articlePageView = new ArticlePageView(i);
            pageList.add(articlePageView);
        }
    }

    // This function will start to insert article content to the card
    private void inputArticle(int scrollValue) {
        Platform.runLater(() -> {
            // Get the stored page from the list coresponding to the current page value
            ScrollPane scrollPane = pageList.get(currentPage);
            // If the scrollValue is 0 mean that reset the scroll position to be the top of the page
            if (scrollValue == 0) {
                scrollPane.setVvalue(scrollValue);
            }
            // set the scrollpage into the center of the border pane
            borderPane.setCenter(scrollPane);
            // run the service by restart it (stop the previous thread and ready to run again)
            service.restart();
        });
    }

    // Get the sidebar controller
    SidebarController getSidebarController() {
        return sidebarController;
    }

    // Make the stage listen for closing the app to shut down the thread that still running
    public void ready(Stage stage) {
        stage.setOnCloseRequest(event -> {
            System.out.println("Stage will close");
            connectionTest.end();
            articleDatabase.end();
            service.cancel();
        });
    }

    // Listen for to notify from the observable instance
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // If the notification is from the article database then start update the new content into the cards
        if (evt.getPropertyName().equals("updateScrapeDone") && (boolean) evt.getNewValue()) {
            System.out.println("Update scrapping");
            resetHaveClick();
            inputArticle(1);
        }
        // If the notification is from the navigation controller then set the page corresponding to the new value
        if (evt.getPropertyName().equals("currentPage")) {
            currentPage = (int) evt.getNewValue();
            inputArticle(0);
        }

        // If the notification is from the category controller then set the card's content corresponding to the new value
        if (evt.getPropertyName().equals("currentCategory")) {
            currentCategory = (int) evt.getNewValue();
            currentPage = 0;
            resetHaveClick();
            // notify the navigation controller to update the page to page 1
            propertyChangeSupport.firePropertyChange("currentPage", null, currentPage);
            inputArticle(currentPage);
        }

        // If the notification is from the connection testing then change the value of the status
        if (evt.getPropertyName().equals("Bad internet connection")) {
            // If the value is true then change the color to red
            if ((boolean) evt.getNewValue()) {
                System.out.println("Bad internet connection");
                Platform.runLater(() -> connectionCircle.setFill(Color.rgb(255,81,82)));
            } else if (connectionCircle.getFill().equals(Color.rgb(255, 81, 82)) && !((boolean) evt.getNewValue())) {
                // If the value is not false and the current color of the status is red then change back to green
                Platform.runLater(() -> connectionCircle.setFill(Color.rgb(0, 194, 66)));
                inputArticle(0);
            }
        }
    }

    // Add the observer
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    // Remove the observer not use yet but maybe for further development usage
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
}
