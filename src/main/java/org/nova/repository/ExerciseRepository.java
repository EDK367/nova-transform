package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.Exercise;

import java.util.List;

@ApplicationScoped
public class ExerciseRepository implements PanacheRepositoryBase<Exercise, Long> {

    public boolean existsByName(String name) {
        return count("LOWER(name) = ?1", name.toLowerCase()) > 0;
    }

    public boolean existsByNameAndIdNot(String name, Long id) {
        return count("LOWER(name) = ?1 and id != ?2", name.toLowerCase(), id) > 0;
    }

    public List<Exercise> findByMuscleGroup(String muscleGroup) {
        return list("LOWER(muscleGroup) = ?1", muscleGroup.toLowerCase());
    }
}
