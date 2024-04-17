import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void sendFormWithValidNameAndPhoneNumber() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("Николай Булкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
    }
    @Test
    void sendFormWithInvalidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("Kolya Bylkin");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }
    @Test
    void sendFormWithInvalidPhoneNumber() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("Николай Булкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("+799988877666");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        WebElement form = driver.findElement(By.cssSelector("[data-test-id=phone]"));
        String text = form.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void sendFormWithValidPhoneNumberAndEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void sendFormWithEmptyPhoneNumberAndValidName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("Николай Булкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }

    @Test
    void sendFormWithValidNameAndPhoneNumberButEmptyCheckBox() {
        driver.findElement(By.cssSelector("[data-test-id=name] input ")).sendKeys("Николай Булкин");
        driver.findElement(By.cssSelector("[data-test-id=phone] input ")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[role=button]")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='agreement'].input_invalid .checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }
}
