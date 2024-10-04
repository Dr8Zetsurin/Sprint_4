import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import static org.junit.Assert.assertEquals;

class MainPageAccordion {

    private WebDriver driver;
    private By accordion = By.className("accordion__button");
    private String[] expectedTexts = {
            "Сутки — 400 рублей. Оплата курьеру — наличными или картой.",
            "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим.",
            "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30.",
            "Только начиная с завтрашнего дня. Но скоро станем расторопнее.",
            "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010.",
            "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится.",
            "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои.",
            "Да, обязательно. Всем самокатов! И Москве, и Московской области."
    };

    public MainPageAccordion(WebDriver driver){
        this.driver = driver;
    }

    public void clickAccordeon() {
        List<WebElement> elements = driver.findElements(accordion);
        for (int i = 0; i < elements.size(); i++) {
            WebElement accordionButton = elements.get(i);
            ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", accordionButton);
            String panelId = accordionButton.getAttribute("aria-controls");
            WebElement accordionPanel = driver.findElement(By.id(panelId));

            accordionButton.click();

            new WebDriverWait(driver, 1)
                    .until(driver -> accordionPanel.getAttribute("hidden") == null);

            String accordionText = driver.findElement(By.id(panelId)).getText();
            System.out.println(accordionText);

            if (accordionPanel.isDisplayed() && accordionPanel.getAttribute("hidden") == null) {
                System.out.println("Аккордион " + i + " раскрылся, текст верный.");
                assertEquals("Аккордион " + i + " текст не соответствует требованиям", expectedTexts[i], accordionText);
            } else {
                System.out.println("Аккордион " + i + " не раскрылся.");
            }
        }
    }
}