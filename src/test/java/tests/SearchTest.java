package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.example.pages.HomePage;
import org.example.pages.SearchResultsPage;

public class SearchTest {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il");
    }

    @Test
    public void testSearch() {
        HomePage homePage = new HomePage(driver);
        String query = "חישוב סכום דמי לידה ליום";
        SearchResultsPage resultsPage = homePage.search(query);
        String actualTitle = resultsPage.getTitleText();
        System.out.println("הכותרת שנמצאה: " + actualTitle);
        Assertions.assertTrue(actualTitle.contains(query),
                "הכותרת לא הכילה את טקסט החיפוש! קיבלנו: " + actualTitle);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}