package org.nova.service.membership;

import org.nova.domain.model.membership.Benefit;
import org.nova.web.dto.request.membership.CreateBenefitRequest;
import org.nova.web.dto.request.membership.UpdateBenefitRequest;
import org.nova.web.dto.response.membership.BenefitResponse;

import java.util.List;
import java.util.Optional;

public interface BenefitService {

    List<Benefit> findAll();

    Optional<Benefit> findById(Long id);

    Benefit create(Benefit benefit);

    Benefit update(Long id, Benefit benefit);

    void delete(Long id);

    List<BenefitResponse> findAllResponses();

    BenefitResponse findResponseById(Long id);

    BenefitResponse createBenefit(CreateBenefitRequest request);

    BenefitResponse updateBenefit(Long id, UpdateBenefitRequest request);

    void deleteBenefit(Long id);
}