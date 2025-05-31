import io.restassured.http.ContentType
import static io.restassured.RestAssured.given
import io.restassured.response.Response

class OrganizedRequest {
    String baseURL
    OrganizedRequest(String baseURL) {

        this.baseURL = baseURL

    }

    def make_request(String endpoint, body=[:] ) {

        println("-" * 30 + " [POST REQUEST " + endpoint + "]" + "-" * 30)
        println("REQUEST BODY:  ${body}")
        Response response = given()
        .contentType(ContentType.JSON)
        .when()
        .body(body)
        .post(this.baseURL + endpoint)

        println("RESPONSE BODY - [${response.getStatusCode().toString()}]: ${response.getBody().asString()}")
        println("-" * 40 + "[END OF RESPONSE]" + "-" * 40 + "\n")
        return response

    }
}

class Runner  {
    static void main(String[] args) {
        def api_client = new OrganizedRequest("https://awakening2.eu")

        def create_account_body = [

                login: "testid1234",
                email: "user2sdwae@gmail.com",
                password: "12342wer",
                social_id: "qwerty",
                securitycode: "123456"


        ]

        def create_api_response = api_client.make_request("/accounts/create/", create_account_body)
        def login_response = api_client.make_request("/accounts/login/", [email: create_account_body.email, password: create_account_body.password] )


    }
}