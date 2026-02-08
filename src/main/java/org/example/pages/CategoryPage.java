package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class CategoryPage extends BtlBasePage {

    @FindBy(css = "li.active.breadcrumbs-item")
    private WebElement activeBreadcrumb;

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public String getActiveBreadcrumbText() {
        return activeBreadcrumb.getText().trim();
    }
}