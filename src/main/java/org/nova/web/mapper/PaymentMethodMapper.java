package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.payment.PaymentMethod;
import org.nova.web.dto.request.payment.CreatePaymentMethodRequest;
import org.nova.web.dto.request.payment.UpdatePaymentMethodRequest;
import org.nova.web.dto.response.payment.PaymentMethodResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMethodMapper {

    PaymentMethodResponse toResponse(PaymentMethod paymentMethod);

    List<PaymentMethodResponse> toResponseList(List<PaymentMethod> paymentMethods);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PaymentMethod toEntity(CreatePaymentMethodRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(@MappingTarget PaymentMethod entity, UpdatePaymentMethodRequest request);
}
