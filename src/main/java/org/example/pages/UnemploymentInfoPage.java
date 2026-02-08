package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class UnemploymentInfoPage extends BtlBasePage {

    @FindBy(css = "a[href*='/Simulators/AvtCalcIndex/']")
    private WebElement calculatorsMenuLink;

    @FindBy(css = "a[href*='AvtalaCalcNew.aspx']")
    private WebElement specificCalculatorLink;

    public UnemploymentInfoPage(WebDriver driver) {
        super(driver);
    }

    public UnemploymentCalculatorPage navigateToCalculator() {
        calculatorsMenuLink.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(specificCalculatorLink)).click();

        return new UnemploymentCalculatorPage(driver);
    }
}