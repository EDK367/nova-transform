package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.TrainerSpecialization;
import org.nova.repository.TrainerSpecializationRepository;
import org.nova.service.trainer.TrainerSpecializationService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerSpecializationServiceImpl implements TrainerSpecializationService {

    private final TrainerSpecializationRepository repository;

    @Override
    public List<TrainerSpecialization> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<TrainerSpecialization> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public TrainerSpecialization create(TrainerSpecialization trainerSpecialization) {
        repository.persist(trainerSpecialization);
        return trainerSpecialization;
    }

    @Override
    @Transactional
    public TrainerSpecialization update(Long id, TrainerSpecialization trainerSpecialization) {
        TrainerSpecialization existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdTrainer(trainerSpecialization.getIdTrainer());
        existing.setIdSpecialization(trainerSpecialization.getIdSpecialization());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}