package org.example.pages;

import org.openqa.selenium.WebDriver;

public class HomePage extends BtlBasePage {

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public InsuranceInfoPage goToInsuranceInfo() {
        clickMainMenu(BtlMenu.INSURANCE);
        clickSubMenu("דמי ביטוח לאומי");
        return new InsuranceInfoPage(driver);
    }
}