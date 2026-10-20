package org.nova.service.trainer;

import org.nova.domain.model.trainer.Trainer;
import org.nova.web.dto.request.trainer.CreateTrainerRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerRequest;
import org.nova.web.dto.response.trainer.TrainerResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerService {

    List<Trainer> findAll();

    List<TrainerResponse> findAllResponses();

    Optional<Trainer> findById(UUID id);

    TrainerResponse findResponseById(UUID id);

    Trainer create(Trainer trainer);

    TrainerResponse createTrainer(CreateTrainerRequest request);

    Trainer update(UUID id, Trainer trainer);

    TrainerResponse updateTrainer(UUID id, UpdateTrainerRequest request);

    void delete(UUID id);

    void deleteTrainer(UUID id);
}
