package org.nova.service.impl.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.user.UserToken;
import org.nova.repository.UserTokenRepository;
import org.nova.service.user.UserTokenService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class UserTokenServiceImpl implements UserTokenService {

    private final UserTokenRepository repository;

    @Override
    public List<UserToken> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<UserToken> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public UserToken create(UserToken userToken) {
        repository.persist(userToken);
        return userToken;
    }

    @Override
    @Transactional
    public UserToken update(Long id, UserToken userToken) {
        UserToken existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdUser(userToken.getIdUser());
        existing.setToken(userToken.getToken());
        existing.setType(userToken.getType());
        existing.setGenerationDate(userToken.getGenerationDate());
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