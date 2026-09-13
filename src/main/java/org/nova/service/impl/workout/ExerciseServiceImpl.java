package org.nova.service.impl.workout;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.workout.Exercise;
import org.nova.repository.ExerciseRepository;
import org.nova.service.workout.ExerciseService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository repository;

    @Override
    public List<Exercise> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Exercise> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Exercise create(Exercise exercise) {
        repository.persist(exercise);
        return exercise;
    }

    @Override
    @Transactional
    public Exercise update(Long id, Exercise exercise) {
        Exercise existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(exercise.getName());
        existing.setDescription(exercise.getDescription());
        existing.setMuscleGroup(exercise.getMuscleGroup());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}