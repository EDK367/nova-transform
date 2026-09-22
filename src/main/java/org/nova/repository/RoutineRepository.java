package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.workout.Routine;

@ApplicationScoped
public class RoutineRepository implements PanacheRepositoryBase<Routine, Long> {
}