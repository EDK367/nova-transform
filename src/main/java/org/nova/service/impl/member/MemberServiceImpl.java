package org.nova.service.impl.member;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.member.Member;
import org.nova.repository.MemberRepository;
import org.nova.service.member.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public List<Member> findAll() {
        return memberRepository.listAll();
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return memberRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Member create(Member member) {
        memberRepository.persist(member);
        return member;
    }

    @Override
    @Transactional
    public Member update(UUID id, Member member) {
        Member existing = memberRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setRegistrationDate(member.getRegistrationDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        memberRepository.deleteById(id);
    }
}