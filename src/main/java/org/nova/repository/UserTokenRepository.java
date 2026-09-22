package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.user.UserToken;

@ApplicationScoped
public class UserTokenRepository implements PanacheRepositoryBase<UserToken, Long> {
}