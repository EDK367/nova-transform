package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.TrainerAssignment;
import org.nova.repository.TrainerAssignmentRepository;
import org.nova.service.trainer.TrainerAssignmentService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerAssignmentServiceImpl implements TrainerAssignmentService {

    private final TrainerAssignmentRepository repository;

    @Override
    public List<TrainerAssignment> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<TrainerAssignment> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public TrainerAssignment create(TrainerAssignment trainerAssignment) {
        repository.persist(trainerAssignment);
        return trainerAssignment;
    }

    @Override
    @Transactional
    public TrainerAssignment update(Long id, TrainerAssignment trainerAssignment) {
        TrainerAssignment existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(trainerAssignment.getIdMember());
        existing.setIdTrainer(trainerAssignment.getIdTrainer());
        existing.setStartDate(trainerAssignment.getStartDate());
        existing.setEndDate(trainerAssignment.getEndDate());
        existing.setReason(trainerAssignment.getReason());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}