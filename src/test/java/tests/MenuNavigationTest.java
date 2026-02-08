package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.pages.BtlMenu;
import org.example.pages.CategoryPage;
import org.example.pages.HomePage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MenuNavigationTest {
    WebDriver driver;

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il");

    }

    @ParameterizedTest(name = "בדיקת ניווט לדף: {0}")
    @ValueSource(strings = {
            "אבטלה",
            "ניידות", // תיקון 1: שינינו לשם המדויק שמופיע בתפריט
            "נכות כללית",
            "מילואים",
            "אזרח ותיק"
    })
    public void testMenuNavigation(String categoryName) {

        System.out.println("מתחיל בדיקה עבור הקטגוריה: " + categoryName);

        HomePage homePage = new HomePage(driver);
        homePage.clickMainMenu(BtlMenu.BENEFITS);
        homePage.clickSubMenu(categoryName);

        CategoryPage categoryPage = new CategoryPage(driver);
        String actualBreadcrumb = categoryPage.getActiveBreadcrumbText();

        System.out.println("פירור הלחם שנמצא בדף: " + actualBreadcrumb);

        Assertions.assertTrue(actualBreadcrumb.contains(categoryName),
                "הגענו לדף הלא נכון! ציפינו שהפירור יכיל את '" + categoryName + "' אבל קיבלנו: " + actualBreadcrumb);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}