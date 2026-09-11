package org.nova.service.user;

import org.nova.domain.model.user.UserToken;

import java.util.List;
import java.util.Optional;

public interface UserTokenService {

    List<UserToken> findAll();

    Optional<UserToken> findById(Long id);

    UserToken create(UserToken userToken);

    UserToken update(Long id, UserToken userToken);

    void delete(Long id);
}