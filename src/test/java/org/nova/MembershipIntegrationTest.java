package org.nova;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.nova.web.dto.request.membership.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class MembershipIntegrationTest {

    @Inject
    MembershipTestHelper testHelper;

    private static final UUID MEMBER_UUID = UUID.fromString("c1111111-1111-1111-1111-111111111111");

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewMembershipPlans() {
        given()
                .when()
                .get("/api/v1/membership-plans")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanViewMembershipPlans() {
        given()
                .when()
                .get("/api/v1/membership-plans")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanViewMembershipPlans() {
        given()
                .when()
                .get("/api/v1/membership-plans")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCannotCreateMembershipPlan() {
        CreateMembershipPlanRequest request = new CreateMembershipPlanRequest(
                "Plan No Permitido",
                30L,
                BigDecimal.valueOf(999.0),
                "Descripción",
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/membership-plans")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanCreateMembershipPlan() {
        CreateMembershipPlanRequest request = new CreateMembershipPlanRequest(
                "Plan Test Admin " + System.currentTimeMillis(),
                60L,
                BigDecimal.valueOf(850.0),
                "Plan creado por Admin",
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/membership-plans")
                .then()
                .statusCode(201)
                .body("data.name", startsWith("Plan Test Admin"))
                .body("data.duration", is(60))
                .body("data.price", is(850.0F));
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanChangeMembershipPlan() {
        Long membershipId = testHelper.createActiveMembership(MEMBER_UUID, 1L);

        try {
            ChangePlanRequest request = new ChangePlanRequest(2L, "Upgrade por recepcionista");

            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .put("/api/v1/memberships/" + membershipId + "/change-plan")
                    .then()
                    .statusCode(200)
                    .body("data.id", is(membershipId.intValue()))
                    .body("data.planId", is(2));
        } finally {
            testHelper.deleteMembership(membershipId);
        }
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCannotCancelMembership() {
        Long membershipId = testHelper.createActiveMembership(MEMBER_UUID, 1L);

        try {
            CancelMembershipRequest request = new CancelMembershipRequest("Intento recepcionista");

            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/memberships/" + membershipId + "/cancel")
                    .then()
                    .statusCode(403);
        } finally {
            testHelper.deleteMembership(membershipId);
        }
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanCancelMembership() {
        Long membershipId = testHelper.createActiveMembership(MEMBER_UUID, 1L);

        try {
            CancelMembershipRequest request = new CancelMembershipRequest("Cancelación administrativa autorizada");

            given()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/api/v1/memberships/" + membershipId + "/cancel")
                    .then()
                    .statusCode(200)
                    .body("data.id", is(membershipId.intValue()))
                    .body("data.status", is("CANCELLED"));
        } finally {
            testHelper.deleteMembership(membershipId);
        }
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanFreezeAndUnfreezeMembership() {
        Long membershipId = testHelper.createActiveMembership(MEMBER_UUID, 1L);

        try {
            // Freeze membership
            FreezeMembershipRequest freezeRequest = new FreezeMembershipRequest(
                    LocalDate.now(),
                    LocalDate.now().plusDays(10),
                    "Motivo médico temporal"
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(freezeRequest)
                    .when()
                    .post("/api/v1/memberships/" + membershipId + "/freeze")
                    .then()
                    .statusCode(200)
                    .body("data.id", is(membershipId.intValue()))
                    .body("data.status", is("FROZEN"));

            // Unfreeze membership
            given()
                    .when()
                    .post("/api/v1/memberships/" + membershipId + "/unfreeze")
                    .then()
                    .statusCode(200)
                    .body("data.id", is(membershipId.intValue()))
                    .body("data.status", is("ACTIVE"));
        } finally {
            testHelper.deleteMembership(membershipId);
        }
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewTheirOwnMemberships() {
        given()
                .when()
                .get("/api/v1/memberships/my")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }
}
