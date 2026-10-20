package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.payment.PaymentReceipt;
import org.nova.web.dto.response.payment.PaymentReceiptResponse;

import java.util.List;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentReceiptMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "invoiceId", source = "idInvoice.id")
    @Mapping(target = "paymentMethodId", source = "idPaymentMethod.id")
    @Mapping(target = "paymentMethodName", source = "idPaymentMethod.name")
    @Mapping(target = "paymentDate", source = "paymentDate")
    @Mapping(target = "amountPaid", source = "amountPaid")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    PaymentReceiptResponse toResponse(PaymentReceipt receipt);

    List<PaymentReceiptResponse> toResponseList(List<PaymentReceipt> receipts);
}
