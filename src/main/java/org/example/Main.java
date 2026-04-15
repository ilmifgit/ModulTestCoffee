package org.example;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.time.Duration;

public class Main {
    private static WebDriver driver;
    private static WebDriverWait wait;

    public static void main(String[] args) {
        try {
            // Настройка драйвера

            ChromeOptions options = new ChromeOptions();
            options.addArguments("window-size=1920,1080");
            driver = new ChromeDriver(options);
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            System.out.println("========== ТЕСТ 1: Успешная регистрация нового пользователя ==========");
            testSuccessfulRegistration();

            System.out.println("\n========== ТЕСТ 2: Регистрация с уже существующими данными ==========");
            testRegistrationWithExistingData();

            System.out.println("\n========== ТЕСТ 3: Вход с незарегистрированными данными ==========");
            testLoginWithNonExistentData();

            System.out.println("\n========== ВСЕ ТЕСТЫ ПРОЙДЕНЫ ==========");

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                try {
                    Thread.sleep(3000); // небольшая задержка перед закрытием
                    driver.quit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // ========== Вспомогательные методы ==========

    private static String randomEmail() {
        return "user_" + System.currentTimeMillis() + "_" + (int) (Math.random() * 1000) + "@test.com";
    }

    private static String randomPhone() {
        return "9" + (long) (Math.random() * 1_000_000_000L);
    }

    private static void goToRegistrationForm() {
        driver.get("https://www.coffeeport.ru/"); // начинаем с главной
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".cabinet.header-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("РЕГИСТРАЦИЯ"))).click();
        wait.until(ExpectedConditions.urlContains("/sign-up")); // проверка, что перешли на страницу регистрации
    }

    private static void fillRegistrationForm(String firstName, String email, String phone, String password, String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fields[first-name]"))).sendKeys(firstName);
        driver.findElement(By.name("fields[email]")).sendKeys(email);
        driver.findElement(By.name("fields[phone]")).sendKeys(phone);
        driver.findElement(By.name("fields[password]")).sendKeys(password);
        driver.findElement(By.name("fields[confirm-password]")).sendKeys(confirmPassword);

        // Чекбоксы
        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign-up-personaldataagreement"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign-up-agreement"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("sign-up-serviceoffering"))).click();


        ((JavascriptExecutor) driver).executeScript(
                "document.getElementsByName('fields[google_recaptcha]')[0].value = 'test_token_123';");
    }

    private static void submitRegistration() {
        driver.findElement(By.cssSelector(".button.button-red")).click();
    }

    private static void goToLoginForm() {
        driver.get("https://www.coffeeport.ru/"); // начинаем с главной
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".cabinet.header-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("ВХОД"))).click();
        wait.until(ExpectedConditions.urlContains("/sign-in")); // проверка, что перешли на страницу входа
    }

    private static void fillLoginForm(String email, String phone, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("fields[email]"))).sendKeys(email);
        driver.findElement(By.name("fields[phone]")).sendKeys(phone);
        driver.findElement(By.name("fields[password]")).sendKeys(password);

        // Капча
        ((JavascriptExecutor) driver).executeScript(
                "document.getElementsByName('fields[google_recaptcha]')[0].value = 'test_token_123';");
    }

    private static void submitLogin() {
        driver.findElement(By.cssSelector(".button.button-red")).click();
    }

    private static void logout() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".cabinet.header-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Выйти"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cabinet.header-button")));
    }

    private static void assertLoggedIn() {
        boolean loggedIn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".cabinet.header-button .user-name"))).isDisplayed();
        if (!loggedIn) {
            throw new AssertionError("Пользователь не авторизован");
        }
        System.out.println("Пользователь успешно авторизован");
    }

    private static void assertErrorPresent() {
        boolean errorPresent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class, 'error') or contains(@class, 'alert')]"))).isDisplayed();
        if (!errorPresent) {
            throw new AssertionError("Ожидалась ошибка, но её нет");
        }
        System.out.println("Ошибка успешно обнаружена");
    }

    private static void assertNotLoggedIn() {
        boolean loggedIn = driver.findElements(By.cssSelector(".cabinet.header-button .user-name")).size() > 0;
        if (loggedIn) {
            throw new AssertionError("Неожиданная авторизация");
        }
        System.out.println("Пользователь не авторизова");
    }

    // ========== ТЕСТЫ ==========

    private static void testSuccessfulRegistration() {
        String email = randomEmail();
        String phone = randomPhone();
        String password = "Qwert12432";

        System.out.println("Регистрация с данными: email=" + email + ", phone=" + phone);

        goToRegistrationForm();
        fillRegistrationForm("Auto", email, phone, password, password);
        submitRegistration();

        assertLoggedIn();
        System.out.println("ТЕСТ 1 ПРОЙДЕН");

        // Выходим, чтобы не мешать следующему тесту
        logout();
        System.out.println("Выход из аккаунта выполнен");
    }

    private static void testRegistrationWithExistingData() {
        // Сначала регистрируем пользователя
        String email = randomEmail();
        String phone = randomPhone();
        String password = "Qwert12432";

        System.out.println("Регистрация первого пользователя: email=" + email + ", phone=" + phone);

        goToRegistrationForm();
        fillRegistrationForm("Auto", email, phone, password, password);
        submitRegistration();
        assertLoggedIn();
        logout();

        System.out.println("Попытка зарегистрироваться с теми же данными:");
        goToRegistrationForm();
        fillRegistrationForm("Auto", email, phone, password, password);
        submitRegistration();

        assertErrorPresent();
        System.out.println("ТЕСТ 2 ПРОЙДЕН");
    }

    private static void testLoginWithNonExistentData() {
        String fakeEmail = "never@exists.com";
        String fakePhone = "9000000000";
        String fakePassword = "wrongpass";

        System.out.println("Попытка входа с данными: email=" + fakeEmail + ", phone=" + fakePhone);

        goToLoginForm();
        fillLoginForm(fakeEmail, fakePhone, fakePassword);
        submitLogin();

        assertErrorPresent();
        assertNotLoggedIn();
        System.out.println("ТЕСТ 3 ПРОЙДЕН");
    }
}