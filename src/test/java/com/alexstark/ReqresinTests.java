package com.alexstark;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresinTests {

    @BeforeEach
    void beforeEach() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    @DisplayName("Single user")
    void singleUser() {
        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2),
                        "data.first_name", is("Janet"));
    }

    @Test
    @DisplayName("List users")
    void listUsers() {
        String listUsersResponse =
                given()
                        .when()
                        .get("/api/users?page=2")
                        .then()
                        .statusCode(200)
                        .body("total", is(12))
                        .extract().path("data[2].email").toString();
        assertEquals("tobias.funke@reqres.in", listUsersResponse);
    }

    @Test
    @DisplayName("Single User Not Found")
    void singleUserNotFound() {
        given()
                .when()
                .get("/api/users/55")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create User")
    void create() {
        Response createUserResponse =
                given()
                        .contentType(JSON)
                        .body("{\"name\": \"Max\", \"job\": \"QA\"}")
                        .when()
                        .post("/api/users")
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
                given()
                        .contentType(JSON)
                        .body("{\"name\": \"Alex\", \"job\": \"QA Automation\"}")
                        .when()
                        .put("/api/users/3")
                        .then()
                        .statusCode(200)
                        .extract().response();
        assertThat((updateUserResponse).asString()).contains("Alex");
    }

    @Test
    @DisplayName("Login - Successful")
    void successfulLogin() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}")
                .when()
                .post("/api/login")
                .then()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Login - Unsuccessful")
    void unsuccessfulLogin() {
        given()
                .contentType(JSON)
                .body("{\"email\": \"peter@reqres.in\"}")
                .when()
                .post("/api/login")
                .then()
                .statusCode(400)
                .body("error", is("Missing password"));
    }
}
