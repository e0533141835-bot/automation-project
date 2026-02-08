package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BranchDetailsPage extends BtlBasePage {

    @FindBy(xpath = "//div[contains(text(), 'רחוב')]")
    private WebElement addressInfo;

    @FindBy(className = "KabalatKahal")
    private WebElement receptionInfo;

    @FindBy(xpath = "//*[contains(text(), 'טלפון')]")
    private WebElement phoneInfo;

    public BranchDetailsPage(WebDriver driver) {
        super(driver);
    }


    public boolean hasAddress() {
        return addressInfo.isDisplayed();
    }

    public boolean hasReceptionInfo() {
        return receptionInfo.isDisplayed();
    }

    public boolean hasPhoneInfo() {
        return phoneInfo.isDisplayed();
    }
}