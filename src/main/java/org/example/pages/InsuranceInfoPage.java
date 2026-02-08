package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class InsuranceInfoPage extends BtlBasePage {

    @FindBy(css = "a[href*='Insurance_NotSachir']")
    private WebElement calculatorLink;

    public InsuranceInfoPage(WebDriver driver) {
        super(driver);
    }

    public InsuranceCalculatorPage clickToOpenCalculator() {
        calculatorLink.click();

        return new InsuranceCalculatorPage(driver);
    }
}