package org.nova.service.trainer;

import org.nova.domain.model.trainer.Specialization;
import org.nova.web.dto.request.trainer.CreateSpecializationRequest;
import org.nova.web.dto.request.trainer.UpdateSpecializationRequest;
import org.nova.web.dto.response.trainer.SpecializationResponse;

import java.util.List;
import java.util.Optional;

public interface SpecializationService {

    List<Specialization> findAll();

    List<SpecializationResponse> findAllResponses();

    Optional<Specialization> findById(Long id);

    SpecializationResponse findResponseById(Long id);

    Specialization create(Specialization specialization);

    SpecializationResponse createSpecialization(CreateSpecializationRequest request);

    Specialization update(Long id, Specialization specialization);

    SpecializationResponse updateSpecialization(Long id, UpdateSpecializationRequest request);

    void delete(Long id);

    void deleteSpecialization(Long id);
}
