package org.nova.service.membership;

import org.nova.domain.model.membership.MembershipHistory;

import java.util.List;
import java.util.Optional;

public interface MembershipHistoryService {

    List<MembershipHistory> findAll();

    Optional<MembershipHistory> findById(Long id);

    MembershipHistory create(MembershipHistory membershipHistory);

    MembershipHistory update(Long id, MembershipHistory membershipHistory);

    void delete(Long id);
}