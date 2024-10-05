import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

class MainPageOrderFlow {
    private WebDriver driver;

    private final By AcceptCookie = By.id("rcc-confirm-button");
    private final By orderButton;

    public MainPageOrderFlow(WebDriver driver, String buttonClass){
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

        WebElement orderButton = driver.findElement(By.cssSelector("div.Order_Buttons__1xGrp button:nth-child(2)"));
        orderButton.click();

        // Ожидание страницы подтверждения заказа
        WebElement orderConfirmation = new WebDriverWait(driver, 3)
                .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));

        // Подтверждение заказа
        WebElement yesButton = orderConfirmation.findElement(By.xpath("//button[contains(text(), 'Да')]"));
        yesButton.click();

        try {
            // Ожидание страницы подтверждения заказа с увеличенным таймаутом
            WebElement orderInformation = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("Order_Modal__YZ-d3")));

            // Проверка текста "Заказ оформлен"
            WebElement header = orderInformation.findElement(By.className("Order_ModalHeader__3FDaJ"));
            String headerText = header.getText();
            Assert.assertTrue("Текст успешного заказа не найден", headerText.contains("Заказ оформлен"));
            System.out.println("Заказ успешно оформлен");
        } catch (TimeoutException e) {
            Assert.fail("Страница подтверждения заказа не появилась после нажатия кнопки 'Да'");
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