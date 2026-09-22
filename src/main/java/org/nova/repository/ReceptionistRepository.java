package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.staff.Receptionist;

import java.util.UUID;

@ApplicationScoped
public class ReceptionistRepository implements PanacheRepositoryBase<Receptionist, UUID> {
}