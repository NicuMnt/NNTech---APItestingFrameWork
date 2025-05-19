package stepDefinitions

import com.google.gson.JsonObject
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.restassured.response.Response
import io.cucumber.java.en.When
import static org.junit.Assert.assertEquals
import static io.restassured.RestAssured.given
import groovy.json.JsonSlurper
import io.restassured.http.ContentType
import groovy.json.JsonOutput



class ApiStepDefinitions {

    private Response response
    def AccID
    def requestBody = [:] //Empty Map initialization bcs groovy might not understand exactly what this is
    def loginRequestBody = [:]
    @Given("I have a valid set of prerequisites")
    def PrepareDynamicalySetofData () {

        def random = new Random()
        def randomLogin = "user${random.nextInt(10000)}"
        def randomEmail = "user${random.nextInt(100000)}@gmail.com"
        def randomPassword = UUID.randomUUID().toString()[0..7] + random.nextInt(999)
        def socialId = "qwerty"
        def securityCode = "123456"


         requestBody = [

                login: randomLogin,
                email: randomEmail,
                password: randomPassword,
                social_id: socialId,
                securitycode: securityCode


        ]

    }
    @When("I send Post request to create the account")
    def SendPostRequestToCreateAccount () {

        String jsonBody = JsonOutput.toJson(requestBody)

        response = given()
        .contentType(ContentType.JSON)
        .body(jsonBody)
        .when()
        .post("https://awakening2.eu/accounts/create/")

        println "Sending request with body: ${jsonBody}"
        println "Status: ${response.getStatusCode()}"
        println "Body: ${response.getBody().asString()}"

    }
    @Then("The response status should be 200")
    def CheckResponseStatus () {

        assert [200, 201].contains(response.getStatusCode())
    }

    @Then("I should store the account ID")
    def StoreAccId () {
        // Parses the HTTP response body (which is a JSON string) into a Groovy map-like object (JSON object)
        def json = new JsonSlurper().parseText(response.getBody().asString())
        accID = json.id
        println "Stored account ID: $accID"


    }

    @Given("I login in with unique data")
    def Login () {

        loginRequestBody = [

                password : requestBody.password,
                email : requestBody.email



        ]

        println "Prepared Login body :  ${loginRequestBody}"


    }

    @Then("I send Post request to login")
    def SendPostLogin () {

        String JsonLoginBody = JsonOutput.toJson(loginRequestBody)

        response = given()
                .contentType(ContentType.JSON)
                .body(JsonLoginBody)
                .when()
                .post("https://awakening2.eu/accounts/login/")



        println "Sending login request with body: ${JsonLoginBody}"
        println "Login response status: ${response.getStatusCode()}"
        println "Login response body: ${response.getBody().asString()}"


    }

    @Then("The login response status should be 200")
    def checkLoginStatus() {
        assert response.getStatusCode() == 200 : "Expected 200, got ${response.getStatusCode()}"
    }

    @Then("I store the access token and CSRF token from the login response")
    def storeAuthTokens() {

        def json = new JsonSlurper().parseText(response.getBody().asString())


        def accessToken = json.access_token
        println "Stored access token: ${accessToken}"



        def cookies = response.getCookies()
        def csrfToken = cookies['csrftoken']
        println "Stored CSRF token: ${csrfToken}"

    }


}