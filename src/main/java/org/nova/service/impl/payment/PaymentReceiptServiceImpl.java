package org.nova.service.impl.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.service.payment.PaymentReceiptService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

    private final PaymentReceiptRepository repository;

    @Override
    public List<PaymentReceipt> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<PaymentReceipt> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public PaymentReceipt create(PaymentReceipt paymentReceipt) {
        repository.persist(paymentReceipt);
        return paymentReceipt;
    }

    @Override
    @Transactional
    public PaymentReceipt update(Long id, PaymentReceipt paymentReceipt) {
        PaymentReceipt existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdInvoice(paymentReceipt.getIdInvoice());
        existing.setIdPaymentMethod(paymentReceipt.getIdPaymentMethod());
        existing.setPaymentDate(paymentReceipt.getPaymentDate());
        existing.setAmountPaid(paymentReceipt.getAmountPaid());
        existing.setReference(paymentReceipt.getReference());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}