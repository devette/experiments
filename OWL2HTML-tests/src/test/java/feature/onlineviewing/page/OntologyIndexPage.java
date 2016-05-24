package feature.onlineviewing.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OntologyIndexPage {

    private String ontologyUrl;
    private WebDriver webDriver;

    public OntologyIndexPage(WebDriver webDriver, String ontologyUrl) {
        this.ontologyUrl = ontologyUrl;
        this.webDriver = webDriver;
    }

    public void open()  {
        webDriver.get(ontologyUrl);
    }

    public String getTitle() {
        return webDriver.findElement(By.className("title")).getText();
    }

    public void clickClass(String className)  {
        webDriver.findElement(By.xpath("//*[@id='"+ className +"']")).click();
    }
}
