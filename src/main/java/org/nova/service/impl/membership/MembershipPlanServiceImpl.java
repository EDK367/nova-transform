package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.repository.MembershipPlanRepository;
import org.nova.service.membership.MembershipPlanService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipPlanServiceImpl implements MembershipPlanService {

    private final MembershipPlanRepository repository;

    @Override
    public List<MembershipPlan> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<MembershipPlan> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public MembershipPlan create(MembershipPlan membershipPlan) {
        repository.persist(membershipPlan);
        return membershipPlan;
    }

    @Override
    @Transactional
    public MembershipPlan update(Long id, MembershipPlan membershipPlan) {
        MembershipPlan existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(membershipPlan.getName());
        existing.setDuration(membershipPlan.getDuration());
        existing.setPrice(membershipPlan.getPrice());
        existing.setDescription(membershipPlan.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}