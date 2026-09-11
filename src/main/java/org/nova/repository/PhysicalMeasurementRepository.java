package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.member.PhysicalMeasurement;

@ApplicationScoped
public class PhysicalMeasurementRepository implements PanacheRepositoryBase<PhysicalMeasurement, Long> {
}