package com.alexstark;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    //make request to https://selenoid.autotests.cloud/status
    //check total: 20

    @Test
    void checkTotal20() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));

    }

    @Test
    void checkTotal20WithoutGiven() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkTotal20WithResponceAndBadPractice() {
        String responce =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response().asString();

        System.out.println(responce);
        //DON'T DO THAN! BAD PRACTICE! It's example.
        assertEquals("{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\":" +
                "{\"android\":{\"8.1\":{}},\"chrome\":{\"90.0\":{},\"91.0\":{}},\"firefox\":{\"88.0\":" +
                "{},\"89.0\":{}},\"opera\":{\"76.0\":{},\"77.0\":{}}}}\n", responce);
    }

    @Test
    void checkTotal20WithResponce() {
        Integer responce =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(responce);
        assertEquals(20, responce);
    }

    @Test
    void checkTotal20WithTalkAboutResponce() {
        Response responce =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().response();

        System.out.println(responce.asString());
        System.out.println(responce.path("total") + "");
        System.out.println(responce.path("browsers.chrome").toString());
    }

    @Test
    void checkTotal20WithAssertJ() {
        Integer responce =
                get("https://selenoid.autotests.cloud/status")
                        .then()
                        .extract().path("total");

        System.out.println(responce);
        assertThat(responce).isEqualTo(20);
    }

    @Test
    void checkWdHubStatus401() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void checkWdHubStatus200() {
        get("https://user1:1234@selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }

    @Test
    void checkWdHubStatus200WithAuth() {
        given().auth().basic("user1", "1234")
                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200)
                .body("value.ready", is(true));
    }
}
