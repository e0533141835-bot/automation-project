package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class BtlBasePage extends BasePage {

    @FindBy(id = "TopQuestions")
    private WebElement searchField;

    @FindBy(id = "ctl00_SiteHeader_reserve_btnSearch")
    private WebElement searchButton;

    @FindBy(id = "ctl00_Topmneu_BranchesHyperLink")
    private WebElement branchesLink;

    public BtlBasePage(WebDriver driver) {
        super(driver);
    }


    public SearchResultsPage search(String textToSearch) {
        searchField.clear();
        searchField.sendKeys(textToSearch);
        searchButton.click();
        return new SearchResultsPage(driver);
    }

    public void clickMainMenu(BtlMenu menu) {
        String menuText = menu.getHebrewName();
        driver.findElement(By.linkText(menuText)).click();
    }

    public void clickSubMenu(String subMenuText) {
        driver.findElement(By.linkText(subMenuText)).click();
    }

    public BranchesPage goToBranches() {
        branchesLink.click();
        return new BranchesPage(driver);
    }

}