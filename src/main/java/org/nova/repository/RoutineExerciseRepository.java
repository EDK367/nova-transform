package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.RoutineExercise;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RoutineExerciseRepository implements PanacheRepositoryBase<RoutineExercise, Long> {

    public List<RoutineExercise> listAllWithDetails() {
        return find("select re from RoutineExercise re left join fetch re.idRoutine r left join fetch re.idExercise e").list();
    }

    public Optional<RoutineExercise> findByIdWithDetails(Long id) {
        return find("select re from RoutineExercise re left join fetch re.idRoutine r left join fetch re.idExercise e where re.id = ?1", id).firstResultOptional();
    }

    public List<RoutineExercise> findByRoutineId(Long routineId) {
        return find("select re from RoutineExercise re left join fetch re.idRoutine r left join fetch re.idExercise e where re.idRoutine.id = ?1", routineId).list();
    }

    public List<RoutineExercise> findByTrainingDay(String day) {
        return find("select re from RoutineExercise re left join fetch re.idRoutine r left join fetch re.idExercise e where UPPER(re.trainingDay) = ?1", day.toUpperCase()).list();
    }
}
