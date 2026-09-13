package org.nova.service.impl.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.payment.PaymentMethod;
import org.nova.repository.PaymentMethodRepository;
import org.nova.service.payment.PaymentMethodService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;

    @Override
    public List<PaymentMethod> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<PaymentMethod> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public PaymentMethod create(PaymentMethod paymentMethod) {
        repository.persist(paymentMethod);
        return paymentMethod;
    }

    @Override
    @Transactional
    public PaymentMethod update(Long id, PaymentMethod paymentMethod) {
        PaymentMethod existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(paymentMethod.getName());
        existing.setDescription(paymentMethod.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}