import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;


public class MeghuTest {
    public static WebDriver driver;
    public static String baseURL = "https://www.meghu.com/";

    @BeforeAll
    public static void initTestMeghuTest() {
        System.setProperty("chromedriver.chrome.driver", "chromedriver-mac-arm64");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.navigate().refresh();
    }

    @Test
    public void testLogoText() {
        driver.get(baseURL);
        WebElement logo = driver.findElement(By.cssSelector("#logo_text"));
        assertEquals("MEGHU.COM", logo.getText(), "Logo text is incorrect");
    }

    @Test
    public void testNameField() {
        driver.get(baseURL);
        WebElement nameField = driver.findElement(By.xpath("//*[@id=\"name\"]"));
        nameField.sendKeys("Milena");
        String actualNameFieldValue = nameField.getAttribute("value");

        assertEquals("Milena", actualNameFieldValue, "Name field value mismatch");

        nameField.sendKeys(Keys.ENTER);
        nameField.sendKeys(Keys.TAB); //maps to the next email section
    }

    @Test
    public void testPrivacyLink() {
        driver.get(baseURL);
        WebElement iFrame = driver.findElement(By.xpath("//*[@id=\"contact_form\"]/div[5]/div[1]/div/div/div/iframe"));

        // Switch to the frame
        driver.switchTo().frame(iFrame);

        //specifying timeout
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement privacyLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Privacy")));
        String privacyText = privacyLink.getText();

        assertEquals("Privacy", privacyText, "Privacy link text does not match expected value");

        driver.switchTo().defaultContent();
    }


    @Test
    public void testPhoneField() {
        driver.get(baseURL);

        By phoneLocator = RelativeLocator.with(By.id("phone")).below(By.tagName("input"));
        WebElement phoneField = driver.findElement(phoneLocator);
        phoneField.sendKeys("+374");

        //get the class attribute of the phone Field element if exists
        String phoneFieldClass = phoneField.getAttribute("class");
        assertNotNull(phoneFieldClass, "Class attribute of phone field is null");
        assertEquals("form-control", phoneFieldClass, "Class attribute of phone field does not match expected value");

        // Assert the background-color attribute of the phone field if exists
        String phoneFieldStyle = phoneField.getAttribute("background-color");
        assertNull(phoneFieldStyle, "Style attribute of phone field does not match expected value");

    }

    @Test
    public void testSend() {
        driver.get(baseURL);

        //Send Button
        WebElement send = driver.findElement(By.cssSelector("#contact_form > div.form-group.row.mb-0.mt-2 > div.col-sm-4.text-right > input"));
        send.click();

        //Hidden view that pops whenever email field is empty
        WebElement hiddenView = driver.findElement(By.cssSelector("#contact_form > div:nth-child(5) > div:nth-child(2)"));
        boolean displayed = hiddenView.isDisplayed();
        assertTrue(displayed, "Hidden view indicating empty email field is not displayed");
    }

    @Test
    public void testMessageArea() {
        driver.get(baseURL);

        //Message text area
        WebElement textArea = driver.findElement(By.xpath("//*[@id=\"contact_form\"]/div[4]/div/textarea"));
        textArea.click();
        Point locationTextArea = textArea.getLocation();
        int xCoordinate = locationTextArea.getX();
        int yCoordinate = locationTextArea.getY();
        System.out.println("The top-left corner of the element relative to the top-left corner of the page's viewport: x = " + xCoordinate + ", y = "+ yCoordinate + ".");

        assertTrue(xCoordinate >= 0, "xCoordinate is not within expected range");
        assertTrue(yCoordinate >= 0, "yCoordinate is not within expected range");
    }

    @Test
    public void testTextFields(){
        driver.get(baseURL);

        //list of similar elements
        List<WebElement> textFields = driver.findElements(By.xpath("//input[@type='text']"));

        for (WebElement element : textFields) {
            element.click();
            element.sendKeys("hello");
        }

    }

}
