package org.nova.service.impl.auth;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthenticationException;
import org.nova.repository.UserRepository;
import org.nova.service.auth.AuthService;
import org.nova.service.auth.JwtTokenGenerator;
import org.nova.web.dto.request.auth.LoginRequest;
import org.nova.web.dto.response.auth.AuthResponse;
import org.nova.web.dto.response.user.UserResponse;
import org.nova.web.mapper.UserMapper;

@ApplicationScoped
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtTokenGenerator tokenGenerator;

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = repository.findByUsername(request.username())
                .orElseThrow(() -> new AuthenticationException("Credenciales Invalidas"));

        if (!user.isActive()) throw new AuthenticationException("Credenciales Invalidas");


        if (!BcryptUtil.matches(request.password(), user.getPassword())) {
            throw new AuthenticationException("Credenciales Invalidas");
        }

        String token = tokenGenerator.generate(user);

        UserResponse userResponse = mapper.toResponse(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(userResponse)
                .build();
    }
}
