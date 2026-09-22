package org.nova.service.member;

import org.nova.domain.model.member.PhysicalMeasurement;

import java.util.List;
import java.util.Optional;

public interface PhysicalMeasurementService {

    List<PhysicalMeasurement> findAll();

    Optional<PhysicalMeasurement> findById(Long id);

    PhysicalMeasurement create(PhysicalMeasurement physicalMeasurement);

    PhysicalMeasurement update(Long id, PhysicalMeasurement physicalMeasurement);

    void delete(Long id);
}