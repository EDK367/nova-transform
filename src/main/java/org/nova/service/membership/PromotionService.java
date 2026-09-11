package org.nova.service.membership;

import org.nova.domain.model.membership.Promotion;

import java.util.List;
import java.util.Optional;

public interface PromotionService {

    List<Promotion> findAll();

    Optional<Promotion> findById(Long id);

    Promotion create(Promotion promotion);

    Promotion update(Long id, Promotion promotion);

    void delete(Long id);
}