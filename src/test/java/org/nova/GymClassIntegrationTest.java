package org.nova;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.nova.web.dto.request.gymclass.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class GymClassIntegrationTest {

    @Inject
    GymClassTestHelper testHelper;

    private static final UUID TRAINER_UUID = UUID.fromString("a1111111-1111-1111-1111-111111111111");
    private static final UUID MEMBER1_UUID = UUID.fromString("c1111111-1111-1111-1111-111111111111");
    private static final UUID MEMBER2_UUID = UUID.fromString("c2222222-2222-2222-2222-222222222222");

    // ==========================================
    // CLASS TYPES (CATALOG & ADMIN CRUD)
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewClassTypes() {
        given()
                .when()
                .get("/api/v1/class-types")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "trainer1", roles = {"ENTRENADOR"})
    void testTrainerCanViewClassTypes() {
        given()
                .when()
                .get("/api/v1/class-types")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCannotCreateClassType() {
        CreateClassTypeRequest req = new CreateClassTypeRequest("Zumba Gold", "Clase de baile aeróbico");
        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/class-types")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanManageClassType() {
        String uniqueName = "Test-ClassType-" + System.currentTimeMillis();
        CreateClassTypeRequest createReq = new CreateClassTypeRequest(uniqueName, "Tipo de clase de prueba");

        Number idNum = given()
                .contentType(ContentType.JSON)
                .body(createReq)
                .when()
                .post("/api/v1/class-types")
                .then()
                .statusCode(201)
                .body("data.name", equalTo(uniqueName))
                .extract().path("data.id");

        Long typeId = idNum.longValue();

        UpdateClassTypeRequest updateReq = new UpdateClassTypeRequest(uniqueName + "-Modificado", "Actualizado");
        given()
                .contentType(ContentType.JSON)
                .body(updateReq)
                .when()
                .put("/api/v1/class-types/" + typeId)
                .then()
                .statusCode(200)
                .body("data.name", equalTo(uniqueName + "-Modificado"));

        given()
                .when()
                .delete("/api/v1/class-types/" + typeId)
                .then()
                .statusCode(200);
    }

    // ==========================================
    // CLASSES (SCHEDULING & RBAC)
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewClasses() {
        given()
                .when()
                .get("/api/v1/classes")
                .then()
                .statusCode(200)
                .body("data.content", notNullValue())
                .body("data.totalElements", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCannotScheduleClass() {
        CreateClassRequest req = new CreateClassRequest(
                1L, 1L, TRAINER_UUID, Instant.now().plusSeconds(7200), 60L, 20L
        );

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/classes")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanScheduleAndCancelClass() {
        CreateClassRequest req = new CreateClassRequest(
                1L, 1L, TRAINER_UUID, Instant.now().plusSeconds(86400), 45L, 15L
        );

        Number classIdNum = given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/classes")
                .then()
                .statusCode(201)
                .body("data.status", equalTo("SCHEDULED"))
                .body("data.duration", equalTo(45))
                .body("data.maxCapacity", equalTo(15))
                .body("data.availableSpots", equalTo(15))
                .extract().path("data.id");

        Long classId = classIdNum.longValue();

        try {
            // Cancel class
            given()
                    .when()
                    .post("/api/v1/classes/" + classId + "/cancel")
                    .then()
                    .statusCode(200)
                    .body("data.status", equalTo("CANCELLED"));
        } finally {
            testHelper.deleteClassAndRelated(classId);
        }
    }

    // ==========================================
    // CLASS ENROLLMENTS, CAPACITY & WAITLIST
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testEnrollmentCapacityAndAutomaticWaitlistPromotion() {
        // Create class with capacity = 1
        Long classId = testHelper.createTestClass(1L, 1L, TRAINER_UUID, 1L);

        try {
            // 1. Member 1 enrolls -> Spot available -> status ENROLLED
            CreateEnrollmentRequest enrollMember1 = new CreateEnrollmentRequest(classId, MEMBER1_UUID);

            Number enroll1IdNum = given()
                    .contentType(ContentType.JSON)
                    .body(enrollMember1)
                    .when()
                    .post("/api/v1/class-enrollments")
                    .then()
                    .statusCode(201)
                    .body("data.status", equalTo("ENROLLED"))
                    .extract().path("data.id");

            Long enroll1Id = enroll1IdNum.longValue();

            // 2. Member 2 tries to enroll in same full class -> Capacity exceeded -> WAITLISTED
            CreateEnrollmentRequest enrollMember2 = new CreateEnrollmentRequest(classId, MEMBER2_UUID);

            given()
                    .contentType(ContentType.JSON)
                    .body(enrollMember2)
                    .when()
                    .post("/api/v1/class-enrollments")
                    .then()
                    .statusCode(201)
                    .body("data.status", equalTo("WAITLISTED"));

            // Verify class reports 0 available spots and 1 on waitlist
            given()
                    .when()
                    .get("/api/v1/classes/" + classId)
                    .then()
                    .statusCode(200)
                    .body("data.enrolledCount", equalTo(1))
                    .body("data.availableSpots", equalTo(0))
                    .body("data.waitlistCount", equalTo(1));

            // 3. Member 1 cancels their spot -> Member 2 is promoted from WAITLISTED to ENROLLED automatically!
            given()
                    .when()
                    .post("/api/v1/class-enrollments/" + enroll1Id + "/cancel")
                    .then()
                    .statusCode(200)
                    .body("data.status", equalTo("CANCELLED"));

            // Check class state: Member 2 took the spot! Enrolled is 1, available is 0, waitlist is now 0!
            given()
                    .when()
                    .get("/api/v1/classes/" + classId)
                    .then()
                    .statusCode(200)
                    .body("data.enrolledCount", equalTo(1))
                    .body("data.availableSpots", equalTo(0))
                    .body("data.waitlistCount", equalTo(0));

        } finally {
            testHelper.deleteClassAndRelated(classId);
        }
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanViewClassEnrollments() {
        given()
                .when()
                .get("/api/v1/classes/1/enrollments")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewMyEnrollments() {
        given()
                .when()
                .get("/api/v1/class-enrollments/my")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }

    // ==========================================
    // CLASS RATINGS
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientRatings() {
        // Seed has enrollment 1 for member1
        // Check ratings for class 1
        given()
                .when()
                .get("/api/v1/classes/1/ratings")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));

        // Rating detail
        given()
                .when()
                .get("/api/v1/class-ratings/1")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.rating", equalTo(5));
    }

    // ==========================================
    // ATTENDANCES (CHECK-IN & CHECK-OUT)
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCannotCheckInDirectly() {
        CreateAttendanceRequest req = new CreateAttendanceRequest(
                1L, MEMBER1_UUID, null, LocalDate.now(), LocalTime.now(), null
        );

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/attendances")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanCheckInAndCheckOut() {
        CreateAttendanceRequest req = new CreateAttendanceRequest(
                1L, MEMBER1_UUID, null, LocalDate.now(), LocalTime.of(9, 0), null
        );

        Number attIdNum = given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/attendances")
                .then()
                .statusCode(201)
                .body("data.memberId", equalTo(MEMBER1_UUID.toString()))
                .extract().path("data.id");

        Long attendanceId = attIdNum.longValue();

        try {
            UpdateAttendanceExitRequest exitReq = new UpdateAttendanceExitRequest(LocalTime.of(10, 30));
            given()
                    .contentType(ContentType.JSON)
                    .body(exitReq)
                    .when()
                    .put("/api/v1/attendances/" + attendanceId + "/checkout")
                    .then()
                    .statusCode(200)
                    .body("data.exitTime", notNullValue());
        } finally {
            testHelper.deleteAttendance(attendanceId);
        }
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewMyAttendanceHistory() {
        given()
                .when()
                .get("/api/v1/attendances/my")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }
}
