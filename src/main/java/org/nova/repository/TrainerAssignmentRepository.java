package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.TrainerAssignment;

@ApplicationScoped
public class TrainerAssignmentRepository implements PanacheRepositoryBase<TrainerAssignment, Long> {
}