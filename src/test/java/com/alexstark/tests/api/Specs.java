package com.alexstark.tests.api;

import com.alexstark.helpers.AllureRestAssuredFilter;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.RestAssured.with;

public class Specs {

    // spec for ReqresinTests

    public static RequestSpecification requestSpec = with()
            .filter(AllureRestAssuredFilter.withCustomTemplates())
            .log().all()
            .contentType(ContentType.JSON)
            .baseUri("https://reqres.in")
            .basePath("/api");


    public static ResponseSpecification responseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
}
