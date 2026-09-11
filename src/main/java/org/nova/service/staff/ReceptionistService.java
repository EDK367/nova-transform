package org.nova.service.staff;

import org.nova.domain.model.staff.Receptionist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceptionistService {

    List<Receptionist> findAll();

    Optional<Receptionist> findById(UUID id);

    Receptionist create(Receptionist receptionist);

    Receptionist update(UUID id, Receptionist receptionist);

    void delete(UUID id);
}