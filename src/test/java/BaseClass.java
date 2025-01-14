import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;


public class BaseClass {

    // ThreadLocal to maintain separate WebDriver instances for each thread
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    public WebDriver driver;
    public Logger logger; // Logger instance for logging
    public Properties properties;
    SoftAssert softAssert = new SoftAssert(); // SoftAssert for test validations

    // Setup method to initialize WebDriver before test class execution
    @BeforeClass(groups = { })
    @Parameters({ "os", "browser" })
    public void setUp(String os, String br) throws IOException {

        logger = LogManager.getLogger(this.getClass()); // Initialize logger

        // Determine execution environment and initialize WebDriver accordingly

        driver = initializeLocalDriver(br);


        // Configure WebDriver settings
        if (driver != null) {
            setDriver(driver); // Set WebDriver in ThreadLocal
            driver = getDriver();
            driver.manage().deleteAllCookies();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().window().maximize();
            logger.info("WebDriver initialized for: " + br);
        } else {
            logger.error("Failed to initialize WebDriver for browser: " + br);
        }
    }

    // Set WebDriver instance in ThreadLocal
    public static void setDriver(WebDriver driver) {
        tdriver.set(driver);
    }

    // Get WebDriver instance from ThreadLocal
    public static WebDriver getDriver() {
        return tdriver.get();
    }


    // Initialize WebDriver for local execution
    private WebDriver initializeLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome": {
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications", "--disable-popup-blocking",
                        "--disable-extensions", "disable-infobars", "--ignore-certificate-errors");
                chromeOptions.setAcceptInsecureCerts(true);
                return new ChromeDriver(chromeOptions);
            }
            case "edge": {
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications", "--disable-popup-blocking",
                        "--disable-extensions", "disable-infobars", "--ignore-certificate-errors");
                edgeOptions.setAcceptInsecureCerts(true);
                return new EdgeDriver(edgeOptions);
            }
            case "firefox": {
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addArguments("--disable-notifications", "--disable-popup-blocking",
                        "--disable-extensions", "disable-infobars", "--ignore-certificate-errors");
                firefoxOptions.setAcceptInsecureCerts(true);
                firefoxOptions.addPreference("dom.webnotifications.enabled", false);
                firefoxOptions.addPreference("dom.disable_open_during_load", true);
                firefoxOptions.addPreference("extensions.showRecommendedInstalled", false);
                return new FirefoxDriver(firefoxOptions);
            }
            default: {
                logger.error("No matching browser for local execution: " + browser);
                return null;
            }
        }
    }

        // Teardown method to quit WebDriver after test class execution
    @AfterClass()
    public void tearDown() {
        WebDriver driver = getDriver();
        if (driver != null) {
            driver.quit();
            tdriver.remove(); // Remove WebDriver from ThreadLocal
            logger.info("WebDriver quit and removed from ThreadLocal.");
        }
    }


}
