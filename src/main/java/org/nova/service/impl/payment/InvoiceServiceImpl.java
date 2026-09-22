package org.nova.service.impl.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.payment.Invoice;
import org.nova.repository.InvoiceRepository;
import org.nova.service.payment.InvoiceService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;

    @Override
    public List<Invoice> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Invoice> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Invoice create(Invoice invoice) {
        repository.persist(invoice);
        return invoice;
    }

    @Override
    @Transactional
    public Invoice update(Long id, Invoice invoice) {
        Invoice existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdBranch(invoice.getIdBranch());
        existing.setIdMembership(invoice.getIdMembership());
        existing.setIdReceptionist(invoice.getIdReceptionist());
        existing.setStatus(invoice.getStatus());
        existing.setIdPromotion(invoice.getIdPromotion());
        existing.setAmount(invoice.getAmount());
        existing.setIssueDate(invoice.getIssueDate());
        existing.setDueDate(invoice.getDueDate());
        existing.setObservation(invoice.getObservation());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}