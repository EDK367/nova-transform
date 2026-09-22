package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.Exercise;

@ApplicationScoped
public class ExerciseRepository implements PanacheRepositoryBase<Exercise, Long> {
}