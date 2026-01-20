package api.steps;

import api.models.AddBooksRequest;
import api.models.IsbnModel;
import io.qameta.allure.Step;

import java.util.List;

import static api.specs.BaseSpec.baseRequestSpec;
import static io.restassured.RestAssured.given;

public class BookStoreSteps {

    @Step("Delete all books for user via API")
    public void deleteAllBooks(String userId, String token) {
        given(baseRequestSpec)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete("/BookStore/v1/Books?UserId=" + userId)
                .then()
                .log().ifValidationFails()
                .statusCode(204);
    }

    @Step("Add book to user's collection via API")
    public void addBook(String userId, String token, String isbn) {
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
    }
}