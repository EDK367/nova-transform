package org.nova.service.impl.membership;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.membership.MembershipHistory;
import org.nova.exception.NotFoundException;
import org.nova.repository.MembershipHistoryRepository;
import org.nova.service.membership.MembershipHistoryService;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;
import org.nova.web.mapper.MembershipHistoryMapper;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipHistoryServiceImpl implements MembershipHistoryService {

    private final MembershipHistoryRepository repository;
    private final MembershipHistoryMapper mapper;

    @Override
    public List<MembershipHistory> findAll() {
        return repository.listAll();
    }

    @Override
    public List<MembershipHistoryResponse> findAllResponses(Long membershipId) {
        List<MembershipHistory> list = membershipId != null
                ? repository.findByMembershipId(membershipId)
                : repository.listAll();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<MembershipHistory> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    public MembershipHistoryResponse findResponseById(Long id) {
        MembershipHistory history = repository.findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Historial de membresia no encontrado con ID: " + id));
        return mapper.toResponse(history);
    }

    @Override
    @Transactional
    public MembershipHistory create(MembershipHistory membershipHistory) {
        repository.persist(membershipHistory);
        return membershipHistory;
    }

    @Override
    @Transactional
    public MembershipHistory update(Long id, MembershipHistory membershipHistory) {
        MembershipHistory existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setPreviousStatus(membershipHistory.getPreviousStatus());
        existing.setNewStatus(membershipHistory.getNewStatus());
        existing.setChangeDate(membershipHistory.getChangeDate());
        existing.setObservation(membershipHistory.getObservation());
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
