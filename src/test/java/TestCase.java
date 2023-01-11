import io.percy.selenium.Percy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class TestCase {
    @Test
    public void test() throws Exception {
        String username =  System.getenv("BROWSERSTACK_USERNAME");
        String accesskey =  System.getenv("BROWSERSTACK_ACCESS_KEY");
        ArrayList<Integer> widths = new ArrayList();

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("browser","Chrome");
        capabilities.setCapability("browser_version","latest");
        capabilities.setCapability("os","Windows");
        capabilities.setCapability("os_version","10");
        capabilities.setCapability("build","Bstack Percy Integration");
        capabilities.setCapability("name","test");
        WebDriver driver = new RemoteWebDriver(new URL("https://"+username+":"+accesskey+"@hub.browserstack.com/wd/hub"),capabilities);
        driver.manage().timeouts().pageLoadTimeout(50, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        try{
            Percy percySnapshots = new Percy(driver);
            SessionId sessionId = ((RemoteWebDriver)driver).getSessionId();
            storeSessionID(sessionId.toString());

            driver.get("https://diagnostics.roche.com/us/en/products/instruments/cobas-c-311-ins-2043.html#productSpecs");
            WebDriverWait wait = new WebDriverWait(driver, 30);
            WebElement acceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            acceptButton.click();
            keepScrolling(driver);

            percySnapshots.snapshot("Product Specs Tab",null, null, false, "div[class='timeline-Viewport']{ visibility: hidden}");
            driver.get("https://diagnostics.roche.com/us/en/products/instruments/cobas-c-311-ins-2043.html#assay");
            keepScrolling(driver);
            percySnapshots.snapshot("Product Assay Menu Tab", null, null, false, "div[class='timeline-Viewport']{ visibility: hidden}");
        }catch (ClassCastException e){
            System.out.printf(e.getMessage());
        }

        driver.quit();
    }
    public void keepScrolling(WebDriver driver){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,400)");
        js.executeScript("window.scrollBy(400,800)");
        js.executeScript("window.scrollBy(800,1200)");
        js.executeScript("window.scrollBy(1200,1600)");
        js.executeScript("window.scrollBy(1600,2000)");
        js.executeScript("window.scrollBy(2000,2400)");
        js.executeScript("window.scrollBy(2400,2800)");
        js.executeScript("window.scrollBy(2800,3200)");
        js.executeScript("window.scrollBy(3200,3600)");
        js.executeScript("window.scrollBy(3600,4000)");
        js.executeScript("window.scrollBy(0,0)");
    }
    public void storeSessionID(String sessionID) throws IOException {
        try (OutputStream output = new FileOutputStream("src/test/resources/session-data/currentSessionID.properties")) {
            Properties prop = new Properties();
            prop.setProperty("sessionId", sessionID);
            prop.store(output, null);
            System.out.println(prop);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
