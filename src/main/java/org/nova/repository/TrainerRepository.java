package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.trainer.Trainer;

import java.util.UUID;

@ApplicationScoped
public class TrainerRepository implements PanacheRepositoryBase<Trainer, UUID> {
}