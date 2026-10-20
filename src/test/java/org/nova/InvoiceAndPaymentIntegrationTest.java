package org.nova;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.nova.web.dto.request.payment.CreateInvoiceRequest;
import org.nova.web.dto.request.payment.CreatePaymentMethodRequest;
import org.nova.web.dto.request.payment.CreatePaymentReceiptRequest;
import org.nova.web.dto.request.payment.UpdatePaymentMethodRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class InvoiceAndPaymentIntegrationTest {

    @Inject
    InvoiceAndPaymentTestHelper testHelper;

    private static final UUID RECEPTIONIST_UUID = UUID.fromString("b1111111-1111-1111-1111-111111111111");

    // ==========================================
    // PAYMENT METHODS CATALOG (GET: all roles)
    // ==========================================

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewPaymentMethods() {
        given()
                .when()
                .get("/api/v1/payment-methods")
                .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "trainer1", roles = {"ENTRENADOR"})
    void testTrainerCanViewPaymentMethods() {
        given()
                .when()
                .get("/api/v1/payment-methods")
                .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue());
    }

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanViewPaymentMethods() {
        given()
                .when()
                .get("/api/v1/payment-methods")
                .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue());
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanViewPaymentMethods() {
        given()
                .when()
                .get("/api/v1/payment-methods")
                .then()
                .statusCode(200)
                .body("status", equalTo(200))
                .body("data", notNullValue());
    }

    @Test
    void testUnauthenticatedCannotViewPaymentMethods() {
        given()
                .when()
                .get("/api/v1/payment-methods")
                .then()
                .statusCode(401);
    }

    // ==========================================
    // PAYMENT METHODS MANAGEMENT (ADMIN ONLY)
    // ==========================================

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCannotCreatePaymentMethod() {
        CreatePaymentMethodRequest req = new CreatePaymentMethodRequest("Criptomoneda", "Pago con Bitcoin o ETH");

        given()
                .contentType(ContentType.JSON)
                .body(req)
                .when()
                .post("/api/v1/payment-methods")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanManagePaymentMethod() {
        String uniqueName = "Test-PayMethod-" + System.currentTimeMillis();
        CreatePaymentMethodRequest createReq = new CreatePaymentMethodRequest(uniqueName, "Descripción método de prueba");

        Number createdId = given()
                .contentType(ContentType.JSON)
                .body(createReq)
                .when()
                .post("/api/v1/payment-methods")
                .then()
                .statusCode(201)
                .body("data.name", equalTo(uniqueName))
                .extract().path("data.id");

        UpdatePaymentMethodRequest updateReq = new UpdatePaymentMethodRequest(uniqueName + "-Modificado", "Actualizado");
        given()
                .contentType(ContentType.JSON)
                .body(updateReq)
                .when()
                .put("/api/v1/payment-methods/" + createdId)
                .then()
                .statusCode(200)
                .body("data.name", equalTo(uniqueName + "-Modificado"));

        given()
                .when()
                .delete("/api/v1/payment-methods/" + createdId)
                .then()
                .statusCode(200);
    }

    // ==========================================
    // INVOICES READ & RBAC
    // ==========================================

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCanListInvoices() {
        given()
                .when()
                .get("/api/v1/invoices")
                .then()
                .statusCode(200)
                .body("data.content", notNullValue())
                .body("data.totalElements", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCannotListAllInvoices() {
        given()
                .when()
                .get("/api/v1/invoices")
                .then()
                .statusCode(403);
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewMyInvoices() {
        given()
                .when()
                .get("/api/v1/invoices/my")
                .then()
                .statusCode(200)
                .body("data", notNullValue())
                .body("data.size()", greaterThanOrEqualTo(1));
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewOwnInvoiceDetail() {
        given()
                .when()
                .get("/api/v1/invoices/1")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1))
                .body("data.memberEmail", equalTo("isabel.navarro@nova.com"));
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCannotViewOtherMemberInvoiceDetail() {
        // Invoice 2 belongs to member2 (c2222222...)
        given()
                .when()
                .get("/api/v1/invoices/2")
                .then()
                .statusCode(403);
    }

    // ==========================================
    // INVOICE CREATION & PAYMENT FLOW
    // ==========================================

    /*
    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCreatesInvoiceAndProcessesPayments() {
        // 1. Create invoice without promotion
        CreateInvoiceRequest invoiceReq = new CreateInvoiceRequest(
                1L,
                1L,
                RECEPTIONIST_UUID,
                null,
                BigDecimal.valueOf(100.00),
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Factura creada en prueba de flujo"
        );

        Number invoiceIdNum = given()
                .contentType(ContentType.JSON)
                .body(invoiceReq)
                .when()
                .post("/api/v1/invoices")
                .then()
                .statusCode(201)
                .body("data.status", equalTo("PENDING"))
                .body("data.amount", equalTo(100.0F))
                .body("data.balanceDue", equalTo(100.0F))
                .extract().path("data.id");

        Long invoiceId = invoiceIdNum.longValue();

        try {
            // 2. Register partial payment of 40.00
            CreatePaymentReceiptRequest partialPayment = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(40.00),
                    "REF-PARTIAL-40",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(partialPayment)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(201)
                    .body("data.amountPaid", equalTo(40.0F));

            // Verify invoice is now PARTIALLY_PAID
            given()
                    .when()
                    .get("/api/v1/invoices/" + invoiceId)
                    .then()
                    .statusCode(200)
                    .body("data.status", equalTo("PARTIALLY_PAID"))
                    .body("data.amountPaid", equalTo(40.0F))
                    .body("data.balanceDue", equalTo(60.0F));

            // 3. Attempt to overpay (try to pay 70.00 when balance is 60.00)
            CreatePaymentReceiptRequest overPayment = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(70.00),
                    "REF-OVERPAY",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(overPayment)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(400);

            // 4. Pay exact remaining balance (60.00)
            CreatePaymentReceiptRequest finalPayment = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(60.00),
                    "REF-FINAL-60",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(finalPayment)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(201);

            // Verify invoice is now PAID
            given()
                    .when()
                    .get("/api/v1/invoices/" + invoiceId)
                    .then()
                    .statusCode(200)
                    .body("data.status", equalTo("PAID"))
                    .body("data.amountPaid", equalTo(100.0F))
                    .body("data.balanceDue", equalTo(0.0F));

            // 5. Attempt another payment on PAID invoice -> 400
            CreatePaymentReceiptRequest extraPayment = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(10.00),
                    "REF-EXTRA",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(extraPayment)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(400);

        } finally {
            testHelper.deleteInvoiceAndReceipts(invoiceId);
        }
    }
    */

    // ==========================================
    // INVOICE CANCELLATION (ADMIN ONLY)
    // ==========================================

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCannotCancelInvoice() {
        Long invoiceId = testHelper.createPendingInvoice(1L, 1L, RECEPTIONIST_UUID, BigDecimal.valueOf(250.00));

        try {
            given()
                    .when()
                    .post("/api/v1/invoices/" + invoiceId + "/cancel")
                    .then()
                    .statusCode(403);
        } finally {
            testHelper.deleteInvoiceAndReceipts(invoiceId);
        }
    }

    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testAdminCanCancelPendingInvoice() {
        Long invoiceId = testHelper.createPendingInvoice(1L, 1L, RECEPTIONIST_UUID, BigDecimal.valueOf(300.00));

        try {
            given()
                    .when()
                    .post("/api/v1/invoices/" + invoiceId + "/cancel")
                    .then()
                    .statusCode(200)
                    .body("data.status", equalTo("CANCELLED"));

            // Attempt to cancel again -> 400
            given()
                    .when()
                    .post("/api/v1/invoices/" + invoiceId + "/cancel")
                    .then()
                    .statusCode(400);

            // Attempt to pay on CANCELLED invoice -> 400
            CreatePaymentReceiptRequest payOnCancelled = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(50.00),
                    "REF-CANCELLED-PAY",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(payOnCancelled)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(400);
        } finally {
            testHelper.deleteInvoiceAndReceipts(invoiceId);
        }
    }

    /*
    @Test
    @TestSecurity(user = "admin", roles = {"ADMIN"})
    void testCannotCancelInvoiceWithPayments() {
        Long invoiceId = testHelper.createPendingInvoice(1L, 1L, RECEPTIONIST_UUID, BigDecimal.valueOf(200.00));

        try {
            CreatePaymentReceiptRequest partialPayment = new CreatePaymentReceiptRequest(
                    invoiceId,
                    1L,
                    BigDecimal.valueOf(50.00),
                    "REF-PARTIAL-50",
                    null
            );

            given()
                    .contentType(ContentType.JSON)
                    .body(partialPayment)
                    .when()
                    .post("/api/v1/payment-receipts")
                    .then()
                    .statusCode(201);

            // Cannot cancel invoice that has registered receipts
            given()
                    .when()
                    .post("/api/v1/invoices/" + invoiceId + "/cancel")
                    .then()
                    .statusCode(400);
        } finally {
            testHelper.deleteInvoiceAndReceipts(invoiceId);
        }
    }
    */

    @Test
    @TestSecurity(user = "recep1", roles = {"RECEPCIONISTA"})
    void testReceptionistCreatesInvoiceWithPromotionDiscount() {
        // Promotion 3 is 'Bono Amigo' (FIXED_AMOUNT 100.00, valid 2025-01-01 to 2025-12-31)
        // Membership 1 has Plan 1 (499.00)
        // With Bono Amigo discount: 499.00 - 100.00 = 399.00
        CreateInvoiceRequest invoiceReq = new CreateInvoiceRequest(
                1L,
                1L,
                RECEPTIONIST_UUID,
                3L,
                null,
                LocalDate.of(2025, 6, 15),
                LocalDate.of(2025, 7, 15),
                "Factura con Bono Amigo aplicada"
        );

        Number invoiceIdNum = given()
                .contentType(ContentType.JSON)
                .body(invoiceReq)
                .when()
                .post("/api/v1/invoices")
                .then()
                .statusCode(201)
                .body("data.promotionId", equalTo(3))
                .body("data.originalAmount", equalTo(250.0F))
                .body("data.discountAmount", equalTo(50.0F))
                .body("data.amount", equalTo(200.0F))
                .body("data.balanceDue", equalTo(200.0F))
                .extract().path("data.id");

        Long invoiceId = invoiceIdNum.longValue();

        try {
            given()
                    .when()
                    .get("/api/v1/invoices/" + invoiceId)
                    .then()
                    .statusCode(200)
                    .body("data.amount", equalTo(200.0F));
        } finally {
            testHelper.deleteInvoiceAndReceipts(invoiceId);
        }
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCanViewOwnInvoicePayments() {
        // Invoice 1 belongs to member1
        given()
                .when()
                .get("/api/v1/invoices/1/payments")
                .then()
                .statusCode(200)
                .body("data", notNullValue());

        // Receipt 1 belongs to Invoice 1
        given()
                .when()
                .get("/api/v1/payment-receipts/1")
                .then()
                .statusCode(200)
                .body("data.id", equalTo(1));
    }

    @Test
    @TestSecurity(user = "member1", roles = {"CLIENTE"})
    void testClientCannotViewOtherMemberReceipt() {
        // Receipt 2 belongs to Invoice 2 (member2)
        given()
                .when()
                .get("/api/v1/payment-receipts/2")
                .then()
                .statusCode(403);
    }
}
