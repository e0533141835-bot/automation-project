//package tests;
//
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.apache.commons.io.FileUtils; // וודא שיש לך את הספרייה הזו
//import org.example.pages.BtlMenu;
//import org.example.pages.HomePage;
//import org.example.pages.UnemploymentCalculatorPage;
//import org.example.pages.UnemploymentInfoPage;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtensionContext;
//import org.junit.jupiter.api.extension.RegisterExtension;
//import org.junit.jupiter.api.extension.TestWatcher;
//import org.openqa.selenium.By;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.io.File;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//public class UnemploymentTest {
//    WebDriver driver;
//
//    // --- 1. המנגנון שמזהה נפילה ומצלם (בדיוק כמו בקובץ הקודם) ---
//    @RegisterExtension
//    TestWatcher watcher = new TestWatcher() {
//        @Override
//        public void testFailed(ExtensionContext context, Throwable cause) {
//            captureScreenshot(context.getDisplayName());
//        }
//    };
//
//    // --- 2. פונקציית העזר שמצלמת בפועל ---
//    public void captureScreenshot(String testName) {
//        if (driver == null) return;
//
//        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//        // יצירת שם קובץ ייחודי עם זמן
//        String fileName = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
//
//        try {
//            FileUtils.copyFile(screenshot, new File(fileName));
//            System.out.println(">>> צילום מסך נשמר בגלל כישלון: " + fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
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
//    public void testUnemploymentCalculator() {
//        HomePage homePage = new HomePage(driver);
//
//        homePage.clickMainMenu(BtlMenu.BENEFITS);
//        homePage.clickSubMenu("אבטלה");
//        UnemploymentInfoPage infoPage = new UnemploymentInfoPage(driver);
//        UnemploymentCalculatorPage calculator = infoPage.navigateToCalculator();
//
//        String recentDate = getRecentDate();
//        System.out.println("תאריך הפסקת עבודה: " + recentDate);
//        calculator.fillStep1(recentDate);
//
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id$='_IncomeGrid_ctl02_Txt_Sallary']")));
//
//        calculator.fillStep2("12000");
//
//        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'שכר יומי ממוצע')]")));
//        System.out.println("מוודא שדף התוצאות עלה עם כל הנתונים...");
//
//        // אם אחת השורות האלו תיכשל - המנגנון יצלם מסך אוטומטית!
//        Assertions.assertTrue(calculator.isAvgDailyWageDisplayed(), "חסר: שכר יומי ממוצע");
//        Assertions.assertTrue(calculator.isDailyBenefitDisplayed(), "חסר: דמי אבטלה ליום");
//        Assertions.assertTrue(calculator.isMonthlyBenefitDisplayed(), "חסר: דמי אבטלה לחודש");
//
//        System.out.println("כל הנתונים הופיעו בהצלחה!");
//    }
//
//    private String getRecentDate() {
//        LocalDate date = LocalDate.now().minusDays(21);
//        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//}

package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.example.pages.BtlMenu;
import org.example.pages.HomePage;
import org.example.pages.UnemploymentCalculatorPage;
import org.example.pages.UnemploymentInfoPage;
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

public class UnemploymentTest {
    WebDriver driver;

    // --- משתנים עבור הדוח ---
    static ExtentReports extent;
    static ExtentTest test;

    // --- יצירת דוח נפרד לאבטלה ---
    @BeforeAll
    public static void initReport() {
        ExtentSparkReporter htmlReporter = new ExtentSparkReporter("unemployment-report.html");
        htmlReporter.config().setReportName("דוח בדיקות - מחשבון אבטלה");
        htmlReporter.config().setDocumentTitle("תוצאות אבטלה");

        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("Tester", "Student Name");
    }

    // --- המנגנון שמצלם ומדווח ---
    @RegisterExtension
    TestWatcher watcher = new TestWatcher() {
        @Override
        public void testFailed(ExtensionContext context, Throwable cause) {
            String screenshotPath = captureScreenshot(context.getDisplayName());

            // דיווח על כישלון + תמונה
            test.fail("הטסט נכשל! שגיאה: " + cause.getMessage());
            test.addScreenCaptureFromPath(screenshotPath);

            closeDriver(); // סגירה אחרי כישלון
        }

        @Override
        public void testSuccessful(ExtensionContext context) {
            test.pass("הטסט עבר בהצלחה!");
            closeDriver(); // סגירה אחרי הצלחה
        }
    };

    public String captureScreenshot(String testName) {
        if (driver == null) return null;
        String path = System.getProperty("user.dir") + "/screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
        try {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(path));
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

        // יצירת הטסט בדוח
        test = extent.createTest("בדיקת מחשבון אבטלה");
    }

    @Test
    public void testUnemploymentCalculator() {
        test.info("מתחיל ניווט למחשבון אבטלה...");

        HomePage homePage = new HomePage(driver);
        homePage.clickMainMenu(BtlMenu.BENEFITS);
        homePage.clickSubMenu("אבטלה");

        UnemploymentInfoPage infoPage = new UnemploymentInfoPage(driver);
        UnemploymentCalculatorPage calculator = infoPage.navigateToCalculator();

        String recentDate = getRecentDate();
        test.info("תאריך הפסקת עבודה שנבחר: " + recentDate);
        System.out.println("תאריך הפסקת עבודה: " + recentDate);

        calculator.fillStep1(recentDate);

        // שימוש נכון ב-WebDriverWait (כמו שעשית כבר!)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[id$='_IncomeGrid_ctl02_Txt_Sallary']")));

        test.info("ממלא שכר חודשי: 12000");
        calculator.fillStep2("12000");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'שכר יומי ממוצע')]")));
        test.info("תוצאות התקבלו, מבצע בדיקות תקינות (Assertions)...");
        System.out.println("מוודא שדף התוצאות עלה עם כל הנתונים...");

        Assertions.assertTrue(calculator.isAvgDailyWageDisplayed(), "חסר: שכר יומי ממוצע");
        Assertions.assertTrue(calculator.isDailyBenefitDisplayed(), "חסר: דמי אבטלה ליום");
        Assertions.assertTrue(calculator.isMonthlyBenefitDisplayed(), "חסר: דמי אבטלה לחודש");

        test.info("כל הנתונים הופיעו בהצלחה!");
        System.out.println("כל הנתונים הופיעו בהצלחה!");
    }

    @AfterAll
    public static void tearDownReport() {
        extent.flush(); // שמירת הדוח
        System.out.println(">>> דוח אבטלה נוצר: unemployment-report.html");
    }

    private String getRecentDate() {
        LocalDate date = LocalDate.now().minusDays(21);
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}