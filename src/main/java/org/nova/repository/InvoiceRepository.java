package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.payment.Invoice;

@ApplicationScoped
public class InvoiceRepository implements PanacheRepositoryBase<Invoice, Long> {
}