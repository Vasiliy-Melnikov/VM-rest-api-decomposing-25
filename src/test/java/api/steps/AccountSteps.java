package api.steps;

import api.models.LoginRequest;
import api.models.LoginResponse;
import api.models.TokenResponse;
import api.specs.BaseSpec;
import io.qameta.allure.Step;

import java.util.function.Supplier;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class AccountSteps {

    private <T> T retryOnBadGateway(Supplier<T> action) {
        int attempts = 3;
        long delayMs = 700;

        for (int i = 1; i <= attempts; i++) {
            try {
                return action.get();
            } catch (AssertionError e) {
                if (i == attempts) throw e;
                try { Thread.sleep(delayMs); } catch (InterruptedException ignored) {}
            }
        }
        throw new IllegalStateException("unreachable");
    }

    @Step("API: generate token")
    public TokenResponse generateToken(String username, String password) {
        return retryOnBadGateway(() -> {
            LoginRequest body = new LoginRequest();
            body.setUserName(username);
            body.setPassword(password);

            return given()
                    .spec(BaseSpec.baseRequestSpec)
                    .body(body)
                    .when()
                    .post("/Account/v1/GenerateToken")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract().as(TokenResponse.class);
        });
    }

    @Step("API: login with fresh token (GenerateToken -> Login)")
    public LoginResponse loginWithFreshToken(String username, String password) {
        generateToken(username, password);
        return login(username, password);
    }

    @Step("API: login (userName/password)")
    public LoginResponse login(String username, String password) {
        return retryOnBadGateway(() -> {
            LoginRequest body = new LoginRequest();
            body.setUserName(username);
            body.setPassword(password);

            return given()
                    .spec(BaseSpec.baseRequestSpec)
                    .body(body)
                    .when()
                    .post("/Account/v1/Login")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .extract().as(LoginResponse.class);
        });
    }

    @Step("API: check authorized (username/password)")
    public void checkAuthorized(String username, String password) {
        retryOnBadGateway(() -> {
            LoginRequest body = new LoginRequest();
            body.setUserName(username);
            body.setPassword(password);

            given()
                    .spec(BaseSpec.baseRequestSpec)
                    .body(body)
                    .when()
                    .post("/Account/v1/Authorized")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200)
                    .body(is("true"));

            return true;
        });
    }

    @Step("API: check user доступен по token")
    public void checkUserByToken(String userId, String token) {
        retryOnBadGateway(() -> {
            given()
                    .spec(BaseSpec.baseRequestSpec)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .get("/Account/v1/User/" + userId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(200);

            return true;
        });
    }
}









