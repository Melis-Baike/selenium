package com.example.qa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class QaApplicationTests {

    private WebDriver driver;
    private static final String PATH = "the-internet.herokuapp.com/basic_auth";
    private static final String PROTOCOL = "https://";
    private static final String DELIMITER = ":";
    private static final String SECOND_DELIMITER = "@";
    private static final String CHECK_TEXT = "Not authorized";
    private static final String BODY = "body";
    private static final String CREDENTIAL = "admin";
    private static final String DRIVER_PROPERTY = "webdriver.chrome.driver";
    private static final String PATH_OF_DRIVER = "C:\\Users\\USER\\IdeaProjects\\qa\\data\\chromedriver.exe";
    private static final String ARGUMENT = "--headless";

    @BeforeEach
    void setUp() {
        System.setProperty(DRIVER_PROPERTY, PATH_OF_DRIVER);
        ChromeOptions options = new ChromeOptions();
        options.addArguments(ARGUMENT);
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void shutdown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void testUnsuccessfulLoginWithInvalidCredentials(){
        String path = PROTOCOL + BODY + DELIMITER + BODY + SECOND_DELIMITER + PATH;
        assertEquals(CHECK_TEXT, auth(path, false));
    }

    @Test
    void testUnsuccessfulLoginWithEmptyCredentials() {
        assertEquals(CHECK_TEXT, auth(PROTOCOL.concat(PATH), false));
    }

    @Test
    void testSuccessfulLogin() {
        String path = PROTOCOL + CREDENTIAL + DELIMITER + CREDENTIAL + SECOND_DELIMITER + PATH;
        assertNotEquals(CHECK_TEXT, auth(path, true));
    }

    private String auth(String path, boolean isSuccessWay){
        driver.get(path);
        String bodyText = driver.findElement(By.tagName(BODY)).getText();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        if(isSuccessWay) {
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(By.tagName(BODY), CHECK_TEXT)));
        } else {
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.tagName(BODY), CHECK_TEXT));
        }
        return bodyText.trim();
    }
}
