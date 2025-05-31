package stepDefinitions

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.restassured.response.Response
import io.cucumber.java.en.When
import static io.restassured.RestAssured.given
import groovy.json.JsonSlurper
import io.restassured.http.ContentType
import groovy.json.JsonOutput
import context.TestContext
import hooks.Hooks

// multipart - file uploading

class ApiStepDefinitions {

    private Response response
    def AccID
    def requestBody = [:] //Empty Map initialization bcs groovy might not understand exactly what this is
    def loginRequestBody = [:]
    def EmailActivationBody = [:]
    def EmailValidationbody = [:]

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

        println "----- Sending POST Request -----"
        println "Endpoint: /accounts/create/"
        println "Method: POST"
        println "Body: ${jsonBody}"
        println" --------------------------------------------- "


        response = given()
        .contentType(ContentType.JSON)
        .body(jsonBody)
        .when()
        .post("https://awakening2.eu/accounts/create/")


        println "Status code: ${response.getStatusCode()}"
        println" --------------------------------------------- "
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
        Hooks.context.accountId = json.id
        println" --------------------------------------------- "
        println "Stored account ID: ${Hooks.context.accountId}"
        println" --------------------------------------------- "

    }

    @Given("I login in with unique data")
    def Login () {

        loginRequestBody = [

                password : requestBody.password,
                email : requestBody.email



        ]

        println "Prepared Login body :  ${loginRequestBody}"
        println" --------------------------------------------- "


    }

    @Then("I send Post request to login")
    def SendPostLogin () {

        String JsonLoginBody = JsonOutput.toJson(loginRequestBody)

        println "----- Sending POST Request -----"
        println "Endpoint: /accounts/login/"
        println "Method: POST"
        println "Body: ${JsonLoginBody}"

        Hooks.context.response = given()
                .contentType(ContentType.JSON)
                .body(JsonLoginBody)
                .when()
                .post("https://awakening2.eu/accounts/login/")




        println" --------------------------------------------- "
        println "Login response status: ${response.getStatusCode()}"
        println" --------------------------------------------- "
        println "Login response body: ${response.getBody().asString()}"



    }

    @Then("The login response status should be 201")
    def checkLoginStatus() {

        assert response.getStatusCode() == 201 : "Expected 201, got ${response.getStatusCode()}"

    }

    @Then("I store the access token and CSRF token from the login response")
    def storeAuthTokens() {

        def json = new JsonSlurper().parseText(Hooks.context.response.getBody().asString())


        Hooks.context.token = json.access_token
        println" --------------------------------------------- "
        println "Stored access token: ${Hooks.context.token}"


        if (response == null) {
            println "Response is null. Cannot extract cookies."
            return
        }

        def cookies = Hooks.context.response.getCookies()
        Hooks.context.csrfToken = cookies['csrftoken']
        println" --------------------------------------------- "
        println "Stored CSRF token: ${Hooks.context.csrfToken}"
        println" --------------------------------------------- "

    }

    @Then("I Prepare body for Email activation request")
    def prepareEmailActivationBody () {

        EmailActivationBody = [

                email : requestBody.email

        ]


    }

    @Then("I send the Post request for email activation")
    def PreparePostRequestForMailActivation () {

        def JsonEmailActivation = JsonOutput.toJson(EmailActivationBody)

        println "----- Sending POST Request -----"
        println "Endpoint: /send-activation-email/"
        println "Method: POST"
        println "Body: ${JsonEmailActivation}"
        println" --------------------------------------------- "

        response = given()
                .contentType(ContentType.JSON)
                .body(JsonEmailActivation)
                .when()
                .post("https://awakening2.eu/send-activation-email/")

        println "Status code: ${response.getStatusCode()}"
        println" --------------------------------------------- "
        println "Body: ${response.getBody().asString()}"


    }

    @Then("The login response status should be 200")
    def checkLoginStatus200() {

        assert response.getStatusCode() == 200: "Expected 200, got ${response.getStatusCode()}"
    }



//    @Then("I prepare validation Email body")
//    def PreparebodyForMailValidation () {
//        def random = new Random()
//        def activationcode = "123456"
//
//        EmailValidationbody = [
//
//                email          : requestBody.email,
//                activation_code: activationcode
//
//
//        ]
//    }
//    @Then("I send the Post request for email validation")
//    def PreparePostRequestForMailValidation () {
//
//
//
//        def JsonEmailValidation = JsonOutput.toJson(EmailValidationbody)
//
//        response = given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer ${accessToken}")
//                .body(JsonEmailValidation)
//                .when()
//                .post("https://awakening2.eu/validate-activation-code/")
//
//
//
//        println "Response Body: ${response.getBody().asString()}"
//
//    }

}