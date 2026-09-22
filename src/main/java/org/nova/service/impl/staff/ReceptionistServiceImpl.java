package org.nova.service.impl.staff;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.staff.Receptionist;
import org.nova.repository.ReceptionistRepository;
import org.nova.service.staff.ReceptionistService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final ReceptionistRepository repository;

    @Override
    public List<Receptionist> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Receptionist> findById(UUID id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Receptionist create(Receptionist receptionist) {
        repository.persist(receptionist);
        return receptionist;
    }

    @Override
    @Transactional
    public Receptionist update(UUID id, Receptionist receptionist) {
        Receptionist existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setHireDate(receptionist.getHireDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}