package org.nova;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class GreetingResourceTest {
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testHelloEndpoint() {
        given()
          .when().get("/api/v1/hello-admin")
          .then()
             .statusCode(200)
             .body(is("Hello from Quarkus REST"));
    }

}