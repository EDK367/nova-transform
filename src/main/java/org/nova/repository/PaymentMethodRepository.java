package org.nova.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.nova.domain.model.payment.PaymentMethod;

@ApplicationScoped
public class PaymentMethodRepository implements PanacheRepositoryBase<PaymentMethod, Long> {
}