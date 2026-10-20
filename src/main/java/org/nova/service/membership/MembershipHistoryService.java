package org.nova.service.membership;

import org.nova.domain.model.membership.MembershipHistory;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;

import java.util.List;
import java.util.Optional;

public interface MembershipHistoryService {

    List<MembershipHistory> findAll();

    List<MembershipHistoryResponse> findAllResponses(Long membershipId);

    Optional<MembershipHistory> findById(Long id);

    MembershipHistoryResponse findResponseById(Long id);

    MembershipHistory create(MembershipHistory membershipHistory);

    MembershipHistory update(Long id, MembershipHistory membershipHistory);

    void delete(Long id);
}
