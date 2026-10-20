package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.member.PhysicalMeasurement;
import org.nova.exception.NotFoundException;
import org.nova.repository.MemberRepository;
import org.nova.repository.PhysicalMeasurementRepository;
import org.nova.service.member.PhysicalMeasurementService;
import org.nova.web.dto.request.member.CreatePhysicalMeasurementRequest;
import org.nova.web.dto.request.member.UpdatePhysicalMeasurementRequest;
import org.nova.web.dto.response.member.PhysicalMeasurementResponse;
import org.nova.web.mapper.PhysicalMeasurementMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class PhysicalMeasurementServiceImpl implements PhysicalMeasurementService {

    private final PhysicalMeasurementRepository repository;
    private final MemberRepository memberRepository;
    private final PhysicalMeasurementMapper mapper;

    @Override
    public List<PhysicalMeasurement> findAll() {
        return repository.listAllWithDetails();
    }

    @Override
    public List<PhysicalMeasurementResponse> findAllResponses(UUID memberId) {
        List<PhysicalMeasurement> list = memberId != null
                ? repository.findByMemberId(memberId)
                : repository.listAllWithDetails();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<PhysicalMeasurement> findById(Long id) {
        return repository.findByIdWithDetails(id);
    }

    @Override
    public PhysicalMeasurementResponse findResponseById(Long id) {
        PhysicalMeasurement pm = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Medicion fisica no encontrada con ID: " + id));
        return mapper.toResponse(pm);
    }

    @Override
    @Transactional
    public PhysicalMeasurement create(PhysicalMeasurement physicalMeasurement) {
        repository.persist(physicalMeasurement);
        return physicalMeasurement;
    }

    @Override
    @Transactional
    public PhysicalMeasurementResponse createMeasurement(CreatePhysicalMeasurementRequest request) {
        Member member = memberRepository.findByIdWithUser(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        PhysicalMeasurement pm = new PhysicalMeasurement();
        pm.setIdMember(member);
        pm.setDate(request.date() != null ? request.date() : LocalDate.now());
        pm.setWeight(request.weight());
        pm.setHeight(request.height());
        pm.setBodyFatPercentage(request.bodyFatPercentage());
        pm.setMuscleMass(request.muscleMass());
        pm.setWaist(request.waist());
        pm.setArm(request.arm());
        pm.setLeg(request.leg());
        pm.setObservations(request.observations() != null ? request.observations().trim() : null);

        repository.persist(pm);
        return mapper.toResponse(pm);
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
    public PhysicalMeasurementResponse updateMeasurement(Long id, UpdatePhysicalMeasurementRequest request) {
        PhysicalMeasurement pm = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Medicion fisica no encontrada con ID: " + id));

        if (request.date() != null) {
            pm.setDate(request.date());
        }
        if (request.weight() != null) {
            pm.setWeight(request.weight());
        }
        if (request.height() != null) {
            pm.setHeight(request.height());
        }
        if (request.bodyFatPercentage() != null) {
            pm.setBodyFatPercentage(request.bodyFatPercentage());
        }
        if (request.muscleMass() != null) {
            pm.setMuscleMass(request.muscleMass());
        }
        if (request.waist() != null) {
            pm.setWaist(request.waist());
        }
        if (request.arm() != null) {
            pm.setArm(request.arm());
        }
        if (request.leg() != null) {
            pm.setLeg(request.leg());
        }
        if (request.observations() != null) {
            pm.setObservations(request.observations().trim());
        }

        return mapper.toResponse(pm);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteMeasurement(Long id) {
        PhysicalMeasurement pm = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Medicion fisica no encontrada con ID: " + id));
        repository.delete(pm);
    }
}
