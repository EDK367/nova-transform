package org.nova.service.membership;

import org.nova.domain.model.membership.MembershipFreeze;

import java.util.List;
import java.util.Optional;

public interface MembershipFreezeService {

    List<MembershipFreeze> findAll();

    Optional<MembershipFreeze> findById(Long id);

    MembershipFreeze create(MembershipFreeze membershipFreeze);

    MembershipFreeze update(Long id, MembershipFreeze membershipFreeze);

    void delete(Long id);
}