package org.nova.service.impl.membership;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.enums.PromotionDiscountType;
import org.nova.domain.model.membership.Promotion;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.PromotionRepository;
import org.nova.service.membership.PromotionService;
import org.nova.web.dto.request.membership.CreatePromotionRequest;
import org.nova.web.dto.request.membership.UpdatePromotionRequest;
import org.nova.web.dto.response.membership.PromotionResponse;
import org.nova.web.mapper.PromotionMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository repository;
    private final PromotionMapper mapper;

    @Override
    public List<Promotion> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Promotion> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Promotion create(Promotion promotion) {
        repository.persist(promotion);
        return promotion;
    }

    @Override
    @Transactional
    public Promotion update(Long id, Promotion promotion) {
        Promotion existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(promotion.getName());
        existing.setDescription(promotion.getDescription());
        existing.setDiscountType(promotion.getDiscountType());
        existing.setDiscountValue(promotion.getDiscountValue());
        existing.setStartDate(promotion.getStartDate());
        existing.setEndDate(promotion.getEndDate());
        existing.setActive(promotion.getActive());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<PromotionResponse> findAllResponses(Boolean onlyActive) {
        if (Boolean.TRUE.equals(onlyActive)) {
            return mapper.toResponseList(repository.listActive(LocalDate.now()));
        }
        return mapper.toResponseList(repository.listAll(Sort.by("startDate", Sort.Direction.Descending)));
    }

    @Override
    public PromotionResponse findResponseById(Long id) {
        Promotion promotion = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));
        return mapper.toResponse(promotion);
    }

    @Override
    @Transactional
    public PromotionResponse createPromotion(CreatePromotionRequest request) {
        validatePromotionDatesAndDiscount(request.startDate(), request.endDate(), request.discountType(), request.discountValue());

        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe una promoción con el nombre: " + request.name().trim());
        }

        Promotion promotion = new Promotion();
        promotion.setName(request.name().trim());
        promotion.setDescription(request.description() != null ? request.description().trim() : null);
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        promotion.setActive(request.active() != null ? request.active() : true);

        repository.persist(promotion);
        return mapper.toResponse(promotion);
    }

    @Override
    @Transactional
    public PromotionResponse updatePromotion(Long id, UpdatePromotionRequest request) {
        Promotion promotion = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));

        validatePromotionDatesAndDiscount(request.startDate(), request.endDate(), request.discountType(), request.discountValue());

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otra promoción con el nombre: " + request.name().trim());
        }

        promotion.setName(request.name().trim());
        promotion.setDescription(request.description() != null ? request.description().trim() : null);
        promotion.setDiscountType(request.discountType());
        promotion.setDiscountValue(request.discountValue());
        promotion.setStartDate(request.startDate());
        promotion.setEndDate(request.endDate());
        if (request.active() != null) {
            promotion.setActive(request.active());
        }

        return mapper.toResponse(promotion);
    }

    @Override
    @Transactional
    public void deletePromotion(Long id) {
        Promotion promotion = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Promoción no encontrada con ID: " + id));
        repository.delete(promotion);
    }

    private void validatePromotionDatesAndDiscount(LocalDate start, LocalDate end, PromotionDiscountType type, BigDecimal value) {
        if (start.isAfter(end)) {
            throw new BadRequestException("La fecha de inicio de la promoción no puede ser posterior a la fecha de fin");
        }
        if (type == PromotionDiscountType.PERCENTAGE && value.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BadRequestException("El porcentaje de descuento no puede ser mayor al 100%");
        }
    }
}