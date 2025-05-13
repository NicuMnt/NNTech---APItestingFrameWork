package hooks

import io.cucumber.java.Before
import io.restassured.RestAssured

class hooks {

    @Before
    def setup() {
        RestAssured.baseURI = "https://awakening2.eu"
        println "Base URI set to: ${RestAssured.baseURI}"
    }
}




