package hooks;

import context.TestContext;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import io.cucumber.java.Scenario;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Hooks {
    public static TestContext context;
    public static RequestSpecification request;
    public static Properties config;

    @Before
    public void setup() throws IOException {
        context = new TestContext();
        String env = System.getProperty("env", "staging");
        String configPath = "src/test/resources/config/" + env + ".properties";

        config = new Properties();
        config.load(new FileInputStream(configPath));


        RestAssured.baseURI = config.getProperty("baseUrl");

        request = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("Content-Type", "application/json");

        System.out.println("Environment: " + env);
        System.out.println("Base URI: " + RestAssured.baseURI);
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            System.out.println("Scenario failed: " + scenario.getName());


    }
        context = null;
        }

            }
