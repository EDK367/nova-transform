package org.nova.service.user;

import org.nova.domain.model.user.UserToken;
import org.nova.web.dto.request.user.CreateUserTokenRequest;
import org.nova.web.dto.response.user.UserTokenResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserTokenService {

    List<UserToken> findAll();

    List<UserTokenResponse> findAllResponses(UUID userId);

    Optional<UserToken> findById(Long id);

    UserTokenResponse findResponseById(Long id);

    UserTokenResponse findByTokenValue(String token);

    UserToken create(UserToken userToken);

    UserTokenResponse generateToken(CreateUserTokenRequest request);

    UserTokenResponse consumeToken(String token);

    UserToken update(Long id, UserToken userToken);

    void delete(Long id);
}
