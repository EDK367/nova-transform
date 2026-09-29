package org.nova.service.impl.payment;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.payment.PaymentMethod;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.exception.BadRequestException;
import org.nova.exception.NotFoundException;
import org.nova.repository.InvoiceRepository;
import org.nova.repository.PaymentMethodRepository;
import org.nova.repository.PaymentReceiptRepository;
import org.nova.service.payment.PaymentReceiptService;
import org.nova.web.dto.request.payment.CreatePaymentReceiptRequest;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;
import org.nova.web.mapper.PaymentReceiptMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PaymentReceiptServiceImpl implements PaymentReceiptService {

    private final PaymentReceiptRepository repository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentReceiptMapper mapper;

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

    @Override
    @Transactional
    public PaymentReceiptResponse createReceipt(CreatePaymentReceiptRequest request) {
        Invoice invoice = invoiceRepository.findByIdWithDetails(request.invoiceId())
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con ID: " + request.invoiceId()));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BadRequestException("No se pueden registrar pagos para una factura cancelada");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("La factura ya se encuentra totalmente pagada");
        }
        if (invoice.getStatus() == InvoiceStatus.REFUNDED) {
            throw new BadRequestException("No se pueden registrar pagos para una factura reembolsada");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdOptional(request.paymentMethodId())
                .orElseThrow(() -> new NotFoundException("Método de pago no encontrado con ID: " + request.paymentMethodId()));

        BigDecimal totalPaidSoFar = repository.sumPaidByInvoiceId(invoice.getId());
        BigDecimal remainingBalance = invoice.getAmount().subtract(totalPaidSoFar);

        if (request.amountPaid().compareTo(remainingBalance) > 0) {
            throw new BadRequestException("El monto a pagar (" + request.amountPaid() + ") no puede exceder el saldo pendiente (" + remainingBalance + ")");
        }

        PaymentReceipt receipt = new PaymentReceipt();
        receipt.setIdInvoice(invoice);
        receipt.setIdPaymentMethod(paymentMethod);
        receipt.setAmountPaid(request.amountPaid());
        receipt.setReference(request.reference());
        receipt.setPaymentDate(request.paymentDate() != null ? request.paymentDate() : Instant.now());

        repository.persist(receipt);

        BigDecimal newTotalPaid = totalPaidSoFar.add(request.amountPaid());
        if (newTotalPaid.compareTo(invoice.getAmount()) >= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }
        invoiceRepository.persist(invoice);

        return mapper.toResponse(receipt);
    }

    @Override
    public PaymentReceiptResponse findResponseById(Long id) {
        PaymentReceipt receipt = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Comprobante de pago no encontrado con ID: " + id));
        return mapper.toResponse(receipt);
    }

    @Override
    public List<PaymentReceiptResponse> findByInvoiceId(Long invoiceId) {
        invoiceRepository.findByIdOptional(invoiceId)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con ID: " + invoiceId));
        return mapper.toResponseList(repository.findByInvoiceId(invoiceId));
    }

    @Override
    public PageResponse<PaymentReceiptResponse> findPaged(int page, int size, Long invoiceId, String sortBy, String sortDirection) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                ? Sort.Direction.Ascending
                : Sort.Direction.Descending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "paymentDate";
        if (!property.contains(".")) {
            property = "r." + property;
        }
        Sort sort = Sort.by(property, direction);

        PanacheQuery<PaymentReceipt> query = repository.findFiltered(invoiceId, sort)
                .page(Page.of(page, size));

        List<PaymentReceiptResponse> content = mapper.toResponseList(query.list());
        long totalElements = query.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }
}