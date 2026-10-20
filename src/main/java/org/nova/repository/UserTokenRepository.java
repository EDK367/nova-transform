package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.user.UserToken;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class UserTokenRepository implements PanacheRepositoryBase<UserToken, Long> {

    public Optional<UserToken> findByToken(String token) {
        return find("token", token).firstResultOptional();
    }

    public List<UserToken> findByUserId(UUID userId) {
        return find("idUser.id = ?1 order by generationDate desc", userId).list();
    }

    public List<UserToken> listAllWithUser() {
        return find("select ut from UserToken ut left join fetch ut.idUser u left join fetch u.role").list();
    }

    public Optional<UserToken> findByIdWithUser(Long id) {
        return find("select ut from UserToken ut left join fetch ut.idUser u left join fetch u.role where ut.id = ?1", id).firstResultOptional();
    }
}
