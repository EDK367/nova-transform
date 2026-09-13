package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.Routine;
import org.nova.repository.RoutineRepository;
import org.nova.service.workout.RoutineService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository repository;

    @Override
    public List<Routine> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Routine> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Routine create(Routine routine) {
        repository.persist(routine);
        return routine;
    }

    @Override
    @Transactional
    public Routine update(Long id, Routine routine) {
        Routine existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdAssignment(routine.getIdAssignment());
        existing.setName(routine.getName());
        existing.setDescription(routine.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}