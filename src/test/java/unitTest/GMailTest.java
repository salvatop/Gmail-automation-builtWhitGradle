package unitTest;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import junit.framework.TestCase;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;



public class GMailTest extends TestCase {
    private WebDriver driver;
    private Properties properties = new Properties();

    public void setUp() throws Exception {
        try{
        properties.load(new FileReader(new File("src/test/resources/test.properties")));
        //Dont Change below line. Set this value in test.properties file incase you need to change it..
        System.setProperty("webdriver.chrome.driver",properties.getProperty("webdriver.chrome.driver") );
        driver = new ChromeDriver();
    } catch (Exception e) {
        System.out.println("file not found" + e);
        }
    }

    public void tearDown() throws Exception {
        driver.quit();
    }

    /*
     * Please focus on completing the task
     * 
     */
    @Test
    public void testSendEmail() throws Exception {
        WebDriverWait wait = new WebDriverWait(driver,5, 500);

        driver.get("https://mail.google.com/");

        String emailSubject = properties.getProperty("email.subject");
        String emailBody = properties.getProperty("email.body");

        //login
        WebElement userElement = driver.findElement(By.id("identifierId"));
        userElement.sendKeys(properties.getProperty("username"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("identifierNext"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("password")));
        WebElement passwordElement = driver.findElement(By.name("password"));
        passwordElement.sendKeys(properties.getProperty("password"));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("passwordNext"))).click();

        //click on compose and input subject and body
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='z0']/div"))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.name("to"))).clear();
        driver.findElement(By.name("to")).sendKeys(String.format("%s@gmail.com", properties.getProperty("username")));
        driver.findElement(By.className("aoT")).sendKeys(emailSubject);

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[aria-label='Message Body']"))).click();
        driver.findElement(By.cssSelector("div[aria-label='Message Body']")).sendKeys(emailBody);

        //click on the option menu select label and apply Social label then send
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[data-tooltip='More options']"))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='J-Ph J-N']")));
        WebElement label = driver.findElement(By.cssSelector("div[class='J-Ph J-N']"));
        Actions clickOnLabel = new Actions(driver);
        clickOnLabel.moveToElement(label)
                .click()
                .perform();

        WebElement input = driver.findElement(By.xpath("//input[@class='bqf']"));
        input.sendKeys("Social");
        input.sendKeys(Keys.ENTER);

        driver.findElement(By.xpath("//*[@role='button' and text()='Send']")).click();

        //click on Social tab verify email is under this category, open it and add a star
        wait.until(ExpectedConditions.elementToBeClickable(
                (By.cssSelector("div[aria-label='Social']")))).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                (By.xpath("//div[@class='y6']/span[contains(., '"+ emailSubject +"')]")))).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[aria-label='Not starred']"))).click();
        Thread.sleep(2000);

        String subject = driver.findElement(By.xpath("//h2[@class='hP']")).getText();
        assertEquals(emailSubject, subject);

        String body = driver.findElement(By.xpath("//div[contains(@class, 'ii gt') "
                + "and contains(string(), '"+ emailBody +"')]")).getText();
        assertEquals(emailBody, body);

        String tag = driver.findElement(By.xpath("//div[@name='^smartlabel_social']")).getText();
        assertTrue(tag.contains("Social"));
    }
}
