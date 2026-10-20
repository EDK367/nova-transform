package org.nova.service.trainer;

import org.nova.domain.model.trainer.TrainerSpecialization;
import org.nova.web.dto.request.trainer.AssignTrainerSpecializationRequest;
import org.nova.web.dto.response.trainer.TrainerSpecializationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerSpecializationService {

    List<TrainerSpecialization> findAll();

    List<TrainerSpecializationResponse> findAllResponses(UUID trainerId);

    Optional<TrainerSpecialization> findById(Long id);

    TrainerSpecializationResponse findResponseById(Long id);

    TrainerSpecialization create(TrainerSpecialization trainerSpecialization);

    TrainerSpecializationResponse assignSpecialization(AssignTrainerSpecializationRequest request);

    TrainerSpecialization update(Long id, TrainerSpecialization trainerSpecialization);

    void delete(Long id);

    void deleteTrainerSpecialization(Long id);
}
