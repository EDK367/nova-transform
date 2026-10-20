package org.nova.service.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.User;
import org.nova.domain.model.user.UserToken;
import org.nova.exception.BadRequestException;
import org.nova.exception.NotFoundException;
import org.nova.repository.UserRepository;
import org.nova.repository.UserTokenRepository;
import org.nova.service.user.UserTokenService;
import org.nova.web.dto.request.user.CreateUserTokenRequest;
import org.nova.web.dto.response.user.UserTokenResponse;
import org.nova.web.mapper.UserTokenMapper;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository repository;
    private final UserRepository userRepository;
    private final UserTokenMapper mapper;

    @Override
    public List<UserToken> findAll() {
        return repository.listAllWithUser();
    }

    @Override
    public List<UserTokenResponse> findAllResponses(UUID userId) {
        List<UserToken> list = userId != null
                ? repository.findByUserId(userId)
                : repository.listAllWithUser();
        return mapper.toResponseList(list);
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        return repository.findByIdWithUser(id);
    }

    @Override
    public UserTokenResponse findResponseById(Long id) {
        UserToken ut = repository.findByIdWithUser(id)
                .orElseThrow(() -> new NotFoundException("Token de usuario no encontrado con ID: " + id));
        return mapper.toResponse(ut);
    }

    @Override
    public UserTokenResponse findByTokenValue(String token) {
        UserToken ut = repository.findByToken(token.trim())
                .orElseThrow(() -> new NotFoundException("Token no encontrado o invalido"));
        return mapper.toResponse(ut);
    }

    @Override
    @Transactional
    public UserToken create(UserToken userToken) {
        repository.persist(userToken);
        return userToken;
    }

    @Override
    @Transactional
    public UserTokenResponse generateToken(CreateUserTokenRequest request) {
        User user = userRepository.findByIdOptional(request.userId())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado con ID: " + request.userId()));

        long hours = request.durationHours() != null && request.durationHours() > 0 ? request.durationHours() : 24L;

        UserToken ut = new UserToken();
        ut.setIdUser(user);
        ut.setToken("tok_" + UUID.randomUUID().toString().replace("-", ""));
        ut.setType(request.type());
        ut.setGenerationDate(Instant.now());
        ut.setExpirationDate(Instant.now().plus(hours, ChronoUnit.HOURS));
        ut.setUsed(false);

        repository.persist(ut);
        return mapper.toResponse(ut);
    }

    @Override
    @Transactional
    public UserTokenResponse consumeToken(String token) {
        UserToken ut = repository.findByToken(token.trim())
                .orElseThrow(() -> new NotFoundException("Token no encontrado"));

        if (ut.getUsed()) {
            throw new BadRequestException("El token ya ha sido utilizado");
        }

        if (Instant.now().isAfter(ut.getExpirationDate())) {
            throw new BadRequestException("El token ha expirado");
        }

        ut.setUsed(true);
        return mapper.toResponse(ut);
    }

    @Override
    @Transactional
    public UserToken update(Long id, UserToken userToken) {
        UserToken existing = repository.findById(id);
        if (existing == null) {
            return null;
        }
        existing.setToken(userToken.getToken());
        existing.setType(userToken.getType());
        existing.setExpirationDate(userToken.getExpirationDate());
        existing.setUsed(userToken.getUsed());
        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
