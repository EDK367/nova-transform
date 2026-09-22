package org.nova.service.user;

import org.nova.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(UUID id);

    User create(User user);

    User update(UUID id, User user);

    void delete(UUID id);
}