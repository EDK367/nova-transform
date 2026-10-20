package org.nova.service.membership;

import org.nova.domain.model.membership.Promotion;
import org.nova.web.dto.request.membership.CreatePromotionRequest;
import org.nova.web.dto.request.membership.UpdatePromotionRequest;
import org.nova.web.dto.response.membership.PromotionResponse;

import java.util.List;
import java.util.Optional;

public interface PromotionService {

    List<Promotion> findAll();

    Optional<Promotion> findById(Long id);

    Promotion create(Promotion promotion);

    Promotion update(Long id, Promotion promotion);

    void delete(Long id);

    List<PromotionResponse> findAllResponses(Boolean onlyActive);

    PromotionResponse findResponseById(Long id);

    PromotionResponse createPromotion(CreatePromotionRequest request);

    PromotionResponse updatePromotion(Long id, UpdatePromotionRequest request);

    void deletePromotion(Long id);
}