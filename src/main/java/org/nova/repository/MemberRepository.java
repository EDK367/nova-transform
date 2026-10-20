package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MemberRepository implements PanacheRepositoryBase<Member, UUID> {

    public List<Member> findAllWithUser() {
        return find("select m from Member m join fetch m.users u join fetch u.role").list();
    }

    public PanacheQuery<Member> findPagedWithUser(String sortBy, String sortDirection) {
        String direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) ? "desc" : "asc";
        String orderClause = (sortBy != null && sortBy.equalsIgnoreCase("email"))
                ? " order by u.email " + direction
                : " order by u.firstName " + direction + ", u.lastName " + direction;
        return find("select m from Member m join fetch m.users u join fetch u.role" + orderClause);
    }

    public PanacheQuery<Member> findPagedWithUser() {
        return findPagedWithUser("name", "asc");
    }

    public Optional<Member> findByIdWithUser(UUID id) {
        return find("select m from Member m join fetch m.users u join fetch u.role where m.id = ?1", id).firstResultOptional();
    }
}