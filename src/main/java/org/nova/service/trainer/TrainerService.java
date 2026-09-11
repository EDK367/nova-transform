package org.nova.service.trainer;

import org.nova.domain.model.trainer.Trainer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerService {

    List<Trainer> findAll();

    Optional<Trainer> findById(UUID id);

    Trainer create(Trainer trainer);

    Trainer update(UUID id, Trainer trainer);

    void delete(UUID id);
}