package org.nova.service.gymclass;

import org.nova.domain.model.gymclass.Waitlist;

import java.util.List;
import java.util.Optional;

public interface WaitlistService {
    List<Waitlist> findAll();

    Optional<Waitlist> findById(Long id);

    Waitlist create(Waitlist waitlist);

    Waitlist update(Long id, Waitlist waitlist);

    void delete(Long id);
}
