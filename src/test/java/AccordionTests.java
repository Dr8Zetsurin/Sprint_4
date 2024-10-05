import org.openqa.selenium.WebDriver;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class AccordionTests {

    private WebDriver driver;
    private final String browser;

    public AccordionTests(String browser) {
        this.browser = browser;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> browsers() {
        return Arrays.asList(new Object[][] {
                { "chrome" },
                { "firefox" }
        });
    }

    @Test
    public void checkAccordion() {
        driver = DriverManager.setupDriver(browser);
        driver.get("https://qa-scooter.praktikum-services.ru/");
        MainPageAccordion objMainPageAccordion = new MainPageAccordion(driver);
        objMainPageAccordion.clickAccordeon();
    }

    @After
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}