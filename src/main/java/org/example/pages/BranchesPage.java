package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BranchesPage extends BtlBasePage {

    // הנה ה-h1 שמצאת בתמונה!
    @FindBy(tagName = "h1")
    private WebElement title;

    public BranchesPage(WebDriver driver) {
        super(driver);
    }

    public String getTitleText() {
        return title.getText();
    }
    public BranchDetailsPage clickOnBranch(String branchName) {
        driver.findElement(org.openqa.selenium.By.partialLinkText(branchName)).click();
        return new BranchDetailsPage(driver);
    }
}