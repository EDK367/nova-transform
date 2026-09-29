package org.nova.service.membership;

import org.nova.common.PageResponse;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.membership.Membership;
import org.nova.web.dto.request.membership.CancelMembershipRequest;
import org.nova.web.dto.request.membership.ChangePlanRequest;
import org.nova.web.dto.request.membership.CreateMembershipRequest;
import org.nova.web.dto.request.membership.FreezeMembershipRequest;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;
import org.nova.web.dto.response.membership.MembershipResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipService {

    List<Membership> findAll();

    Optional<Membership> findById(Long id);

    Membership create(Membership membership);

    Membership update(Long id, Membership membership);

    void delete(Long id);

    PageResponse<MembershipResponse> findPaged(int page, int size, MembershipStatus status, UUID memberId, String sortBy, String sortDirection);

    MembershipResponse findResponseById(Long id);

    List<MembershipResponse> findByMemberId(UUID memberId);

    Optional<MembershipResponse> findActiveByMemberId(UUID memberId);

    MembershipResponse subscribeMember(CreateMembershipRequest request);

    MembershipResponse changePlan(Long membershipId, ChangePlanRequest request);

    MembershipResponse cancelMembership(Long membershipId, CancelMembershipRequest request);

    MembershipResponse freezeMembership(Long membershipId, FreezeMembershipRequest request);

    MembershipResponse unfreezeMembership(Long membershipId);

    List<MembershipHistoryResponse> getMembershipHistory(Long membershipId);

    List<MembershipFreezeResponse> getMembershipFreezes(Long membershipId);
}