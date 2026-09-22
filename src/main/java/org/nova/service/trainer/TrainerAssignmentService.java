package org.nova.service.trainer;

import org.nova.domain.model.trainer.TrainerAssignment;

import java.util.List;
import java.util.Optional;

public interface TrainerAssignmentService {

    List<TrainerAssignment> findAll();

    Optional<TrainerAssignment> findById(Long id);

    TrainerAssignment create(TrainerAssignment trainerAssignment);

    TrainerAssignment update(Long id, TrainerAssignment trainerAssignment);

    void delete(Long id);
}