package org.nova.service.staff;

import org.nova.domain.model.staff.Receptionist;
import org.nova.web.dto.request.staff.CreateReceptionistRequest;
import org.nova.web.dto.request.staff.UpdateReceptionistRequest;
import org.nova.web.dto.response.staff.ReceptionistResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReceptionistService {

    List<Receptionist> findAll();

    List<ReceptionistResponse> findAllResponses();

    Optional<Receptionist> findById(UUID id);

    ReceptionistResponse findResponseById(UUID id);

    Receptionist create(Receptionist receptionist);

    ReceptionistResponse createReceptionist(CreateReceptionistRequest request);

    Receptionist update(UUID id, Receptionist receptionist);

    ReceptionistResponse updateReceptionist(UUID id, UpdateReceptionistRequest request);

    void delete(UUID id);

    void deleteReceptionist(UUID id);
}
