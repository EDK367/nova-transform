package org.nova;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.repository.RoleRepository;
import org.nova.repository.UserRepository;

import java.util.UUID;

@ApplicationScoped
public class UserTransactionRollbackHelper {

    @Inject
    UserRepository userRepository;

    @Inject
    RoleRepository roleRepository;

    @Transactional
    public void executeFailingTransaction(String email, String username) {
        Role role = roleRepository.findByName("CLIENTE").orElseThrow();

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Rollback");
        user.setLastName("Test");
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword("hashedsecret");
        user.setActive(true);
        user.setRole(role);

        userRepository.persist(user);

        throw new IllegalStateException("Simulated exception during member creation flow");
    }
}
