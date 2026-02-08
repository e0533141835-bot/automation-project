//package tests;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.example.pages.HomePage;
//import org.example.pages.InsuranceCalculatorPage;
//import org.example.pages.InsuranceInfoPage;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Random;
//
//public class CalculatorTest {
//    WebDriver driver;
//
//    @BeforeEach
//    public void setup() {
//        WebDriverManager.chromedriver().setup();
//        driver = new ChromeDriver();
//        driver.manage().window().maximize();
//        driver.get("https://www.btl.gov.il");
//    }
//
//    @Test
//    public void testYeshivaStudent() throws InterruptedException {
//        HomePage homePage = new HomePage(driver);
//        InsuranceInfoPage infoPage = homePage.goToInsuranceInfo();
//        InsuranceCalculatorPage calculator = infoPage.clickToOpenCalculator();
//        String randomDate = generateRandomDate();
//        System.out.println("בודק עבור תאריך לידה: " + randomDate);
//        calculator.fillStep1(randomDate);
//        Thread.sleep(5000);
//        Assertions.assertTrue(calculator.isStep2Displayed(), "לא עברנו לשלב 2 (נכות)!");
//        calculator.fillStep2();
//        Thread.sleep(5000);
//        System.out.println("ביטוח לאומי: " + calculator.getBtlAmount());
//        System.out.println("בריאות: " + calculator.getHealthAmount());
//        System.out.println("סה\"כ: " + calculator.getTotalAmount());
//        Assertions.assertTrue(calculator.getBtlAmount().contains("48"),
//                "שגיאה בדמי ביטוח לאומי. ציפינו ל-48");
//        Assertions.assertTrue(calculator.getHealthAmount().contains("123"),
//                "שגיאה בדמי בריאות. ציפינו ל-123"); // כנ"ל, בתמונה 123
//        Assertions.assertTrue(calculator.getTotalAmount().contains("171"),
//                "שגיאה בסה\"כ. ציפינו ל-171"); // כנ"ל
//
//        Assertions.assertTrue(calculator.getTotalAmount().contains("171"), "שגיאה בסכום");
//
//    }
//
//    private String generateRandomDate() {
//        Random rand = new Random();
//        int age = 19 + rand.nextInt(51);
//        LocalDate dob = LocalDate.now().minusYears(age);
//        return dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }
//
//    @AfterEach
//    public void tearDown() {
//         if (driver != null) driver.quit();
//    }
//}
//

package tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.example.pages.HomePage;
import org.example.pages.InsuranceCalculatorPage;
import org.example.pages.InsuranceInfoPage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

public class CalculatorTest {
    WebDriver driver;

    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            captureScreenshot(context.getDisplayName());
            closeDriver();
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            closeDriver();
        }

        public void testAborted(ExtensionContext context) {
            closeDriver();
        }
    };

    public void captureScreenshot(String testName) {
        if (driver == null) return;
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // יצירת שם קובץ ייחודי לפי זמן
            String fileName = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
            FileUtils.copyFile(screenshot, new File(fileName));
            System.out.println(">>> צילום מסך נשמר ב: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    @BeforeEach
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.btl.gov.il");
    }

    @Test
    public void testYeshivaStudent() throws InterruptedException {
        HomePage homePage = new HomePage(driver);
        InsuranceInfoPage infoPage = homePage.goToInsuranceInfo();
        InsuranceCalculatorPage calculator = infoPage.clickToOpenCalculator();

        String randomDate = generateRandomDate();
        System.out.println("בודק עבור תאריך לידה: " + randomDate);

        calculator.fillStep1(randomDate);
        Thread.sleep(5000); // המתנה שהדף ייטען

        Assertions.assertTrue(calculator.isStep2Displayed(), "לא עברנו לשלב 2 (נכות)!");

        calculator.fillStep2();
        Thread.sleep(5000); // המתנה לתוצאות

        System.out.println("ביטוח לאומי: " + calculator.getBtlAmount());
        System.out.println("בריאות: " + calculator.getHealthAmount());
        System.out.println("סה\"כ: " + calculator.getTotalAmount());

        Assertions.assertTrue(calculator.getBtlAmount().contains("48"),
                "שגיאה בדמי ביטוח לאומי. ציפינו ל-48");
        Assertions.assertTrue(calculator.getHealthAmount().contains("123"),
                "שגיאה בדמי בריאות. ציפינו ל-123");
        Assertions.assertTrue(calculator.getTotalAmount().contains("171"),
                "שגיאה בסה\"כ. ציפינו ל-171");
    }

    private String generateRandomDate() {
        Random rand = new Random();
        int age = 19 + rand.nextInt(51);
        LocalDate dob = LocalDate.now().minusYears(age);
        return dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}