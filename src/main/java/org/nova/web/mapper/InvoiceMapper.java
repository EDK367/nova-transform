package org.nova.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.payment.Invoice;
import org.nova.domain.model.staff.Receptionist;
import org.nova.web.dto.response.payment.InvoiceResponse;

import java.math.BigDecimal;

@Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InvoiceMapper {

    @Mapping(target = "id", source = "invoice.id")
    @Mapping(target = "branchId", source = "invoice.idBranch.id")
    @Mapping(target = "branchName", source = "invoice.idBranch.name")
    @Mapping(target = "membershipId", source = "invoice.idMembership.id")
    @Mapping(target = "memberId", source = "invoice.idMembership.idMember.id")
    @Mapping(target = "memberName", source = "invoice.idMembership.idMember", qualifiedByName = "memberFullName")
    @Mapping(target = "memberEmail", source = "invoice.idMembership.idMember.users.email")
    @Mapping(target = "planId", source = "invoice.idMembership.idPlan.id")
    @Mapping(target = "planName", source = "invoice.idMembership.idPlan.name")
    @Mapping(target = "receptionistId", source = "invoice.idReceptionist.id")
    @Mapping(target = "receptionistName", source = "invoice.idReceptionist", qualifiedByName = "receptionistFullName")
    @Mapping(target = "status", source = "invoice.status")
    @Mapping(target = "promotionId", source = "invoice.idPromotion.id")
    @Mapping(target = "promotionName", source = "invoice.idPromotion.name")
    @Mapping(target = "originalAmount", source = "originalAmount")
    @Mapping(target = "discountAmount", source = "discountAmount")
    @Mapping(target = "amount", source = "invoice.amount")
    @Mapping(target = "amountPaid", source = "amountPaid")
    @Mapping(target = "balanceDue", expression = "java(calculateBalance(invoice.getAmount(), amountPaid))")
    @Mapping(target = "issueDate", source = "invoice.issueDate")
    @Mapping(target = "dueDate", source = "invoice.dueDate")
    @Mapping(target = "observation", source = "invoice.observation")
    @Mapping(target = "createdAt", source = "invoice.createdAt")
    @Mapping(target = "updatedAt", source = "invoice.updatedAt")
    InvoiceResponse toResponse(Invoice invoice, BigDecimal amountPaid, BigDecimal originalAmount, BigDecimal discountAmount);

    @Named("memberFullName")
    default String memberFullName(Member member) {
        if (member == null || member.getUsers() == null) {
            return null;
        }
        String first = member.getUsers().getFirstName() != null ? member.getUsers().getFirstName() : "";
        String last = member.getUsers().getLastName() != null ? member.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }

    @Named("receptionistFullName")
    default String receptionistFullName(Receptionist receptionist) {
        if (receptionist == null || receptionist.getUsers() == null) {
            return null;
        }
        String first = receptionist.getUsers().getFirstName() != null ? receptionist.getUsers().getFirstName() : "";
        String last = receptionist.getUsers().getLastName() != null ? receptionist.getUsers().getLastName() : "";
        return (first + " " + last).trim();
    }

    default BigDecimal calculateBalance(BigDecimal amount, BigDecimal amountPaid) {
        if (amount == null) return BigDecimal.ZERO;
        BigDecimal paid = amountPaid != null ? amountPaid : BigDecimal.ZERO;
        BigDecimal balance = amount.subtract(paid);
        return balance.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance;
    }
}
