package org.nova.service.trainer;

import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.web.dto.request.trainer.CreateTrainerAssignmentRequest;
import org.nova.web.dto.request.trainer.UpdateTrainerAssignmentRequest;
import org.nova.web.dto.response.trainer.TrainerAssignmentResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TrainerAssignmentService {

    List<TrainerAssignment> findAll();

    List<TrainerAssignmentResponse> findAllResponses(UUID memberId, UUID trainerId, Boolean activeOnly);

    Optional<TrainerAssignment> findById(Long id);

    TrainerAssignmentResponse findResponseById(Long id);

    TrainerAssignment create(TrainerAssignment trainerAssignment);

    TrainerAssignmentResponse createAssignment(CreateTrainerAssignmentRequest request);

    TrainerAssignment update(Long id, TrainerAssignment trainerAssignment);

    TrainerAssignmentResponse updateAssignment(Long id, UpdateTrainerAssignmentRequest request);

    void delete(Long id);

    void deleteAssignment(Long id);
}
