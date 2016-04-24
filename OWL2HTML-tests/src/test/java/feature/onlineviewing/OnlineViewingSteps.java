package feature.onlineviewing;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import feature.onlineviewing.page.OntologyIndexPage;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.Locale;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class OnlineViewingSteps {

	private Scenario scenario;
	private WebDriverPool webDriverPool;
	private WebDriver webDriver;
	private OntologyIndexPage ontologyIndexPage;

	@Before
	public void before(Scenario scenario)  {
		this.webDriverPool =  WebDriverPool.getInstance();
		this.scenario = scenario;
	}

	@Given("^browser '(.*)' with language set to '(.*)'$")
	public void setBrowserAndlanguage(String browserName, String language) throws Throwable {
		Class webDriverClass= getWebDriverClassByName(browserName);
		Locale locale = getLocaleByName(language);
		webDriver = webDriverPool.getWebDriver(webDriverClass, locale);
	}

	@Given("^an ontology with name '(.*)' and url '(.*)'$")
	public void anOntologyWithNameOntologyNameAndFile(String ontologyName, String file) throws Throwable {
		//scenario.write(String.format("ONTOLOGY: %s URL %s ", ontologyName, file));
		ontologyIndexPage = new OntologyIndexPage(webDriver, file);

	}

	@When("^the user navigates to the ontology index page$")
	public void navigateToOntologyIndexPage() throws Throwable {
		ontologyIndexPage.open();
	}

	@And("^the class '(.*)' should clickable$")
	public void theClassClassesShouldClickable(String classes) throws Throwable {
		ontologyIndexPage.clickClass(classes);
	}

	@Then("^an index page of the ontology should be available$")
	public void an_index_page_of_the_ontology_should_be_available() throws Throwable {
		String expectedText="INDEX";
		String actualText=ontologyIndexPage.getTitle();
		assertThat("Index page should be available", actualText, equalTo(expectedText));
		takeScreenShot();
	}



	@After
	public void tearDown(Scenario scenario) {
		try {
			if (scenario.isFailed()) {
				takeScreenShot();
			}
		} finally {
			webDriverPool.returnWebDriver(webDriver);
		}
	}

	private Class getWebDriverClassByName(String browserName) {
		Class webDriverClass;
		switch (browserName) {
			case "firefox":
				webDriverClass = FirefoxDriver.class;
				break;
			case "safari":
				webDriverClass = SafariDriver.class;
				break;
			default:
				webDriverClass = FirefoxDriver.class;
		}
		return webDriverClass;
	}

	private Locale getLocaleByName(String language) {
		Locale locale;
		switch (language) {
			case "english":
				locale = Locale.ENGLISH;
				break;
			case "spanish":
				locale = new Locale("es");
				break;
			case "dutch":
				locale = new Locale("nl");
				break;
			default:
				locale = Locale.ENGLISH;
		}
		return locale;
	}

	private void takeScreenShot() {
		final byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
		scenario.embed(screenshot, "image/png");
	}
}
