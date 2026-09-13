package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.MemberGoal;
import org.nova.repository.MemberGoalRepository;
import org.nova.service.member.MemberGoalService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MemberGoalServiceImpl implements MemberGoalService {

    private final MemberGoalRepository repository;

    @Override
    public List<MemberGoal> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<MemberGoal> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public MemberGoal create(MemberGoal memberGoal) {
        repository.persist(memberGoal);
        return memberGoal;
    }

    @Override
    @Transactional
    public MemberGoal update(Long id, MemberGoal memberGoal) {
        MemberGoal existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(memberGoal.getIdMember());
        existing.setIdGoal(memberGoal.getIdGoal());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}