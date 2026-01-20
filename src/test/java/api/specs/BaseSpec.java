package api.specs;

import helpers.CustomAllureListener;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.parsing.Parser;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseSpec {

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    public static final RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri("https://demoqa.com")
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .addFilter(CustomAllureListener.withCustomTemplates())
            .build();
}

