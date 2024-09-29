import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class MainPageAccordion {

    private WebDriver driver;
    private By accordion = By.className("accordion__button");

    public MainPageAccordion(WebDriver driver){
        this.driver = driver;
    }

    public void clickAccordeon() {
        List<WebElement> element = driver.findElements(accordion);
//        System.out.println(element.size());
        // Проходим по каждому элементу аккордеона
        for (int i = 0; i < element.size(); i++) {
            WebElement accordionButton = element.get(i);
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", accordionButton);
            String panelId = accordionButton.getAttribute("aria-controls");  // Получаем ID панели, которую должна раскрыть кнопка
            WebElement accordionPanel = driver.findElement(By.id(panelId));

            // Кликаем на кнопку аккордеона
            accordionButton.click();

            new WebDriverWait(driver,1)
                    .until(driver -> accordionPanel.getAttribute("hidden") == null);

            String accordionText = driver.findElement(By.id(panelId)).getText();
            System.out.println(accordionText);
            // Проверяем, что панель раскрылась и текст виден (проверяем отсутствие атрибута hidden)
            if (accordionPanel.isDisplayed() && accordionPanel.getAttribute("hidden") == null) {
                System.out.println("Accordion " + i + " раскрылся, текст отображается.");
            } else {
                System.out.println("Accordion " + i + " не раскрылся.");
            }
        }
    }
}

// Класс с автотестом заказа из хедера
@RunWith(Parameterized.class)
public class AccordionTest {

    private WebDriver driver;
    private final String browser; // Браузер, передаваемый через параметр

    // Конструктор для получения параметра браузера
    public AccordionTest(String browser) {
        this.browser = browser;
    }

    // Параметризация для заполнения форм
    @Parameterized.Parameters
    public static Collection<Object[]> browsers() {
        return Arrays.asList(new Object[][] {
                {"firefox"},
                {"chrome"}
        });
    }

    // Метод для настройки WebDriver в зависимости от браузера
    public void setupDriver() {
        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox", "--headless", "--disable-dev-shm-usage");
            driver = new ChromeDriver(options);
        } else if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--no-sandbox", "--headless");
            driver = new FirefoxDriver(options);
        }
    }

    @Test
    public void checkAccordion() {
        // Инициализируем WebDriver
        setupDriver();

        // Переход на страницу тестового приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Инициализация страницы и выполнение теста
        MainPageAccordion objMainPageAccordion = new MainPageAccordion(driver);
        objMainPageAccordion.clickAccordeon();
    }

    @After
    public void teardown() {
        if (driver != null) {
            // Закрываем браузер
            driver.quit();
        }
    }
}