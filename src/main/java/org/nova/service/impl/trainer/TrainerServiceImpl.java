package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.Trainer;
import org.nova.repository.TrainerRepository;
import org.nova.service.trainer.TrainerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository repository;

    @Override
    public List<Trainer> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Trainer> findById(UUID id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Trainer create(Trainer trainer) {
        repository.persist(trainer);
        return trainer;
    }

    @Override
    @Transactional
    public Trainer update(UUID id, Trainer trainer) {
        Trainer existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setHireDate(trainer.getHireDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}