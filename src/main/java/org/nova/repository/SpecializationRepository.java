package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.Specialization;

@ApplicationScoped
public class SpecializationRepository implements PanacheRepositoryBase<Specialization, Long> {
}