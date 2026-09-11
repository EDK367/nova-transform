package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.ClassRating;

@ApplicationScoped
public class ClassRatingRepository implements PanacheRepositoryBase<ClassRating, Long> {
}
