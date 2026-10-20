package org.nova.service.payment;

import org.nova.common.PageResponse;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.payment.Invoice;
import org.nova.web.dto.request.payment.CreateInvoiceRequest;
import org.nova.web.dto.response.payment.InvoiceResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceService {

    List<Invoice> findAll();

    Optional<Invoice> findById(Long id);

    Invoice create(Invoice invoice);

    Invoice update(Long id, Invoice invoice);

    void delete(Long id);

    PageResponse<InvoiceResponse> findPaged(
            int page,
            int size,
            InvoiceStatus status,
            Long branchId,
            UUID memberId,
            String sortBy,
            String sortDirection
    );

    List<InvoiceResponse> findByMemberId(UUID memberId);

    List<InvoiceResponse> findByMembershipId(Long membershipId);

    InvoiceResponse findResponseById(Long id);

    InvoiceResponse createInvoice(CreateInvoiceRequest request, String authenticatedUsername);

    InvoiceResponse cancelInvoice(Long id);
}