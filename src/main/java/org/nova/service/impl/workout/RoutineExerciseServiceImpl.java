package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.RoutineExercise;
import org.nova.repository.RoutineExerciseRepository;
import org.nova.service.workout.RoutineExerciseService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class RoutineExerciseServiceImpl implements RoutineExerciseService {

    private final RoutineExerciseRepository repository;

    @Override
    public List<RoutineExercise> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<RoutineExercise> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public RoutineExercise create(RoutineExercise routineExercise) {
        repository.persist(routineExercise);
        return routineExercise;
    }

    @Override
    @Transactional
    public RoutineExercise update(Long id, RoutineExercise routineExercise) {
        RoutineExercise existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdRoutine(routineExercise.getIdRoutine());
        existing.setIdExercise(routineExercise.getIdExercise());
        existing.setTrainingDay(routineExercise.getTrainingDay());
        existing.setSets(routineExercise.getSets());
        existing.setReps(routineExercise.getReps());
        existing.setWeight(routineExercise.getWeight());
        existing.setRest(routineExercise.getRest());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}