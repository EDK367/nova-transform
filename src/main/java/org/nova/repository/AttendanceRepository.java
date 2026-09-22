package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.gymclass.Attendance;

@ApplicationScoped
public class AttendanceRepository implements PanacheRepositoryBase<Attendance, Long> {
}
