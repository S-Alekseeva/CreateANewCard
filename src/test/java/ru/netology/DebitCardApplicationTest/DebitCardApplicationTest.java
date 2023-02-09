package ru.netology.DebitCardApplicationTest;

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

public class DebitCardApplicationTest {

    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTestSuccessfulSending() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__control"));
        list.get(0).sendKeys("Пугачева Алла");
        list.get(1).sendKeys("+79357484905");

        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("paragraph")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @Test
    void shouldTestIncorrectName() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__control"));
        list.get(0).sendKeys("Petrovskaya Inna");
        list.get(1).sendKeys("+79357484905");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }

    @Test
    void shouldTestIncorrectTelephone() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__control"));
        list.get(0).sendKeys("Иванова Наталья");
        list.get(1).sendKeys("+793574849059");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.className("button__text")).click();
        list = driver.findElements(By.className("input__sub"));
        String text = list.get(1).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

    @Test
    void shouldTestEmptyCheckbox() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__control"));
        list.get(0).sendKeys("Иванова Наталья");
        list.get(1).sendKeys("+793574849059");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("checkbox__text")).getText();
        assertEquals("Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй", text.trim());
    }

    @Test
    void shouldTestEmptyForm() {
        driver.get("http://localhost:9999/");
        List<WebElement> list = driver.findElements(By.className("input__control"));
        list.get(0).sendKeys("");
        list.get(1).sendKeys("");
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.className("input__sub")).getText();
        assertEquals("Поле обязательно для заполнения", text.trim());
    }
}
