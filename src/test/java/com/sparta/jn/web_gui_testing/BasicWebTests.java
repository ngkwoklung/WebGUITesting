package com.sparta.jn.web_gui_testing;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;

public class BasicWebTests {
    private static WebDriver driver;
    private static ChromeDriverService service;
    private static ChromeOptions options;

    @BeforeAll
    static void setupAll() {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("src/test/resources/chromedriver.exe"))  //chromedriver location
                .usingAnyFreePort()
                .build();

        try {
            service.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        options = new ChromeOptions();
        options.addArguments("headless");

        driver = new ChromeDriver(service, options);
        driver.get("https://www.bbc.co.uk/");
    }

    @Test
    @DisplayName("Check that the websriver works")
    void checkThatTheWebsriverWorks() {
        Assertions.assertEquals("BBC - Home", driver.getTitle());
    }

    @Test
    @DisplayName("Check that the URL for the BBC is correct")
    void checkThatTheUrlForTheBbcIsCorrect() {
        Assertions.assertEquals("https://www.bbc.co.uk/", driver.getCurrentUrl());
    }

    @AfterEach
    void tearDown() {
        //driver.close(); //closes browser window after each test
    }

    @AfterAll
    static void tearDownall() {
        service.stop();
//        driver.quit(); //quits the webdriver
    }
}
