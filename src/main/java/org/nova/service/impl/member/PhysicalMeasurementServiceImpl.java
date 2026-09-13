package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.PhysicalMeasurement;
import org.nova.repository.PhysicalMeasurementRepository;
import org.nova.service.member.PhysicalMeasurementService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class PhysicalMeasurementServiceImpl implements PhysicalMeasurementService {

    private final PhysicalMeasurementRepository repository;

    @Override
    public List<PhysicalMeasurement> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<PhysicalMeasurement> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public PhysicalMeasurement create(PhysicalMeasurement physicalMeasurement) {
        repository.persist(physicalMeasurement);
        return physicalMeasurement;
    }

    @Override
    @Transactional
    public PhysicalMeasurement update(Long id, PhysicalMeasurement physicalMeasurement) {
        PhysicalMeasurement existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(physicalMeasurement.getIdMember());
        existing.setDate(physicalMeasurement.getDate());
        existing.setWeight(physicalMeasurement.getWeight());
        existing.setHeight(physicalMeasurement.getHeight());
        existing.setBodyFatPercentage(physicalMeasurement.getBodyFatPercentage());
        existing.setMuscleMass(physicalMeasurement.getMuscleMass());
        existing.setWaist(physicalMeasurement.getWaist());
        existing.setArm(physicalMeasurement.getArm());
        existing.setLeg(physicalMeasurement.getLeg());
        existing.setObservations(physicalMeasurement.getObservations());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}