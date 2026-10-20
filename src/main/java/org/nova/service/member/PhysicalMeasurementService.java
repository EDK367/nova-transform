package org.nova.service.member;

import org.nova.domain.model.member.PhysicalMeasurement;
import org.nova.web.dto.request.member.CreatePhysicalMeasurementRequest;
import org.nova.web.dto.request.member.UpdatePhysicalMeasurementRequest;
import org.nova.web.dto.response.member.PhysicalMeasurementResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PhysicalMeasurementService {

    List<PhysicalMeasurement> findAll();

    List<PhysicalMeasurementResponse> findAllResponses(UUID memberId);

    Optional<PhysicalMeasurement> findById(Long id);

    PhysicalMeasurementResponse findResponseById(Long id);

    PhysicalMeasurement create(PhysicalMeasurement physicalMeasurement);

    PhysicalMeasurementResponse createMeasurement(CreatePhysicalMeasurementRequest request);

    PhysicalMeasurement update(Long id, PhysicalMeasurement physicalMeasurement);

    PhysicalMeasurementResponse updateMeasurement(Long id, UpdatePhysicalMeasurementRequest request);

    void delete(Long id);

    void deleteMeasurement(Long id);
}
