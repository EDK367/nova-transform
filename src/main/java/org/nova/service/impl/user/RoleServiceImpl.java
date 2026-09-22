package org.nova.service.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.Role;
import org.nova.repository.RoleRepository;
import org.nova.service.user.RoleService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Override
    public List<Role> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Role create(Role role) {
        repository.persist(role);
        return role;
    }

    @Override
    @Transactional
    public Role update(Long id, Role role) {
        Role existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setName(role.getName());
        existing.setDescription(role.getDescription());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}