package org.nova;

import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.trainer.Trainer;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.repository.MemberRepository;
import org.nova.repository.ReceptionistRepository;
import org.nova.repository.RoleRepository;
import org.nova.repository.TrainerRepository;
import org.nova.repository.UserRepository;
import org.nova.web.dto.request.member.CreateMemberRequest;
import org.nova.web.dto.request.user.CreateUserRequest;
import org.nova.web.dto.response.member.CreateMemberResponse;
import org.nova.web.dto.response.user.CreateUserResponse;

import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UserAndMemberIntegrationTest {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Inject
    TrainerRepository trainerRepository;

    @Inject
    ReceptionistRepository receptionistRepository;

    @Inject
    MemberRepository memberRepository;

    @Inject
    UserTransactionRollbackHelper rollbackHelper;

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCannotCreateAdminViaUsersEndpointByName() {
        CreateUserRequest request = new CreateUserRequest(
                "Super",
                "Admin",
                "admin.attempt@test.com",
                "123456789",
                "adminattempt",
                "ADMIN",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("message", containsString("No está permitido crear usuarios con el rol ADMIN"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCannotCreateAdminViaUsersEndpointById() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();

        CreateUserRequest request = new CreateUserRequest(
                "Super",
                "Admin2",
                "admin.attempt2@test.com",
                "123456789",
                "adminattempt2",
                null,
                adminRole.getId(),
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("message", containsString("No está permitido crear usuarios con el rol ADMIN"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCannotCreateClienteViaUsersEndpointByName() {
        CreateUserRequest request = new CreateUserRequest(
                "Client",
                "User",
                "cliente.attempt@test.com",
                "123456789",
                "clienteattempt",
                "CLIENTE",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("message", containsString("No está permitido crear usuarios con el rol CLIENTE"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCannotCreateClienteViaUsersEndpointById() {
        Role clienteRole = roleRepository.findByName("CLIENTE").orElseThrow();

        CreateUserRequest request = new CreateUserRequest(
                "Client",
                "User2",
                "cliente.attempt2@test.com",
                "123456789",
                "clienteattempt2",
                null,
                clienteRole.getId(),
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(400)
                .body("error", is("Bad Request"))
                .body("message", containsString("No está permitido crear usuarios con el rol CLIENTE"));
    }

    /*
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCanCreateEntrenador() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateUserRequest request = new CreateUserRequest(
                "Carlos",
                "Entrenador",
                "trainer." + uniqueSuffix + "@gym.com",
                "987654321",
                "coach_" + uniqueSuffix,
                "ENTRENADOR",
                null,
                null,
                LocalDate.of(2025, 1, 15)
        );

        CreateUserResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateUserResponse.class);

        assertNotNull(response.id());
        assertEquals("ENTRENADOR", response.role());
        assertNotNull(response.temporaryPassword());
        assertEquals(8, response.temporaryPassword().length());

        User user = userRepository.findById(response.id());
        assertNotNull(user);
        assertEquals(response.email(), user.getEmail());
        assertTrue(user.isActive());
        assertEquals("ENTRENADOR", user.getRole().getName());

        assertNotEquals(response.temporaryPassword(), user.getPassword());
        assertTrue(BcryptUtil.matches(response.temporaryPassword(), user.getPassword()));

        Trainer trainer = trainerRepository.findById(response.id());
        assertNotNull(trainer);
        assertEquals(response.id(), trainer.getId());
        assertEquals(LocalDate.of(2025, 1, 15), trainer.getHireDate());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCanCreateRecepcionista() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateUserRequest request = new CreateUserRequest(
                "Maria",
                "Recepcion",
                "reception." + uniqueSuffix + "@gym.com",
                "555444333",
                "recep_" + uniqueSuffix,
                "RECEPCIONISTA",
                null,
                null,
                null
        );

        CreateUserResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateUserResponse.class);

        assertNotNull(response.id());
        assertEquals("RECEPCIONISTA", response.role());
        assertNotNull(response.temporaryPassword());
        assertEquals(8, response.temporaryPassword().length());

        User user = userRepository.findById(response.id());
        assertNotNull(user);
        assertEquals(response.email(), user.getEmail());
        assertTrue(user.isActive());
        assertEquals("RECEPCIONISTA", user.getRole().getName());

        assertNotEquals(response.temporaryPassword(), user.getPassword());
        assertTrue(BcryptUtil.matches(response.temporaryPassword(), user.getPassword()));

        Receptionist receptionist = receptionistRepository.findById(response.id());
        assertNotNull(receptionist);
        assertEquals(response.id(), receptionist.getId());
        assertEquals(LocalDate.now(), receptionist.getHireDate());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCreateSocioCreatesUserAndMemberWithSameId() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateMemberRequest request = new CreateMemberRequest(
                "Roberto",
                "Socio",
                "socio." + uniqueSuffix + "@gym.com",
                "777888999",
                "socio_" + uniqueSuffix,
                null
        );

        CreateMemberResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateMemberResponse.class);

        assertNotNull(response.id());
        assertEquals("CLIENTE", response.role());
        assertNotNull(response.temporaryPassword());
        assertEquals(8, response.temporaryPassword().length());
        assertEquals(LocalDate.now(), response.registrationDate());

        User user = userRepository.findById(response.id());
        assertNotNull(user);
        assertEquals(response.email(), user.getEmail());
        assertEquals("CLIENTE", user.getRole().getName());
        assertTrue(user.isActive());
        assertTrue(BcryptUtil.matches(response.temporaryPassword(), user.getPassword()));

        Member member = memberRepository.findById(response.id());
        assertNotNull(member);
        assertEquals(user.getId(), member.getId());
        assertEquals(LocalDate.now(), member.getRegistrationDate());
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCanCreateMember() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateMemberRequest request = new CreateMemberRequest(
                "Lucia",
                "SocioRecep",
                "recepsocio." + uniqueSuffix + "@gym.com",
                "444333222",
                "recepmem_" + uniqueSuffix,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(201)
                .body("data.role", is("CLIENTE"));
    }
    */

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCannotCreateInternalUser() {
        CreateUserRequest request = new CreateUserRequest(
                "Forbidden",
                "User",
                "forbidden@gym.com",
                "111222333",
                "forbiddenuser",
                "ENTRENADOR",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(403);
    }

    @Test
    void testUnauthenticatedCannotAccessUsers() {
        given()
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testGetAllRoles() {
        given()
                .when()
                .get("/api/v1/roles")
                .then()
                .statusCode(200)
                .body("data", hasSize(4))
                .body("data.name", hasItems("ADMIN", "ENTRENADOR", "RECEPCIONISTA", "CLIENTE"));
    }

    /*
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testDuplicateEmailIsRejected() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "shared.email." + uniqueSuffix + "@gym.com";

        CreateUserRequest firstRequest = new CreateUserRequest(
                "First",
                "User",
                email,
                "111222333",
                "first_" + uniqueSuffix,
                "ENTRENADOR",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(firstRequest)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201);

        CreateUserRequest secondRequest = new CreateUserRequest(
                "Second",
                "User",
                email,
                "444555666",
                "second_" + uniqueSuffix,
                "RECEPCIONISTA",
                null,
                null,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(secondRequest)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(409)
                .body("error", is("Conflict"))
                .body("message", containsString("ya se encuentra registrado"));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testDuplicateUsernameIsRejected() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String username = "samename_" + uniqueSuffix;

        CreateMemberRequest firstRequest = new CreateMemberRequest(
                "First",
                "Member",
                "email1." + uniqueSuffix + "@gym.com",
                "111222333",
                username,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(firstRequest)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(201);

        CreateMemberRequest secondRequest = new CreateMemberRequest(
                "Second",
                "Member",
                "email2." + uniqueSuffix + "@gym.com",
                "444555666",
                username,
                null
        );

        given()
                .contentType(ContentType.JSON)
                .body(secondRequest)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(409)
                .body("error", is("Conflict"))
                .body("message", containsString("ya se encuentra registrado"));
    }
    */

    /*
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testStoredPasswordIsHashedAndNotPlaintext() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateUserRequest request = new CreateUserRequest(
                "Hash",
                "Verification",
                "hash." + uniqueSuffix + "@gym.com",
                "123123123",
                "hash_" + uniqueSuffix,
                "ENTRENADOR",
                null,
                null,
                null
        );

        CreateUserResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateUserResponse.class);

        User user = userRepository.findById(response.id());
        assertNotNull(user);
        assertNotEquals(response.temporaryPassword(), user.getPassword());
        assertTrue(user.getPassword().startsWith("$2a$") || user.getPassword().startsWith("$2b$"));
        assertTrue(BcryptUtil.matches(response.temporaryPassword(), user.getPassword()));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testTemporaryPasswordIsExactly8Characters() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateMemberRequest request = new CreateMemberRequest(
                "Length",
                "Check",
                "length." + uniqueSuffix + "@gym.com",
                "123123123",
                "length_" + uniqueSuffix,
                null
        );

        CreateMemberResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateMemberResponse.class);

        assertEquals(8, response.temporaryPassword().length());
        assertTrue(response.temporaryPassword().matches(".*[A-Za-z].*"));
        assertTrue(response.temporaryPassword().matches(".*[0-9].*"));
    }
    */

    @Test
    void testTransactionRollbackOnException() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        String email = "rollback." + uniqueSuffix + "@gym.com";
        String username = "rollback_" + uniqueSuffix;

        assertThrows(IllegalStateException.class, () ->
                rollbackHelper.executeFailingTransaction(email, username)
        );

        assertFalse(userRepository.existsByEmail(email));
        assertFalse(userRepository.existsByUsername(username));
    }

    /*
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testSoftDeleteUser() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateUserRequest request = new CreateUserRequest(
                "Delete",
                "User",
                "delete." + uniqueSuffix + "@gym.com",
                "999888777",
                "del_" + uniqueSuffix,
                "ENTRENADOR",
                null,
                null,
                null
        );

        CreateUserResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/users")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateUserResponse.class);

        given()
                .when()
                .delete("/api/v1/users/" + response.id())
                .then()
                .statusCode(204);

        User deletedUser = userRepository.findById(response.id());
        assertNotNull(deletedUser);
        assertFalse(deletedUser.isActive());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testSoftDeleteMember() {
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        CreateMemberRequest request = new CreateMemberRequest(
                "Delete",
                "Member",
                "delmember." + uniqueSuffix + "@gym.com",
                "999888777",
                "delmem_" + uniqueSuffix,
                null
        );

        CreateMemberResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/members")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getObject("data", CreateMemberResponse.class);

        given()
                .when()
                .delete("/api/v1/members/" + response.id())
                .then()
                .statusCode(204);

        Member member = memberRepository.findByIdWithUser(response.id()).orElseThrow();
        assertNotNull(member);
        assertFalse(member.getUsers().isActive());
    }
    */

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testPaginatedUsers() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 2)
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .body("status", is(200))
                .body("data.page", is(0))
                .body("data.size", is(2))
                .body("data.content", hasSize(2));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanGetPaginatedMembers() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 2)
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .body("status", is(200))
                .body("data.page", is(0))
                .body("data.size", is(2))
                .body("data.content", hasSize(2));
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCanGetPaginatedMembers() {
        given()
                .queryParam("page", 0)
                .queryParam("size", 2)
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .body("status", is(200))
                .body("data.page", is(0))
                .body("data.size", is(2));
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCanGetMemberById() {
        Member firstMember = memberRepository.findAll().firstResult();
        assertNotNull(firstMember);

        given()
                .when()
                .get("/api/v1/members/" + firstMember.getId())
                .then()
                .statusCode(200)
                .body("status", is(200))
                .body("data.id", is(firstMember.getId().toString()));
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCannotDeleteMember() {
        given()
                .when()
                .delete("/api/v1/members/" + UUID.randomUUID())
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCannotAccessUsers() {
        given()
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testRecepcionistaCannotAccessRoles() {
        given()
                .when()
                .get("/api/v1/roles")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testPaginatedUsersSortingByNameAscAndDesc() {
        var ascNames = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.firstName", String.class);

        var descNames = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "desc")
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.firstName", String.class);

        assertTrue(ascNames.size() >= 2);
        assertTrue(descNames.size() >= 2);
        assertNotEquals(ascNames.get(0), descNames.get(0));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testPaginatedUsersSortingByEmailAscAndDesc() {
        var ascEmails = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "email")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.email", String.class);

        var descEmails = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "email")
                .queryParam("order", "desc")
                .when()
                .get("/api/v1/users")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.email", String.class);

        assertTrue(ascEmails.size() >= 2);
        assertTrue(descEmails.size() >= 2);
        assertNotEquals(ascEmails.get(0), descEmails.get(0));
    }

    @Test
    @TestSecurity(user = "recep_user", roles = {"RECEPCIONISTA"})
    void testPaginatedMembersSortingByNameAscAndDesc() {
        var ascNames = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.firstName", String.class);

        var descNames = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "name")
                .queryParam("sortDirection", "desc")
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.firstName", String.class);

        assertTrue(ascNames.size() >= 2);
        assertTrue(descNames.size() >= 2);
        assertNotEquals(ascNames.get(0), descNames.get(0));
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testPaginatedMembersSortingByEmailAscAndDesc() {
        var ascEmails = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "email")
                .queryParam("sortDirection", "asc")
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.email", String.class);

        var descEmails = given()
                .queryParam("page", 0)
                .queryParam("size", 10)
                .queryParam("sortBy", "email")
                .queryParam("order", "desc")
                .when()
                .get("/api/v1/members")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList("data.content.email", String.class);

        assertTrue(ascEmails.size() >= 2);
        assertTrue(descEmails.size() >= 2);
        assertNotEquals(ascEmails.get(0), descEmails.get(0));
    }
}
