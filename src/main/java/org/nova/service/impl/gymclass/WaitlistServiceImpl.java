package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.Waitlist;
import org.nova.repository.WaitlistRepository;
import org.nova.service.gymclass.WaitlistService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository waitlistRepository;

    @Override
    public List<Waitlist> findAll() {
        return waitlistRepository.listAll();
    }

    @Override
    public Optional<Waitlist> findById(Long id) {
        return waitlistRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Waitlist create(Waitlist waitlist) {
        waitlistRepository.persist(waitlist);
        return waitlist;
    }

    @Override
    @Transactional
    public Waitlist update(Long id, Waitlist waitlist) {
        Waitlist existing = waitlistRepository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdClass(waitlist.getIdClass());
        existing.setIdMember(waitlist.getIdMember());
        existing.setRegistrationDate(waitlist.getRegistrationDate());
        existing.setNotified(waitlist.getNotified());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        waitlistRepository.deleteById(id);
    }
}