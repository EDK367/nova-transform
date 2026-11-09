package org.nova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.payment.PaymentMethod;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.repository.InvoiceRepository;
import org.nova.repository.PaymentMethodRepository;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.service.impl.payment.PaymentReceiptServiceImpl;
import org.nova.service.notification.EmailService;
import org.nova.web.dto.request.payment.CreatePaymentReceiptRequest;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;
import org.nova.web.mapper.PaymentReceiptMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentReceiptServiceUnitTest {

    @Mock
    private PaymentReceiptRepository repository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Mock
    private PaymentReceiptMapper mapper;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PaymentReceiptServiceImpl paymentReceiptService;

    private Invoice sampleInvoice;
    private PaymentMethod sampleMethod;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("member@test.com");
        user.setFirstName("Carlos");
        user.setLastName("Gomez");

        Member member = new Member();
        member.setUsers(user);

        MembershipPlan plan = new MembershipPlan();
        plan.setName("Plan VIP");

        Membership membership = new Membership();
        membership.setIdMember(member);
        membership.setIdPlan(plan);

        sampleInvoice = new Invoice();
        sampleInvoice.setId(100L);
        sampleInvoice.setAmount(BigDecimal.valueOf(500.00));
        sampleInvoice.setStatus(InvoiceStatus.PENDING);
        sampleInvoice.setIdMembership(membership);

        sampleMethod = new PaymentMethod();
        sampleMethod.setId(1L);
        sampleMethod.setName("Efectivo");
    }

    @Test
    void createReceipt_FullPayment_UpdatesInvoiceStatusToPaid() {
        CreatePaymentReceiptRequest request = new CreatePaymentReceiptRequest(
                100L, 1L, BigDecimal.valueOf(500.00), "REF-001", Instant.now()
        );

        when(invoiceRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleInvoice));
        when(paymentMethodRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleMethod));
        when(repository.sumPaidByInvoiceId(100L)).thenReturn(BigDecimal.ZERO);

        PaymentReceiptResponse expectedResponse = new PaymentReceiptResponse(
                1L, 100L, 1L, "Efectivo", Instant.now(), BigDecimal.valueOf(500.00),
                "REF-001", Instant.now(), Instant.now()
        );
        when(mapper.toResponse(any(PaymentReceipt.class))).thenReturn(expectedResponse);

        PaymentReceiptResponse response = paymentReceiptService.createReceipt(request);

        assertNotNull(response);
        assertEquals(InvoiceStatus.PAID, sampleInvoice.getStatus());
        verify(repository, times(1)).persist(any(PaymentReceipt.class));
        verify(invoiceRepository, times(1)).persist(sampleInvoice);
        verify(emailService, times(1)).sendInvoiceReceipt(
                eq("member@test.com"),
                eq("Carlos Gomez"),
                eq(100L),
                eq("Q500.0"),
                eq("Plan VIP")
        );
    }

    @Test
    void createReceipt_PartialPayment_SetsStatusToPartiallyPaid() {
        CreatePaymentReceiptRequest request = new CreatePaymentReceiptRequest(
                100L, 1L, BigDecimal.valueOf(200.00), "REF-002", Instant.now()
        );

        when(invoiceRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleInvoice));
        when(paymentMethodRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleMethod));
        when(repository.sumPaidByInvoiceId(100L)).thenReturn(BigDecimal.ZERO);

        PaymentReceiptResponse expectedResponse = new PaymentReceiptResponse(
                2L, 100L, 1L, "Efectivo", Instant.now(), BigDecimal.valueOf(200.00),
                "REF-002", Instant.now(), Instant.now()
        );
        when(mapper.toResponse(any(PaymentReceipt.class))).thenReturn(expectedResponse);

        PaymentReceiptResponse response = paymentReceiptService.createReceipt(request);

        assertNotNull(response);
        assertEquals(InvoiceStatus.PARTIALLY_PAID, sampleInvoice.getStatus());
        verify(repository, times(1)).persist(any(PaymentReceipt.class));
        verify(invoiceRepository, times(1)).persist(sampleInvoice);
    }

    @Test
    void createReceipt_WhenAmountExceedsRemainingBalance_ThrowsBadRequestException() {
        CreatePaymentReceiptRequest request = new CreatePaymentReceiptRequest(
                100L, 1L, BigDecimal.valueOf(400.00), "REF-003", Instant.now()
        );

        when(invoiceRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleInvoice));
        when(paymentMethodRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleMethod));
        when(repository.sumPaidByInvoiceId(100L)).thenReturn(BigDecimal.valueOf(200.00));

        assertThrows(BadRequestException.class, () -> paymentReceiptService.createReceipt(request));
        verify(repository, never()).persist(any(PaymentReceipt.class));
    }

    @Test
    void createReceipt_WhenInvoiceAlreadyPaid_ThrowsBadRequestException() {
        sampleInvoice.setStatus(InvoiceStatus.PAID);
        CreatePaymentReceiptRequest request = new CreatePaymentReceiptRequest(
                100L, 1L, BigDecimal.valueOf(100.00), "REF-004", Instant.now()
        );

        when(invoiceRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleInvoice));

        assertThrows(BadRequestException.class, () -> paymentReceiptService.createReceipt(request));
        verify(repository, never()).persist(any(PaymentReceipt.class));
    }

    @Test
    void createReceipt_WhenInvoiceCancelled_ThrowsBadRequestException() {
        sampleInvoice.setStatus(InvoiceStatus.CANCELLED);
        CreatePaymentReceiptRequest request = new CreatePaymentReceiptRequest(
                100L, 1L, BigDecimal.valueOf(100.00), "REF-005", Instant.now()
        );

        when(invoiceRepository.findByIdWithDetails(100L)).thenReturn(Optional.of(sampleInvoice));

        assertThrows(BadRequestException.class, () -> paymentReceiptService.createReceipt(request));
        verify(repository, never()).persist(any(PaymentReceipt.class));
    }
}
