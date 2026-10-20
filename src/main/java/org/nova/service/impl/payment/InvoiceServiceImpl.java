package org.nova.service.impl.payment;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.enums.InvoiceStatus;
import org.nova.domain.enums.PromotionDiscountType;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.domain.model.membership.Promotion;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.NotFoundException;
import org.nova.repository.*;
import org.nova.service.payment.InvoiceService;
import org.nova.web.dto.request.payment.CreateInvoiceRequest;
import org.nova.web.dto.response.payment.InvoiceResponse;
import org.nova.web.mapper.InvoiceMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final MembershipRepository membershipRepository;
    private final MemberRepository memberRepository;
    private final BranchRepository branchRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PromotionRepository promotionRepository;
    private final PaymentReceiptRepository paymentReceiptRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper mapper;

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

    @Override
    public PageResponse<InvoiceResponse> findPaged(
            int page,
            int size,
            InvoiceStatus status,
            Long branchId,
            UUID memberId,
            String sortBy,
            String sortDirection
    ) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                ? Sort.Direction.Ascending
                : Sort.Direction.Descending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        if (!property.contains(".")) {
            property = "i." + property;
        }
        Sort sort = Sort.by(property, direction);

        PanacheQuery<Invoice> query = repository.findFiltered(status, branchId, memberId, sort)
                .page(Page.of(page, size));

        List<InvoiceResponse> content = query.list().stream()
                .map(this::toResponseDto)
                .toList();

        long totalElements = query.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public List<InvoiceResponse> findByMemberId(UUID memberId) {
        memberRepository.findByIdOptional(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));
        return repository.findByMemberId(memberId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public List<InvoiceResponse> findByMembershipId(Long membershipId) {
        membershipRepository.findByIdOptional(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));
        return repository.findByMembershipId(membershipId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public InvoiceResponse findResponseById(Long id) {
        Invoice invoice = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con ID: " + id));
        return toResponseDto(invoice);
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(CreateInvoiceRequest request, String authenticatedUsername) {
        Membership membership = membershipRepository.findByIdWithDetails(request.membershipId())
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + request.membershipId()));

        Branch branch = branchRepository.findByIdOptional(request.branchId())
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
        if (!branch.isActive()) {
            throw new BadRequestException("La sucursal seleccionada no está activa");
        }

        Receptionist receptionist;
        if (request.receptionistId() != null) {
            receptionist = receptionistRepository.findByIdOptional(request.receptionistId())
                    .orElseThrow(() -> new NotFoundException("Recepcionista no encontrado con ID: " + request.receptionistId()));
        } else {
            User user = userRepository.findByUsername(authenticatedUsername)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + authenticatedUsername));
            receptionist = receptionistRepository.findByIdOptional(user.getId())
                    .orElseThrow(() -> new BadRequestException("Debe especificar un recepcionista válido para emitir la factura"));
        }

        MembershipPlan plan = membership.getIdPlan();
        BigDecimal originalAmount = plan != null ? plan.getPrice() : BigDecimal.ZERO;
        Promotion promotion = null;
        BigDecimal discountAmount = BigDecimal.ZERO;
        LocalDate issueDate = request.issueDate() != null ? request.issueDate() : LocalDate.now();

        if (request.promotionId() != null) {
            promotion = promotionRepository.findByIdOptional(request.promotionId())
                    .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + request.promotionId()));
            if (Boolean.FALSE.equals(promotion.getActive())) {
                throw new BadRequestException("La promoción seleccionada no está activa");
            }
            if (issueDate.isBefore(promotion.getStartDate()) || issueDate.isAfter(promotion.getEndDate())) {
                throw new BadRequestException("La promoción seleccionada no es aplicable para la fecha de emisión: " + issueDate);
            }
            if (promotion.getDiscountType() == PromotionDiscountType.PERCENTAGE) {
                discountAmount = originalAmount.multiply(promotion.getDiscountValue())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            } else {
                discountAmount = promotion.getDiscountValue();
            }
            if (discountAmount.compareTo(originalAmount) > 0) {
                discountAmount = originalAmount;
            }
        }

        BigDecimal invoiceAmount;
        if (request.amount() != null) {
            invoiceAmount = request.amount();
        } else {
            invoiceAmount = originalAmount.subtract(discountAmount);
            if (invoiceAmount.compareTo(BigDecimal.ZERO) < 0) {
                invoiceAmount = BigDecimal.ZERO;
            }
        }

        LocalDate dueDate = request.dueDate() != null
                ? request.dueDate()
                : (membership.getDueDate() != null ? membership.getDueDate() : issueDate.plusDays(30));

        if (dueDate.isBefore(issueDate)) {
            throw new BadRequestException("La fecha de vencimiento no puede ser anterior a la fecha de emisión");
        }

        Invoice invoice = new Invoice();
        invoice.setIdBranch(branch);
        invoice.setIdMembership(membership);
        invoice.setIdReceptionist(receptionist);
        invoice.setStatus(InvoiceStatus.PENDING);
        invoice.setIdPromotion(promotion);
        invoice.setAmount(invoiceAmount);
        invoice.setIssueDate(issueDate);
        invoice.setDueDate(dueDate);
        invoice.setObservation(request.observation());

        repository.persist(invoice);

        return mapper.toResponse(invoice, BigDecimal.ZERO, originalAmount, discountAmount);
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(Long id) {
        Invoice invoice = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Factura no encontrada con ID: " + id));

        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BadRequestException("La factura ya se encuentra cancelada");
        }
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BadRequestException("No se puede cancelar una factura que ya ha sido pagada");
        }

        BigDecimal totalPaid = paymentReceiptRepository.sumPaidByInvoiceId(id);
        if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("No se puede cancelar una factura que cuenta con comprobantes de pago registrados");
        }

        invoice.setStatus(InvoiceStatus.CANCELLED);
        repository.persist(invoice);

        return toResponseDto(invoice);
    }

    private InvoiceResponse toResponseDto(Invoice invoice) {
        BigDecimal amountPaid = paymentReceiptRepository.sumPaidByInvoiceId(invoice.getId());
        BigDecimal originalAmount = invoice.getIdMembership() != null && invoice.getIdMembership().getIdPlan() != null
                ? invoice.getIdMembership().getIdPlan().getPrice()
                : invoice.getAmount();
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (originalAmount != null && invoice.getAmount() != null && originalAmount.compareTo(invoice.getAmount()) > 0) {
            discountAmount = originalAmount.subtract(invoice.getAmount());
        }
        return mapper.toResponse(invoice, amountPaid, originalAmount, discountAmount);
    }
}