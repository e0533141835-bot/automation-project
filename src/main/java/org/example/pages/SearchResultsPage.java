package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class SearchResultsPage extends BtlBasePage {

    @FindBy(tagName = "h1")
    private WebElement title;

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public String getTitleText() {
        return title.getText();
    }
}