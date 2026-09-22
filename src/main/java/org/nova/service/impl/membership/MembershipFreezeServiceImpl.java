package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.MembershipFreeze;
import org.nova.repository.MembershipFreezeRepository;
import org.nova.service.membership.MembershipFreezeService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipFreezeServiceImpl implements MembershipFreezeService {

    private final MembershipFreezeRepository repository;

    @Override
    public List<MembershipFreeze> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<MembershipFreeze> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public MembershipFreeze create(MembershipFreeze membershipFreeze) {
        repository.persist(membershipFreeze);
        return membershipFreeze;
    }

    @Override
    @Transactional
    public MembershipFreeze update(Long id, MembershipFreeze membershipFreeze) {
        MembershipFreeze existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMembership(membershipFreeze.getIdMembership());
        existing.setStartDate(membershipFreeze.getStartDate());
        existing.setEndDate(membershipFreeze.getEndDate());
        existing.setReason(membershipFreeze.getReason());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}