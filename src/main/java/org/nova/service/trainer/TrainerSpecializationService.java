package org.nova.service.trainer;

import org.nova.domain.model.trainer.TrainerSpecialization;

import java.util.List;
import java.util.Optional;

public interface TrainerSpecializationService {

    List<TrainerSpecialization> findAll();

    Optional<TrainerSpecialization> findById(Long id);

    TrainerSpecialization create(TrainerSpecialization trainerSpecialization);

    TrainerSpecialization update(Long id, TrainerSpecialization trainerSpecialization);

    void delete(Long id);
}