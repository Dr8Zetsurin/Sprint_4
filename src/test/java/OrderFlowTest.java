import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class OrderFlow {
    private WebDriver driver;

    private final By AcceptCookie = By.id("rcc-confirm-button");
    private final By orderButton;

    public OrderFlow(WebDriver driver, String buttonClass){
        this.driver = driver;
        this.orderButton = By.className(buttonClass);
    }

    public void clickAcceptCookie() {
        driver.findElement(AcceptCookie).click();
    }

    public void clickRegisterHeader() {
        driver.findElement(orderButton).click();
    }

    private void orderFirstPage(String firstName,
                                String lastName,
                                String address,
                                String metroStation,
                                String phoneNumber) {
        // Поиск элементов по локаторам и заполнение полей формы
        WebElement firstNameInput = driver.findElement(By.cssSelector("input[placeholder='* Имя']"));
        firstNameInput.sendKeys(firstName);

        WebElement lastNameInput = driver.findElement(By.cssSelector("input[placeholder='* Фамилия']"));
        lastNameInput.sendKeys(lastName);

        WebElement addressInput = driver.findElement(By.cssSelector("input[placeholder='* Адрес: куда привезти заказ']"));
        addressInput.sendKeys(address);

        // Работа с дропдауном (станция метро)
        WebElement metroDropdown = driver.findElement(By.cssSelector(".select-search__value"));
        metroDropdown.click(); // Клик для открытия выпадающего списка

        // Поиск поля для ввода значения станции метро
        WebElement metroInput = driver.findElement(By.cssSelector("input.select-search__input"));
        metroInput.sendKeys(metroStation);

        // Ожидание и выбор соответствующей станции метро
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement metroOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(), '" + metroStation + "')]")));
        metroOption.click();

        WebElement phoneInput = driver.findElement(By.cssSelector("input[placeholder='* Телефон: на него позвонит курьер']"));
        phoneInput.sendKeys(phoneNumber);

        WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Далее')]")));
        ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView();", nextButton);
        nextButton.click();
    }

    private void orderSecondPage(
            String deliveryDate,
            String rentPeriod,
            String scooterColor,
            String courierComment) {

        WebElement datePicker = driver.findElement(By.cssSelector("input[placeholder='* Когда привезти самокат']"));
        new WebDriverWait(driver,3).until(driver -> datePicker.isEnabled());
        datePicker.click();

        new WebDriverWait(driver, 3).until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker")));

        // Find and click the date in the date picker
        WebElement dayElement = driver.findElement(By.xpath("//div[contains(@aria-label, '" + deliveryDate + "')]"));
        dayElement.click();

        WebElement rentInput = driver.findElement(By.className("Dropdown-control"));
        rentInput.click();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        WebElement rentOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(), '" + rentPeriod + "')]")));
        rentOption.click();

        WebElement colorCheckboxLabel = driver.findElement(By.xpath("//label[contains(text(), '" + scooterColor + "')]"));
        colorCheckboxLabel.click();

        WebElement courierInput = driver.findElement(By.cssSelector("input[placeholder='Комментарий для курьера']"));
        courierInput.sendKeys(courierComment);

        WebElement orderButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div[3]/button[2]"));
        orderButton.click();

        // Ожидание страницы подтверждения заказа
        WebElement orderConfirmation = new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));

        // Подтверждение заказа
        WebElement yesButton = orderConfirmation.findElement(By.xpath("//button[contains(text(), 'Да')]"));
        yesButton.click();

        // Ожидание страницы подтверждения заказа
        WebElement orderInformation = new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));

        // Проверка текста "Заказ оформлен"
        WebElement header = orderInformation.findElement(By.className("Order_ModalHeader__3FDaJ"));
        String headerText = header.getText();
        if (headerText.contains("Заказ оформлен")) {
            System.out.println("Заказ успешно оформлен");

        } else {
            System.out.println("Текст успешного заказа не найден");
        }
    }

    public void registerFlow(String firstName,
                             String lastName,
                             String address,
                             String metroStation,
                             String phoneNumber,
                             String deliveryDate,
                             String rentPeriod,
                             String scooterColor,
                             String courierComment) {
        clickAcceptCookie();
        clickRegisterHeader();
        orderFirstPage(
                firstName,
                lastName,
                address,
                metroStation,
                phoneNumber);
        orderSecondPage(
                deliveryDate,
                rentPeriod,
                scooterColor,
                courierComment);
    }
}

// Класс с автотестом заказа из хедера
@RunWith(Parameterized.class)
public class OrderFlowTest {

    private WebDriver driver;
    private final String browser;
    private final String buttonClass;//
    private final String firstName; //
    private final String lastName; //
    private final String address; //
    private final String metroStation; //
    private final String phoneNumber; //
    private final String deliveryDate; //
    private final String rentPeriod; //
    private final String scooterColor; //
    private final String courierComment; //

    // Конструктор для получения параметра браузера
    public OrderFlowTest(String browser,
                         String buttonClass,
                         String firstName,
                         String lastName,
                         String address,
                         String metroStation,
                         String phoneNumber,
                         String deliveryDate,
                         String rentPeriod,
                         String scooterColor,
                         String courierComment) {
        this.browser = browser;
        this.buttonClass = buttonClass;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phoneNumber = phoneNumber;
        this.deliveryDate = deliveryDate;
        this.rentPeriod = rentPeriod;
        this.scooterColor = scooterColor;
        this.courierComment = courierComment;
    }

    public static List<String> browsers() {
        return Arrays.asList("chrome", "firefox");
    }

    public static List<String> orderButtonClasses() {
        return Arrays.asList("Button_Button__ra12g", "Button_Middle__1CSJM");
    }

    public static List<String[]> formParams() {
        return Arrays.asList(
                new String[] {"Иван", "Иванов", "ул. Колотушкина, 1", "Черкизовская", "89991234567", "6-е октября", "двое суток", "серая безысходность", "под прилавком"},
                new String[] {"Петр", "Петров", "ул. Пушкина, 2", "Новокузнецкая", "+79998765432", "1-е октября", "пятеро суток", "чёрный жемчуг", ""}
        );
    }

    // Параметризация для заполнения форм
    @Parameterized.Parameters
    public static Collection<Object[]> testParams() {
        List<Object[]> parameters = new ArrayList<>();

        for (String browser : browsers()) {
            for (String orderButtonClass : orderButtonClasses()) {
                for (String[] formParam : formParams()) {
                    parameters.add(new Object[]{
                            browser,
                            orderButtonClass,
                            formParam[0],
                            formParam[1],
                            formParam[2],
                            formParam[3],
                            formParam[4],
                            formParam[5],
                            formParam[6],
                            formParam[7],
                            formParam[8],
                    });
                }
            }
        }
        return parameters;
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
    public void checkOrderFlow() {
        // Инициализируем WebDriver
        setupDriver();

        // Переход на страницу тестового приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");
        // Инициализация страницы и выполнение теста
        OrderFlow objOrderFlow = new OrderFlow(driver,buttonClass);
        objOrderFlow.registerFlow(firstName,
                lastName,
                address,
                metroStation,
                phoneNumber,
                deliveryDate,
                rentPeriod,
                scooterColor,
                courierComment);
    }

    @After
    public void teardown() {
        if (driver != null) {
            // Закрываем браузер
            driver.quit();
        }
    }
}

