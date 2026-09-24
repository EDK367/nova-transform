package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.Benefit;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.BenefitRepository;
import org.nova.service.membership.BenefitService;
import org.nova.web.dto.request.membership.CreateBenefitRequest;
import org.nova.web.dto.request.membership.UpdateBenefitRequest;
import org.nova.web.dto.response.membership.BenefitResponse;
import org.nova.web.mapper.BenefitMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final BenefitRepository repository;
    private final BenefitMapper mapper;

    @Override
    public List<Benefit> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Benefit> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Benefit create(Benefit benefit) {
        repository.persist(benefit);
        return benefit;
    }

    @Override
    @Transactional
    public Benefit update(Long id, Benefit benefit) {
        Benefit existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(benefit.getName());
        existing.setDescription(benefit.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<BenefitResponse> findAllResponses() {
        return mapper.toResponseList(repository.listAll());
    }

    @Override
    public BenefitResponse findResponseById(Long id) {
        Benefit benefit = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + id));
        return mapper.toResponse(benefit);
    }

    @Override
    @Transactional
    public BenefitResponse createBenefit(CreateBenefitRequest request) {
        if (repository.existsByName(request.name().trim())) {
            throw new ConflictException("Ya existe un beneficio con el nombre: " + request.name().trim());
        }

        Benefit benefit = new Benefit();
        benefit.setName(request.name().trim());
        benefit.setDescription(request.description() != null ? request.description().trim() : null);

        repository.persist(benefit);
        return mapper.toResponse(benefit);
    }

    @Override
    @Transactional
    public BenefitResponse updateBenefit(Long id, UpdateBenefitRequest request) {
        Benefit benefit = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + id));

        if (repository.existsByNameAndIdNot(request.name().trim(), id)) {
            throw new ConflictException("Ya existe otro beneficio con el nombre: " + request.name().trim());
        }

        benefit.setName(request.name().trim());
        benefit.setDescription(request.description() != null ? request.description().trim() : null);

        return mapper.toResponse(benefit);
    }

    @Override
    @Transactional
    public void deleteBenefit(Long id) {
        Benefit benefit = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Beneficio no encontrado con ID: " + id));
        repository.delete(benefit);
    }
}