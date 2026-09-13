package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.Promotion;
import org.nova.repository.PromotionRepository;
import org.nova.service.membership.PromotionService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository repository;

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

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}