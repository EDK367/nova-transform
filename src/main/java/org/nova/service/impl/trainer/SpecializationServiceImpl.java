package org.nova.service.impl.trainer;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.trainer.Specialization;
import org.nova.repository.SpecializationRepository;
import org.nova.service.trainer.SpecializationService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;

    @Override
    public List<Specialization> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Specialization> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Specialization create(Specialization specialization) {
        repository.persist(specialization);
        return specialization;
    }

    @Override
    @Transactional
    public Specialization update(Long id, Specialization specialization) {
        Specialization existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(specialization.getName());
        existing.setDescription(specialization.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}