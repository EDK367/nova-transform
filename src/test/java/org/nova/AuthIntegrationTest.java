package org.nova;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.nova.web.dto.request.auth.LoginRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class AuthIntegrationTest {

    @Test
    void testLoginWithUsername() {
        LoginRequest request = new LoginRequest("admin", "admin123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("user.username", equalTo("admin"))
                .body("user.roleName", equalTo("ADMIN"))
                .body("user.email", equalTo("admin@solairy.com"));
    }

    @Test
    void testLoginSuperAdminOwner() {
        LoginRequest request = new LoginRequest("superadmin", "admin123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("user.username", equalTo("superadmin"))
                .body("user.roleName", equalTo("SUPER_ADMIN"))
                .body("user.permissions", hasItems("ROLES_MANAGE", "PERMISSIONS_MANAGE"));
    }

    @Test
    void testLoginWithEmail() {
        LoginRequest request = new LoginRequest("admin@solairy.com", "admin123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("user.username", equalTo("admin"))
                .body("user.email", equalTo("admin@solairy.com"));
    }

    @Test
    void testLoginMemberWithEmailCaseInsensitive() {
        LoginRequest request = new LoginRequest("ISABEL.NAVARRO@NOVA.COM", "admin123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("user.username", equalTo("member1"));
    }

    @Test
    void testLoginWithInvalidPassword() {
        LoginRequest request = new LoginRequest("admin", "wrongpassword");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    void testLoginWithNonexistentIdentifier() {
        LoginRequest request = new LoginRequest("nonexistent@nowhere.com", "password123");

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(401);
    }
}
