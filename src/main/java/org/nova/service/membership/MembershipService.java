package org.nova.service.membership;

import org.nova.domain.model.membership.Membership;

import java.util.List;
import java.util.Optional;

public interface MembershipService {

    List<Membership> findAll();

    Optional<Membership> findById(Long id);

    Membership create(Membership membership);

    Membership update(Long id, Membership membership);

    void delete(Long id);
}