package org.example.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InsuranceCalculatorPage extends BtlBasePage {

    // --- שלב 1 ---
    @FindBy(css = "input[id$='_BirthDate_Date']")
    private WebElement birthDateField;

    @FindBy(css = "input[id$='_rdb_employeType_1']")
    private WebElement yeshivaStudentRadio;

    @FindBy(css = "input[id$='_rdb_Gender_0']")
    private WebElement genderMaleRadio;

    // כפתור המשך שלב 1 בלבד (לפי התמונה הראשונה)
    @FindBy(css = "input[id$='StartNextButton']")
    private WebElement continueButtonStep1;

    // --- שלב 2 ---
    @FindBy(css = "input[id$='_rdb_GetNechut_1']")
    private WebElement disabilityNoRadio;

    // כפתור המשך שלב 2 בלבד (לפי התמונה האחרונה ששלחת)
    @FindBy(css = "input[id$='StepNextButton']")
    private WebElement continueButtonStep2;

    // --- שלב 3 (תוצאות) ---
    @FindBy(css = "ul.CalcResult li:nth-of-type(1) strong")
    private WebElement btlResult;

    @FindBy(css = "ul.CalcResult li:nth-of-type(2) strong")
    private WebElement healthResult;

    @FindBy(css = "ul.CalcResult li:nth-of-type(3) strong")
    private WebElement totalResult;

    public InsuranceCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void fillStep1(String date) {
        scrollToElement(yeshivaStudentRadio);
        forceClick(yeshivaStudentRadio);

        scrollToElement(genderMaleRadio);
        forceClick(genderMaleRadio);

        scrollToElement(birthDateField);
        birthDateField.clear();
        birthDateField.sendKeys(date);

        // לחיצה מדויקת על כפתור שלב 1
        scrollToElement(continueButtonStep1);
        forceClick(continueButtonStep1);
    }

    public boolean isStep2Displayed() {
        return disabilityNoRadio.isDisplayed();
    }

    public void fillStep2() {
        scrollToElement(disabilityNoRadio);
        forceClick(disabilityNoRadio);
        scrollToElement(continueButtonStep2);
        forceClick(continueButtonStep2);
    }

    public String getBtlAmount() {
        scrollToElement(btlResult);
        return btlResult.getText();
    }

    public String getHealthAmount() {
        return healthResult.getText();
    }

    public String getTotalAmount() {
        return totalResult.getText();
    }

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