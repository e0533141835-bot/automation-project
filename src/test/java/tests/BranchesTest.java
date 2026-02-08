package tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.BranchDetailsPage;
import org.example.pages.BranchesPage;
import org.example.pages.HomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BranchesTest {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il");
    }

    @Test
    public void testGoToBranches() {
        HomePage homePage = new HomePage(driver);
        BranchesPage branchesPage = homePage.goToBranches();
        String actualTitle = branchesPage.getTitleText();
        System.out.println("הכותרת בדף היא: " + actualTitle);
        Assertions.assertTrue(actualTitle.contains("סניפים"), "לא הגענו לדף הנכון!");
    }

    @Test
    public void testBranchDetails() {
        HomePage homePage = new HomePage(driver);
        BranchesPage branchesPage = homePage.goToBranches();
        BranchDetailsPage detailsPage = branchesPage.clickOnBranch("בני ברק");
        Assertions.assertTrue(detailsPage.hasAddress(), "לא מצאנו את הכתובת (רחוב)!");
        Assertions.assertTrue(detailsPage.hasReceptionInfo(), "לא מצאנו אזור קבלת קהל (KabalatKahal)!");
        Assertions.assertTrue(detailsPage.hasPhoneInfo(), "לא מצאנו את המילה טלפון!");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}