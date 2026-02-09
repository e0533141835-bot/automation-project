//package tests;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.apache.commons.io.FileUtils;
//import org.example.pages.HomePage;
//import org.example.pages.InsuranceCalculatorPage;
//import org.example.pages.InsuranceInfoPage;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.junit.jupiter.api.extension.TestWatcher;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.Optional;
//import java.util.Random;
//
//public class CalculatorTest {
//    WebDriver driver;
//
//    @RegisterExtension
//    TestWatcher watcher = new TestWatcher() {
//        @Override
//        public void testFailed(ExtensionContext context, Throwable cause) {
//            captureScreenshot(context.getDisplayName());
//            closeDriver();
//        }
//
//        @Override
//        public void testSuccessful(ExtensionContext context) {
//            closeDriver();
//        }
//
//        public void testAborted(ExtensionContext context) {
//            closeDriver();
//        }
//    };
//
//    public void captureScreenshot(String testName) {
//        if (driver == null) return;
//        try {
//            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//            // יצירת שם קובץ ייחודי לפי זמן
//            String fileName = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
//            FileUtils.copyFile(screenshot, new File(fileName));
//            System.out.println(">>> צילום מסך נשמר ב: " + fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void closeDriver() {
//        if (driver != null) {
//            driver.quit();
//            driver = null;
//        }
//    }
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
//
//        String randomDate = generateRandomDate();
//        System.out.println("בודק עבור תאריך לידה: " + randomDate);
//
//        calculator.fillStep1(randomDate);
//        Thread.sleep(5000); // המתנה שהדף ייטען
//
//        Assertions.assertTrue(calculator.isStep2Displayed(), "לא עברנו לשלב 2 (נכות)!");
//
//        calculator.fillStep2();
//        Thread.sleep(5000); // המתנה לתוצאות
//
//        System.out.println("ביטוח לאומי: " + calculator.getBtlAmount());
//        System.out.println("בריאות: " + calculator.getHealthAmount());
//        System.out.println("סה\"כ: " + calculator.getTotalAmount());
//
//        Assertions.assertTrue(calculator.getBtlAmount().contains("48"),
//                "שגיאה בדמי ביטוח לאומי. ציפינו ל-48");
//        Assertions.assertTrue(calculator.getHealthAmount().contains("123"),
//                "שגיאה בדמי בריאות. ציפינו ל-123");
//        Assertions.assertTrue(calculator.getTotalAmount().contains("171"),
//                "שגיאה בסה\"כ. ציפינו ל-171");
//    }
//
//    private String generateRandomDate() {
//        Random rand = new Random();
//        int age = 19 + rand.nextInt(51);
//        LocalDate dob = LocalDate.now().minusYears(age);
//        return dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }
//}

package tests;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.example.pages.HomePage;
import org.example.pages.InsuranceCalculatorPage;
import org.example.pages.InsuranceInfoPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CalculatorTest {
    WebDriver driver;

    // --- משתנים עבור הדוח ---
    static ExtentReports extent;
    static ExtentTest test;

    // --- פונקציה שרצה פעם אחת בהתחלה ויוצרת את הקובץ HTML ---
    @BeforeAll
    public static void initReport() {
        // יוצר את המעצב (Spark) ואת הקובץ הפיזי
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("extent-report.html");
        htmlReporter.config().setReportName("דוח בדיקות אוטומטיות - ביטוח לאומי");
        htmlReporter.config().setDocumentTitle("תוצאות בדיקה");

        // מחבר את המעצב למנהל הדוחות
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Tester", "Students Name"); // אפשר לשנות לשם שלך
        extent.setSystemInfo("Environment", "Chrome");
    }

    // --- המנגנון שמזהה נפילה ומצלם ---
    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            // קורא לפונקציית הצילום ומקבל את הנתיב של התמונה
            String screenshotPath = captureScreenshot(context.getDisplayName());

            // מדווח על הכישלון ומוסיף את התמונה לדוח
            test.fail("הטסט נכשל! שגיאה: " + cause.getMessage());
            test.addScreenCaptureFromPath(screenshotPath);

            closeDriver();
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            test.pass("הטסט עבר בהצלחה!");
            closeDriver();
        }

        public void testAborted(ExtensionContext context) {
            closeDriver();
        }
    };

    // פונקציית צילום מסך - כעת מחזירה את נתיב הקובץ (String)
    public String captureScreenshot(String testName) {
        if (driver == null) return null;
        String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(path));
            System.out.println(">>> צילום מסך נשמר ב: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
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

        // יצירת "כתב" חדש בדוח עבור הטסט הנוכחי
        test = extent.createTest("בדיקת מחשבון ביטוח לאומי (תלמיד ישיבה)");
    }

    @Test
    public void testYeshivaStudent() {
        test.info("מתחילים את הבדיקה: ניווט למחשבון");

        HomePage homePage = new HomePage(driver);
        InsuranceInfoPage infoPage = homePage.goToInsuranceInfo();
        InsuranceCalculatorPage calculator = infoPage.clickToOpenCalculator();

        String randomDate = generateRandomDate();
        test.info("בודק עבור תאריך לידה: " + randomDate);
        System.out.println("בודק עבור תאריך לידה: " + randomDate);

        calculator.fillStep1(randomDate);

        // --- תיקון: החלפת Thread.sleep ב-WebDriverWait ---
        test.info("ממתין למעבר לשלב 2...");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // אנחנו מחכים שהכפתור של שלב 2 יופיע, זה הסימן שעברנו שלב
        wait.until(d -> calculator.isStep2Displayed());

        Assertions.assertTrue(calculator.isStep2Displayed(), "לא עברנו לשלב 2 (נכות)!");

        calculator.fillStep2();

        // --- המתנה לתוצאות ---
        test.info("ממתין לתוצאות החישוב...");
        // כאן נחכה שסכום הביטוח הלאומי יופיע (למשל שהטקסט לא יהיה ריק)
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("ul.CalcResult")));

        System.out.println("ביטוח לאומי: " + calculator.getBtlAmount());
        System.out.println("בריאות: " + calculator.getHealthAmount());
        System.out.println("סה\"כ: " + calculator.getTotalAmount());

        test.info("תוצאה שהתקבלה: " + calculator.getTotalAmount());

        Assertions.assertTrue(calculator.getBtlAmount().contains("48"),
                "שגיאה בדמי ביטוח לאומי. ציפינו ל-48");
        Assertions.assertTrue(calculator.getHealthAmount().contains("123"),
                "שגיאה בדמי בריאות. ציפינו ל-123");
        Assertions.assertTrue(calculator.getTotalAmount().contains("171"),
                "שגיאה בסה\"כ. ציפינו ל-171");
    }

    // --- פונקציה שרצה בסוף הכל וסוגרת את הדוח ---
    @AfterAll
    public static void tearDownReport() {
        extent.flush(); // כותב את המידע לקובץ
        System.out.println(">>> הדוח נוצר בהצלחה: extent-report.html");
    }

    private String generateRandomDate() {
        Random rand = new Random();
        int age = 19 + rand.nextInt(51);
        LocalDate dob = LocalDate.now().minusYears(age);
        return dob.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}