package runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",  // path to feature files
        glue = "stepDefinitions, hooks",  // Use comma-separated string for packages
        plugin = "pretty, html:target/cucumber-report.html",  // Use comma-separated string for plugins
        monochrome = true  // Optional, makes the output more readable
)
class TestRunner {
}