package org.example.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

public class UnemploymentCalculatorPage extends BtlBasePage {

    // --- שלב 1: פרטים ---
    @FindBy(css = "input[id$='_PiturimDate_Date']")
    private WebElement workEndDateField;

    @FindBy(css = "input[id$='_rdb_age_1']")
    private WebElement ageOver28Radio;

    @FindBy(css = "input[id$='StartNextButton']")
    private WebElement continueButtonStep1;

    // --- שלב 2: שכר ---
    // *** התיקון החכם: תופס את *כל* שדות השכר בטבלה בבת אחת לרשימה ***
    @FindBy(css = "input[id$='Txt_Sallary']")
    private List<WebElement> allSalaryFields;

    @FindBy(css = "input[id$='StepNextButton']")
    private WebElement calculateButton;

    // --- שלב 3: תוצאות ---
    @FindBy(xpath = "//label[contains(text(), 'שכר יומי ממוצע')]")
    private WebElement avgDailyWageLabel;

    @FindBy(xpath = "//label[contains(text(), 'דמי אבטלה ליום')]")
    private WebElement dailyBenefitLabel;

    @FindBy(xpath = "//label[contains(text(), 'דמי אבטלה לחודש')]")
    private WebElement monthlyBenefitLabel;


    public UnemploymentCalculatorPage(WebDriver driver) {
        super(driver);
    }

    // --- פונקציות ---

    public void fillStep1(String date) {
        scrollToElement(ageOver28Radio);
        forceClick(ageOver28Radio);

        scrollToElement(workEndDateField);
        workEndDateField.clear();
        workEndDateField.sendKeys(date);

        scrollToElement(continueButtonStep1);
        forceClick(continueButtonStep1);
    }

    public boolean isStep2Displayed() {
        // בודק אם הרשימה של השדות לא ריקה (כלומר, הטבלה נטענה)
        return !allSalaryFields.isEmpty();
    }

    public void fillStep2(String salary) {
        // *** התיקון: לולאה שעוברת על כל חודשי השכר וממלאת אותם ***
        for (WebElement salaryField : allSalaryFields) {
            scrollToElement(salaryField);
            salaryField.clear();
            salaryField.sendKeys(salary);
        }

        scrollToElement(calculateButton);
        forceClick(calculateButton);
    }

    // פונקציות תוצאות
    public boolean isAvgDailyWageDisplayed() {
        return avgDailyWageLabel.isDisplayed();
    }

    public boolean isDailyBenefitDisplayed() {
        return dailyBenefitLabel.isDisplayed();
    }

    public boolean isMonthlyBenefitDisplayed() {
        return monthlyBenefitLabel.isDisplayed();
    }

    // --- פונקציות עזר ---
    private void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
        js.executeScript("window.scrollBy(0, -200);");
    }

    private void forceClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }
}