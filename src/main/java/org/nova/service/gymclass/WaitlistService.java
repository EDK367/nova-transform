package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.Waitlist;
import org.nova.web.dto.response.gymclass.WaitlistResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WaitlistService {

    List<Waitlist> findAll();

    Optional<Waitlist> findById(Long id);

    Waitlist create(Waitlist waitlist);

    Waitlist update(Long id, Waitlist waitlist);

    void delete(Long id);

    List<WaitlistResponse> findByMemberId(UUID memberId);

    List<WaitlistResponse> findByClassId(Long classId);

    WaitlistResponse findResponseById(Long id);

    void removeFromWaitlist(Long id, String authenticatedUsername);
}
