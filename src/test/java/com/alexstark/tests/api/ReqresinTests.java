package com.alexstark.tests.api;

import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.alexstark.tests.api.Specs.requestSpec;
import static com.alexstark.tests.api.Specs.responseSpec;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTests {

    @Test
    @DisplayName("Single user")
    void singleUser() {
        Specs.requestSpec
                .when()
                .get("/users/2")
                .then()
                .spec(responseSpec)
                .body("data.id", is(2),
                        "data.first_name", is("Janet"));
    }

    @Test
    @DisplayName("List users")
    void listUsers() {
        String listUsersResponse =
                given()
                        .spec(requestSpec)
                        .when()
                        .get("/users?page=2")
                        .then()
                        .spec(responseSpec)
                        .body("total", is(12))
                        .extract().path("data[2].email").toString();
        assertEquals("tobias.funke@reqres.in", listUsersResponse);
    }

    @Test
    @DisplayName("Single User Not Found")
    void singleUserNotFound() {
        given()
                .spec(requestSpec)
                .when()
                .get("/users/55")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create User")
    void create() {
        Response createUserResponse =
                Specs.requestSpec
                        .body("{\"name\": \"Max\", \"job\": \"QA\"}")
                        .when()
                        .post("/users")
                        .then()
                        .statusCode(201)
                        .extract().response();
        assertThat((createUserResponse.path("name")).toString()).isEqualTo("Max");
        assertThat((createUserResponse.path("job")).toString()).isEqualTo("QA");
    }

    @Test
    @DisplayName("Update User")
    void update() {
        Response updateUserResponse =
                Specs.requestSpec
                        .body("{\"name\": \"Alex\", \"job\": \"QA Automation\"}")
                        .when()
                        .put("/users/3")
                        .then()
                        .spec(responseSpec)
                        .extract().response();
        assertThat((updateUserResponse).asString()).contains("Alex");
    }

    @Test
    @DisplayName("Login - Successful")
    void successfulLogin() {
        given()
                .spec(requestSpec)
                .body("{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}")
                .when()
                .post("/login")
                .then()
                .spec(responseSpec)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Login - Unsuccessful")
    void unsuccessfulLogin() {
        given()
                .spec(requestSpec)
                .body("{\"email\": \"peter@reqres.in\"}")
                .when()
                .post("/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
