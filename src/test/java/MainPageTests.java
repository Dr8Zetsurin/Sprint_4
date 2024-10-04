import org.openqa.selenium.WebDriver;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

// Класс с автотестом заказа
@RunWith(Parameterized.class)
public class MainPageTests {

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
    public MainPageTests(String browser,
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

    @Test
    public void checkAccordion() {
        // Инициализируем WebDriver
        driver = DriverManager.setupDriver(browser);

        // Переход на страницу тестового приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");

        // Инициализация страницы и выполнение теста
        MainPageAccordion objMainPageAccordion = new MainPageAccordion(driver);
        objMainPageAccordion.clickAccordeon();
    }

    @Test
    public void checkOrderFlow() {
        // Инициализируем WebDriver
        driver = DriverManager.setupDriver(browser);

        // Переход на страницу тестового приложения
        driver.get("https://qa-scooter.praktikum-services.ru/");
        // Инициализация страницы и выполнение теста
        MainPageOrderFlow objMainPageOrderFlow = new MainPageOrderFlow(driver,buttonClass);
        objMainPageOrderFlow.registerFlow(firstName,
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

