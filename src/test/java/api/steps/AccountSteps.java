package api.steps;

import api.models.LoginRequest;
import api.models.LoginResponse;
import api.models.TokenResponse;
import api.specs.BaseSpec;
import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AccountSteps {

    @Step("API: login (userName/password)")
    public LoginResponse login(String username, String password) {
        LoginRequest body = new LoginRequest();
        body.setUserName(username);
        body.setPassword(password);

        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/Account/v1/Login")
                .then()
                .statusCode(200)
                .extract().as(LoginResponse.class);
    }

    @Step("API: generate token")
    public TokenResponse generateToken(String username, String password) {
        LoginRequest body = new LoginRequest();
        body.setUserName(username);
        body.setPassword(password);

        return given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/Account/v1/GenerateToken")
                .then()
                .statusCode(200)
                .extract().as(TokenResponse.class);
    }

    @Step("API: check authorized (username/password)")
    public void checkAuthorized(String username, String password) {
        LoginRequest body = new LoginRequest();
        body.setUserName(username);
        body.setPassword(password);

        given()
                .spec(BaseSpec.baseRequestSpec)
                .body(body)
                .when()
                .post("/Account/v1/Authorized")
                .then()
                .statusCode(200)
                .body(is("true"));
    }

    @Step("API: check user доступен по token")
    public void checkUserByToken(String userId, String token) {
        given()
                .spec(BaseSpec.baseRequestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/Account/v1/User/" + userId)
                .then()
                .statusCode(200);
    }
}






