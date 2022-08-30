package com.sparta.jn.web_gui_testing;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class HackerNewsTests {
    static WebDriver driver;
    static FluentWait wait;

    @BeforeAll
    static void setupAll() {
        System.setProperty("webdriver.chrome.driver", "src/test/resources/chromedriver.exe");
        driver = new ChromeDriver();
        wait = new FluentWait(driver);
    }

    @BeforeEach
    void setup() {
        driver.get("https://news.ycombinator.com/");
        wait.withTimeout(Duration.ofSeconds(5));
        wait.pollingEvery(Duration.ofSeconds(1));
        wait.ignoring(NoSuchElementException.class);
//        wait.until(ExpectedConditions.;
    }

    @Test
    @DisplayName("Check that we are on the HN homepage")
    void checkThatWeAreOnTheHnHomepage() {
        Assertions.assertEquals("https://news.ycombinator.com/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Check that the new link workds")
    void checkThatTheNewLinkWorkds() {
        driver.findElement(By.linkText("new")).click();
        Assertions.assertEquals("https://news.ycombinator.com/newest", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Check that we can search using Java as a keyword")
    void checkThatWeCanSearchUsingJavaAsAKeyword() {
        driver.findElement(By.name("q")).sendKeys("Java", Keys.ENTER);
//        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Assertions.assertEquals("https://hn.algolia.com/?q=Java", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Check that when we click on the past date yesterday date is selected")
    void checkThatWhenWeClickOnThePastDateYesterdayDateIsSelected() {
        driver.findElement(By.linkText("past")).click();
        String expected = LocalDate.now().minusDays(1).toString();
        String actual = driver.findElement(By.className("pagetop")).findElement(By.tagName("font")).getText();
        System.out.println(driver.findElement(By.className("pagetop")).findElement(By.tagName("font")).getText());
        System.out.println(LocalDate.now());
        Assertions.assertEquals(expected,actual);
    }

//    @Test
//    @DisplayName("webdriver methods")
//    void webdriverMethods() {
//        driver.manage()
//    }

    @Test
    @DisplayName("Check we can open a link in a new tab")
    void checkWeCanOpenALinkInANewTab() {
        String originalTab = driver.getWindowHandle();
        System.out.println(originalTab);
        driver.findElement(By.linkText("new")).sendKeys(Keys.chord(Keys.CONTROL, Keys.ENTER));
        Set<String> handles = driver.getWindowHandles();
        System.out.println(handles);
        for (String tab : handles) {
            if (!originalTab.equals(tab)) {
                driver.switchTo().window(tab);
                break;
            }
        }
        Assertions.assertEquals("https://news.ycombinator.com/newest", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Check that page has 30 articles")
    void checkThatPageHas30Articles() {
        List<WebElement> links = driver.findElements(By.className("titlelink"));
        Assertions.assertEquals(30, links.size());
    }

    @Test
    @DisplayName("Check that after more articles start from 31")
    void checkThatAfterMoreArticlesStartFrom31() {
        driver.findElement(By.linkText("More")).click();
        List<WebElement> links = driver.findElements(By.className("rank"));
        Assertions.assertEquals("31.", links.get(0).getText());
    }

    @Test
    @DisplayName("Check that after more articles ends at 60")
    void checkThatAfterMoreArticlesEndsAt60() {
        driver.findElement(By.linkText("More")).click();
        List<WebElement> links = driver.findElements(By.className("rank"));
        Assertions.assertEquals("60.", links.get(29).getText());

    }

    @Test
    @DisplayName("Check that when using searching bar to search **Java** only articles that has that word shows")
    void checkThatWhenUsingSearchingBarToSearchJavaOnlyArticlesThatHasThatWordShows() {
        driver.findElement(By.name("q")).sendKeys("Java", Keys.ENTER);
        List<WebElement> links = driver.findElements(By.className("Story_title"));
        System.out.println(links.toString());
    }

    @Test
    @DisplayName("Check that points contains numbers")
    void checkThatPointsContainsNumbers(){
        List<WebElement> pointsStrings = driver.findElements(By.className("score"));
//        System.out.println(pointsStrings);
        for (int i = 0; i <pointsStrings.size(); i++) {
            String[] resultArr = pointsStrings.get(i).getText().split(" ");
            Assertions.assertTrue(StringUtils.isNumeric(resultArr[0]));
        }
    }

    @AfterAll
    static void tearDownAll() {
        driver.quit();
    }
}
