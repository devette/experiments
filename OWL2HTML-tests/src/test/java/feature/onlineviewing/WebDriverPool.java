package feature.onlineviewing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.safari.SafariDriver;

import java.util.*;

public class WebDriverPool {
    private static WebDriverPool instance;

    private Map<String, WebDriver> drivers = new HashMap<String, WebDriver>();
    private List<WebDriver> driversInUse = new ArrayList<WebDriver>();

    private WebDriverPool() {
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                System.out.println("Shutting down WebDriver Pool");
                for (WebDriver driver : drivers.values()) {
                    driver.close();
                    driver.quit();
                }

                if (!driversInUse.isEmpty())
                    throw new IllegalStateException("There are still drivers in use, did someone forget to return it? (size: " + driversInUse.size() + ")");
            }
        });
    }

    public static WebDriverPool getInstance()  {
        if (instance == null)  {
            instance = new WebDriverPool();
        }
        return instance;
    }

    private WebDriver createFirefoxDriver(Locale locale){
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("intl.accept_languages", formatLocale(locale));
        return new FirefoxDriver(profile);
    }

    private WebDriver createSafariDriver(Locale locale){
        return new SafariDriver();
    }

    private WebDriver createChromeDriver(Locale locale)  {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=" + locale.getLanguage());
        options.addArguments("--silent");
        return new ChromeDriver(options);

    }

    private String formatLocale(Locale locale) {
        return locale.getCountry().length() == 0
                ? locale.getLanguage()
                : locale.getLanguage() + "-" + locale.getCountry().toLowerCase();
    }

    /**
     * @param clazz
     * @param locale
     * @return web driver which can be new or recycled
     */
    public synchronized WebDriver getWebDriver(Class<? extends WebDriver> clazz, Locale locale){

        String key = clazz.getName() + "-" + locale;

        if(!drivers.containsKey(key)){

            if(clazz == FirefoxDriver.class){
                drivers.put(key, createFirefoxDriver(locale));
            }  else if(clazz == SafariDriver.class){
                drivers.put(key, createSafariDriver(locale));
            }  else if(clazz == ChromeDriver.class){
                 drivers.put(key, createChromeDriver(locale));
            }  else{
                throw new IllegalArgumentException(clazz.getName() + " not supported yet!");
            }
        }

        WebDriver driver = drivers.get(key);

        if(driversInUse.contains(driver))
            throw new IllegalStateException("This driver is already in use. Did someone forgot to return it?");

        driversInUse.add(driver);
        return driver;
    }

    public synchronized void returnWebDriver(WebDriver driver){
        driversInUse.remove(driver);
    }
}