package tech.ada.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.emptyString;

@QuarkusTest
class AuthResourceIT {

    @Test
    void testGenerateJws() {
        String tokenJwt = RestAssured.given()
                .contentType("application/json")
                .post("/auth/jws")
                .then()
                .statusCode(200) // status 200
                .body(Matchers.notNullValue())
                .body(Matchers.not(emptyString()))
                .extract()
                .body()
                .asString();

        System.out.println(tokenJwt);
    }
}