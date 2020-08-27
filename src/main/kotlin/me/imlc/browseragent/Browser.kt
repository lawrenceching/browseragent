package me.imlc.browseragent

import me.imlc.browseragent.logger.Logger
import org.openqa.selenium.By
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated
import org.openqa.selenium.support.ui.WebDriverWait
import java.util.concurrent.TimeUnit


class Browser {

    private val logger = Logger(Browser::class.java)
    private val driver: ChromeDriver


    constructor() {
        val options = ChromeOptions()

        // https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver
        options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")

        logger.info("Using Google Chrome ${options.version}")
        driver = ChromeDriver(options)
    }

    fun get(url: String) {
        driver.get(url)
    }

    fun waitForUserLogin() {
        WebDriverWait(driver, TimeUnit.MINUTES.toSeconds(30)).until(
                ExpectedConditions.elementToBeClickable(
                        By.xpath("//*[@id=\"internationalHeader\"]/div[1]/div/div[3]/div[2]/div[1]/span/div/img")
                )
        )
    }

    fun gotoLoginPage() {

        val loginButton = WebDriverWait(driver, 5).until(
                ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"internationalHeader\"]/div[1]/div/div[3]/div[2]/div[1]/div/span/div/span"))
        )

        loginButton.click()

//        fun findLoginButtonAfterHover(): WebElement? {
//            return driver.findElement(By.xpath("//*[@id=\"van-popover-830\"]/div[1]/div[2]/a[2]"))
//        }
//
//
//        val actions = Actions(driver)
//        val loginHoverButton = driver.findElement(By.xpath("//*[@id=\"internationalHeader\"]/div[1]/div/div[3]/div[2]/div[1]/div/span/div/span"))
//        actions.moveToElement(loginHoverButton).perform()
//
//        WebDriverWait(driver, 5).until(ExpectedConditions.invisibilityOf(findLoginButtonAfterHover()))
//
//        actions.moveToElement(findLoginButtonAfterHover()).click().perform()
    }

    fun readBilibiliMetricsFrom(url: String): BilibiliMetric {

        fun readNumber(xpath: String): Int? {
            val like = WebDriverWait(driver, 5)
                    .until(visibilityOfElementLocated(By.xpath(xpath)))

            WebDriverWait(driver, 5).until { driver ->
                val text = driver.findElement(By.xpath(xpath)).text
                text.isNotBlank() && text.length > 2
            }

            val text = like.text

            try {
                return text.toInt()
            } catch (e: NumberFormatException) {
                logger.error("Unable to convert text to int: $text")
            }

            return null
        }


        driver.get(url)

        return BilibiliMetric(
                title = driver.title,
                url = url,
                like = readNumber("//*[@id=\"arc_toolbar_report\"]/div[1]/span[1]"),
                coin =  readNumber("//*[@id=\"arc_toolbar_report\"]/div[1]/span[2]"),
                collect = readNumber("//*[@id=\"arc_toolbar_report\"]/div[1]/span[3]"),
                share = readNumber("//*[@id=\"arc_toolbar_report\"]/div[1]/span[4]")
        )

    }

    fun close() {
        driver.quit()
        logger.info("Quit browser")
    }

    companion object {
        fun newDriver(): ChromeDriver {
            System.setProperty("webdriver.chrome.driver", "/Users/lawrence/sdks/chromedriver/84.0.4147.30/chromedriver")
            val options = ChromeOptions()
            options.addArguments("--whitelisted-ips=''")
            // https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver
            options.setBinary("/Applications/Google Chrome.app/Contents/MacOS/Google Chrome")
            return ChromeDriver(options)
        }
    }
}