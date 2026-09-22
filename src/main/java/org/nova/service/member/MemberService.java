package org.nova.service.member;

import org.nova.domain.model.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {
    List<Member> findAll();

    Optional<Member> findById(UUID id);

    Member create(Member member);

    Member update(UUID id, Member member);

    void delete(UUID id);
}
