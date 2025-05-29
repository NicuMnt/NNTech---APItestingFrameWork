package hooks;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Hooks {

    public static RequestSpecification request;
    public static Properties config;

    @Before
    public void setup() throws IOException {
        // Load environment from command-line or default to "staging"
        String env = System.getProperty("env", "staging");
        String configPath = "src/test/resources/config/" + env + ".properties";

        config = new Properties();
        config.load(new FileInputStream(configPath));

        // Set RestAssured baseURI and auth token from config
        RestAssured.baseURI = config.getProperty("baseUrl");

        request = RestAssured
                .given()
                .relaxedHTTPSValidation()
                .header("Content-Type", "application/json");

        System.out.println("Environment: " + env);
        System.out.println("Base URI: " + RestAssured.baseURI);
    }
}



