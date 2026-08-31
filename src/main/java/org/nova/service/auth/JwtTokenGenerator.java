package org.nova.service.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.nova.domain.model.User;

import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class JwtTokenGenerator {

    @ConfigProperty(name = "jwt.duration", defaultValue = "3600")
    long durationSeconds;

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String generate(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(durationSeconds);

        return Jwt.issuer(issuer)
                .subject(user.getId().toString())
                .upn(user.getUsername())
                .groups(Set.of(user.getRole().getName()))
                .claim("firstName", user.getFirstName())
                .issuedAt(now)
                .expiresAt(expiration)
                .sign();
    }
}
