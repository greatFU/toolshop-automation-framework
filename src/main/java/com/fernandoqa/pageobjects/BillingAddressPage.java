package com.fernandoqa.pageobjects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.fernandoqa.components.AbstractComponent;

public class BillingAddressPage extends AbstractComponent{

    public BillingAddressPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//h3[contains(normalize-space(),'Billing Address')]")
    private WebElement billingAddressElement;

    public boolean isLoaded() {
        return waitForVisibility(billingAddressElement).isDisplayed();
    }
}
