package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.Benefit;
import org.nova.repository.BenefitRepository;
import org.nova.service.membership.BenefitService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class BenefitServiceImpl implements BenefitService {

    private final BenefitRepository repository;

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
}