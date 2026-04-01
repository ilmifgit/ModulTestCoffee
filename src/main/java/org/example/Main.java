package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import javax.swing.plaf.TableHeaderUI;

import java.sql.Driver;
import java.time.Duration;

import static java.lang.Integer.parseInt;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("window-size=1920,1880");

        WebDriver driver = new ChromeDriver(options);

        try{
            driver.get("https://www.coffeeport.ru/");
            Thread.sleep(3000);
            driver.findElement(By.cssSelector(".cabinet.header-button")).click();
            Thread.sleep(3000);
            driver.findElement(By.linkText("РЕГИСТРАЦИЯ")).click();
            Thread.sleep(3000);
            driver.findElement(By.name("fields[first-name]")).sendKeys("Auto");
            Thread.sleep(300);
            driver.findElement(By.name("fields[email]")).sendKeys("asceind@gmail.com");
            Thread.sleep(300);
            driver.findElement(By.name("fields[phone]")).sendKeys("89219932020");
            Thread.sleep(300);
            driver.findElement(By.name("fields[password]")).sendKeys("Qwert12432");
            Thread.sleep(300);
            driver.findElement(By.name("fields[confirm-password]")).sendKeys("Qwert12432");
            Thread.sleep(300);
//            driver.findElement(By.linkText("Подтверждаю условия")).findElement(By.linkText("Подтверждаю")).click();
//            Thread.sleep(300);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            // Чекбокс 1
            WebElement checkbox1 = driver.findElement(By.id("sign-up-personaldataagreement"));
            js.executeScript("arguments[0].click();", checkbox1);
            Thread.sleep(200);

            // Чекбокс 2
            WebElement checkbox2 = driver.findElement(By.id("sign-up-agreement"));
            js.executeScript("arguments[0].click();", checkbox2);
            Thread.sleep(200);

            // Чекбокс 3
            WebElement checkbox3 = driver.findElement(By.id("sign-up-serviceoffering"));
            js.executeScript("arguments[0].click();", checkbox3);


            try{
                js.executeScript("document.getElementsByName('fields[google_recaptcha]')[0].value = 'test_token_123';");
                driver.findElement(By.className("t-input-group")).click();
                JavascriptExecutor jsfc = (JavascriptExecutor)driver;
                jsfc.executeScript("document.title = '20секунд на решение кпачи'");
                Thread.sleep(20000);
            }finally {
                JavascriptExecutor jsf = (JavascriptExecutor)driver;
                jsf.executeScript("document.title = 'капча'");
            }
            driver.findElement(By.cssSelector(".button.button-red")).click();
            Thread.sleep(3000);
            driver.findElement(By.linkText("МЕНЮ")).click();
            Thread.sleep(3000);
            driver.findElement(By.cssSelector("ЕДА")).click();
            Thread.sleep(3000);
            driver.findElement(By.linkText("Кофейни")).click();
            Thread.sleep(3000);
            driver.findElement(By.linkText("ВКУСНОСТИ")).click();
            Thread.sleep(5000);
            //skroll down
            driver.findElement(By.cssSelector(".button.button-red")).click();
            Thread.sleep(2000);
//            driver.findElement(By.linkText("Апельсиновый -  500 г")).click();
//            Thread.sleep(2000);
            //scroll up
            driver.findElement(By.id("bx_basketFKauil")).click();
            Thread.sleep(2000);
//            driver.findElement(By.linkText("ПЕРЕЙТИ В КОРЗИНУ")).click();
//            Thread.sleep(2000);


        }finally {
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("document.title = 'errroooooorrrrrrr'");
            Thread.sleep(20000);
            driver.quit();
        }
    }
}