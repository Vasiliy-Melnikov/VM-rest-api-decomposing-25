package api.steps;

import api.models.AddBooksRequest;
import api.models.IsbnModel;
import io.qameta.allure.Step;

import java.util.List;
import java.util.function.Supplier;

import static api.specs.BaseSpec.baseRequestSpec;
import static io.restassured.RestAssured.given;

public class BookStoreSteps {

    private <T> T retryOnBadGateway(Supplier<T> action) {
        int attempts = 3;
        long delayMs = 700;

        for (int i = 1; i <= attempts; i++) {
            try {
                return action.get();
            } catch (AssertionError e) {
                if (i == attempts) throw e;
                try {
                    Thread.sleep(delayMs);
                } catch (InterruptedException ignored) {
                }
            }
        }
        throw new IllegalStateException("unreachable");
    }

    @Step("API: Delete all books for userId='{userId}'")
    public void deleteAllBooks(String userId, String token) {
        retryOnBadGateway(() -> {
            given(baseRequestSpec)
                    .header("Authorization", "Bearer " + token)
                    .when()
                    .delete("/BookStore/v1/Books?UserId=" + userId)
                    .then()
                    .log().ifValidationFails()
                    .statusCode(204);
            return true;
        });
    }

    @Step("API: Add book isbn='{isbn}' to userId='{userId}'")
    public void addBook(String userId, String token, String isbn) {
        retryOnBadGateway(() -> {
            AddBooksRequest body = new AddBooksRequest();
            body.setUserId(userId);
            body.setCollectionOfIsbns(List.of(new IsbnModel(isbn)));

            given(baseRequestSpec)
                    .header("Authorization", "Bearer " + token)
                    .body(body)
                    .when()
                    .post("/BookStore/v1/Books")
                    .then()
                    .log().ifValidationFails()
                    .statusCode(201);

            return true;
        });
    }
}
