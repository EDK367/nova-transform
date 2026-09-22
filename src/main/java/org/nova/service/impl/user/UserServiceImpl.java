package org.nova.service.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.User;
import org.nova.repository.UserRepository;
import org.nova.service.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public List<User> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public User create(User user) {
        repository.persist(user);
        return user;
    }

    @Override
    @Transactional
    public User update(UUID id, User user) {
        User existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setUsername(user.getUsername());
        existing.setTwoFactorAuth(user.isTwoFactorAuth());
        existing.setRole(user.getRole());
        existing.setBranch(user.getBranch());

        return existing;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}