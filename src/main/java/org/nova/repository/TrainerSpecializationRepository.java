package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.TrainerSpecialization;

@ApplicationScoped
public class TrainerSpecializationRepository implements PanacheRepositoryBase<TrainerSpecialization, Long> {
}