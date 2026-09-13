package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.Membership;
import org.nova.repository.MembershipRepository;
import org.nova.service.membership.MembershipService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository repository;

    @Override
    public List<Membership> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Membership> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Membership create(Membership membership) {
        repository.persist(membership);
        return membership;
    }

    @Override
    @Transactional
    public Membership update(Long id, Membership membership) {
        Membership existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(membership.getIdMember());
        existing.setIdPlan(membership.getIdPlan());
        existing.setStatus(membership.getStatus());
        existing.setStartDate(membership.getStartDate());
        existing.setDueDate(membership.getDueDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}