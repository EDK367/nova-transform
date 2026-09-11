package org.nova.service.payment;

import org.nova.domain.model.payment.Invoice;

import java.util.List;
import java.util.Optional;

public interface InvoiceService {

    List<Invoice> findAll();

    Optional<Invoice> findById(Long id);

    Invoice create(Invoice invoice);

    Invoice update(Long id, Invoice invoice);

    void delete(Long id);
}