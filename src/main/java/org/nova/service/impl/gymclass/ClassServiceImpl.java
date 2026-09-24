package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.repository.ClassRepository;
import org.nova.service.gymclass.ClassService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository repository;

    @Override
    public List<org.nova.domain.model.gymclass.Class> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<org.nova.domain.model.gymclass.Class> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public org.nova.domain.model.gymclass.Class create(
            org.nova.domain.model.gymclass.Class gymClass) {

        repository.persist(gymClass);
        return gymClass;
    }

    @Override
    @Transactional
    public org.nova.domain.model.gymclass.Class update(
            Long id,
            org.nova.domain.model.gymclass.Class gymClass) {

        org.nova.domain.model.gymclass.Class existing =
                repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdBranch(gymClass.getIdBranch());
        existing.setIdClassType(gymClass.getIdClassType());
        existing.setIdTrainer(gymClass.getIdTrainer());
        existing.setStatus(gymClass.getStatus());
        existing.setSchedule(gymClass.getSchedule());
        existing.setDuration(gymClass.getDuration());
        existing.setMaxCapacity(gymClass.getMaxCapacity());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}