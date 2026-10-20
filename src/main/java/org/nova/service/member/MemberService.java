package org.nova.service.member;

import org.nova.common.PageResponse;
import org.nova.domain.model.member.Member;
import org.nova.web.dto.request.member.CreateMemberRequest;
import org.nova.web.dto.response.member.CreateMemberResponse;
import org.nova.web.dto.response.member.MemberResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {
    List<Member> findAll();

    PageResponse<MemberResponse> findPaged(int page, int size);

    PageResponse<MemberResponse> findPaged(int page, int size, String sortBy, String sortDirection);

    Optional<Member> findById(UUID id);

    Member create(Member member);

    CreateMemberResponse createMember(CreateMemberRequest request);

    Member update(UUID id, Member member);

    void delete(UUID id);
}
