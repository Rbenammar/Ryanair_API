package testng;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

    @CucumberOptions(
            features = "src/test/resources/features/GetBooking.feature", // Path to the feature file
            glue = "stepdefinitions",  // Package where your step definition classes are
            plugin = {"pretty", "html:target/cucumber-reports"}
    )
    public class TestRunner extends AbstractTestNGCucumberTests {
    }


