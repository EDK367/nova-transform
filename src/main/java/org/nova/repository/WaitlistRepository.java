package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.Waitlist;

@ApplicationScoped
public class WaitlistRepository implements PanacheRepositoryBase<Waitlist, Long> {
}