package org.nova.service.membership;

import org.nova.domain.model.membership.MembershipFreeze;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;

import java.util.List;
import java.util.Optional;

public interface MembershipFreezeService {

    List<MembershipFreeze> findAll();

    List<MembershipFreezeResponse> findAllResponses(Long membershipId);

    Optional<MembershipFreeze> findById(Long id);

    MembershipFreezeResponse findResponseById(Long id);

    MembershipFreeze create(MembershipFreeze membershipFreeze);

    MembershipFreeze update(Long id, MembershipFreeze membershipFreeze);

    void delete(Long id);
}
