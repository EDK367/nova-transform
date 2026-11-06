package org.nova.service.auth;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.nova.domain.model.user.Permission;
import org.nova.domain.model.user.User;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
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

        String roleName = user.getRole().getName();
        List<String> permissionCodes = user.getRole().getPermissions() != null
                ? user.getRole().getPermissions().stream().map(Permission::getCode).sorted().toList()
                : List.of();

        Set<String> groups = new HashSet<>();
        groups.add(roleName);
        groups.addAll(permissionCodes);

        if ("SUPER_ADMIN".equalsIgnoreCase(roleName)) {
            groups.add("SUPER_ADMIN");
            groups.add("ADMIN");
            groups.add("RECEPCIONISTA");
            groups.add("ENTRENADOR");
            groups.add("CLIENTE");
            groups.add("USER");
        }

        return Jwt.issuer(issuer)
                .subject(user.getId().toString())
                .upn(user.getUsername())
                .groups(groups)
                .claim("role", roleName)
                .claim("permissions", permissionCodes)
                .claim("email", user.getEmail())
                .claim("name", user.getFirstName())
                .claim("lastName", user.getLastName())
                .issuedAt(now)
                .expiresAt(expiration)
                .sign();
    }
}
