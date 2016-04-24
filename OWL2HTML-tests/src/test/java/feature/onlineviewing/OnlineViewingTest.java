package feature.onlineviewing;

import cucumber.api.CucumberOptions;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(Cucumber.class)
@CucumberOptions(format={"pretty", "html:target/cucumber/cucumber-html-report", "json:target/cucumber/cucumber-report.json"})
//@CucumberOptions(format={"pretty", "html:target/cucumber/cucumber-html-report", "json:target/cucumber/cucumber-report.json"},tags={"@positiveScenario"})
public class OnlineViewingTest {


}
