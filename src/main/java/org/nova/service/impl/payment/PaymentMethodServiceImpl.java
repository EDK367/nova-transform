package org.nova.service.impl.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.payment.PaymentMethod;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.PaymentMethodRepository;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.service.payment.PaymentMethodService;
import org.nova.web.dto.request.payment.CreatePaymentMethodRequest;
import org.nova.web.dto.request.payment.UpdatePaymentMethodRequest;
import org.nova.web.dto.response.payment.PaymentMethodResponse;
import org.nova.web.mapper.PaymentMethodMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository repository;
    private final PaymentReceiptRepository receiptRepository;
    private final PaymentMethodMapper mapper;

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
        if (repository.existsByName(paymentMethod.getName().trim())) {
            throw new ConflictException("Ya existe un método de pago con el nombre: " + paymentMethod.getName());
        }
        repository.persist(paymentMethod);
        return paymentMethod;
    }

    @Override
    @Transactional
    public PaymentMethod update(Long id, PaymentMethod paymentMethod) {
        PaymentMethod existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Método de pago no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(paymentMethod.getName().trim(), id)) {
            throw new ConflictException("Ya existe un método de pago con el nombre: " + paymentMethod.getName());
        }

        existing.setName(paymentMethod.getName().trim());
        existing.setDescription(paymentMethod.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        PaymentMethod method = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Método de pago no encontrado con ID: " + id));

        if (receiptRepository.countByPaymentMethodId(id) > 0) {
            throw new BadRequestException("No se puede eliminar el método de pago porque tiene comprobantes de pago asociados");
        }

        repository.delete(method);
    }

    @Override
    public List<PaymentMethodResponse> findAllResponses() {
        return mapper.toResponseList(repository.list("order by name asc"));
    }

    @Override
    public PaymentMethodResponse findResponseById(Long id) {
        PaymentMethod method = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Método de pago no encontrado con ID: " + id));
        return mapper.toResponse(method);
    }

    @Override
    @Transactional
    public PaymentMethodResponse create(CreatePaymentMethodRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un método de pago con el nombre: " + request.name());
        }
        PaymentMethod entity = mapper.toEntity(request);
        entity.setName(request.name().trim());
        repository.persist(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public PaymentMethodResponse update(Long id, UpdatePaymentMethodRequest request) {
        PaymentMethod existing = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Método de pago no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe un método de pago con el nombre: " + request.name());
        }

        mapper.updateEntityFromRequest(existing, request);
        existing.setName(request.name().trim());
        return mapper.toResponse(existing);
    }
}