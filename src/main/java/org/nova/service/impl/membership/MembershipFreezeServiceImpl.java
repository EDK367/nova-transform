package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.MembershipFreeze;
import org.nova.exception.NotFoundException;
import org.nova.repository.MembershipFreezeRepository;
import org.nova.service.membership.MembershipFreezeService;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;
import org.nova.web.mapper.MembershipFreezeMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipFreezeServiceImpl implements MembershipFreezeService {

    private final MembershipFreezeRepository repository;
    private final MembershipFreezeMapper mapper;

    @Override
    public List<MembershipFreeze> findAll() {
        return repository.listAll();
    }

    @Override
    public List<MembershipFreezeResponse> findAllResponses(Long membershipId) {
        List<MembershipFreeze> list = membershipId != null
                ? repository.findByMembershipId(membershipId)
                : repository.listAll();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<MembershipFreeze> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public MembershipFreezeResponse findResponseById(Long id) {
        MembershipFreeze freeze = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Congelamiento no encontrado con ID: " + id));
        return mapper.toResponse(freeze);
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
