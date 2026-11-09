package org.nova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.membership.MembershipHistory;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.repository.MemberRepository;
import org.nova.repository.MembershipFreezeRepository;
import org.nova.repository.MembershipHistoryRepository;
import org.nova.repository.MembershipPlanRepository;
import org.nova.repository.MembershipRepository;
import org.nova.service.impl.membership.MembershipServiceImpl;
import org.nova.web.dto.request.membership.CancelMembershipRequest;
import org.nova.web.dto.request.membership.ChangePlanRequest;
import org.nova.web.dto.request.membership.CreateMembershipRequest;
import org.nova.web.dto.response.membership.MembershipResponse;
import org.nova.web.mapper.MembershipFreezeMapper;
import org.nova.web.mapper.MembershipHistoryMapper;
import org.nova.web.mapper.MembershipMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MembershipServiceUnitTest {

    @Mock
    private MembershipRepository repository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @Mock
    private MembershipHistoryRepository historyRepository;

    @Mock
    private MembershipFreezeRepository freezeRepository;

    @Mock
    private MembershipMapper mapper;

    @Mock
    private MembershipHistoryMapper historyMapper;

    @Mock
    private MembershipFreezeMapper freezeMapper;

    @InjectMocks
    private MembershipServiceImpl membershipService;

    private Member sampleMember;
    private User sampleUser;
    private MembershipPlan samplePlan;
    private UUID memberId;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();

        sampleUser = new User();
        sampleUser.setId(UUID.randomUUID());
        sampleUser.setActive(true);

        sampleMember = new Member();
        sampleMember.setId(memberId);
        sampleMember.setUsers(sampleUser);

        samplePlan = new MembershipPlan();
        samplePlan.setId(10L);
        samplePlan.setName("Plan Trimestral");
        samplePlan.setDuration(90L);
        samplePlan.setPrice(BigDecimal.valueOf(150.00));
    }

    @Test
    void subscribeMember_Success_CalculatesDueDateAndPersists() {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        CreateMembershipRequest request = new CreateMembershipRequest(memberId, 10L, startDate);

        when(memberRepository.findByIdWithUser(memberId)).thenReturn(Optional.of(sampleMember));
        when(repository.findActiveByMemberId(memberId)).thenReturn(Optional.empty());
        when(planRepository.findByIdOptional(10L)).thenReturn(Optional.of(samplePlan));

        MembershipResponse expectedResponse = new MembershipResponse(
                1L, memberId, "Juan Perez", "juan@test.com",
                10L, "Plan Trimestral", 90L, BigDecimal.valueOf(150.00),
                MembershipStatus.ACTIVE, startDate, startDate.plusDays(90), Instant.now()
        );
        when(mapper.toResponse(any(Membership.class))).thenReturn(expectedResponse);

        MembershipResponse response = membershipService.subscribeMember(request);

        assertNotNull(response);
        assertEquals(MembershipStatus.ACTIVE, response.status());
        assertEquals(startDate.plusDays(90), response.dueDate());
        verify(repository, times(1)).persist(any(Membership.class));
        verify(historyRepository, times(1)).persist(any(MembershipHistory.class));
    }

    @Test
    void subscribeMember_WhenUserInactive_ThrowsBadRequestException() {
        sampleUser.setActive(false);
        CreateMembershipRequest request = new CreateMembershipRequest(memberId, 10L, LocalDate.now());
        when(memberRepository.findByIdWithUser(memberId)).thenReturn(Optional.of(sampleMember));

        assertThrows(BadRequestException.class, () -> membershipService.subscribeMember(request));
        verify(repository, never()).persist(any(Membership.class));
    }

    @Test
    void subscribeMember_WhenAlreadyHasActiveMembership_ThrowsConflictException() {
        Membership activeMembership = new Membership();
        activeMembership.setId(99L);
        activeMembership.setStatus(MembershipStatus.ACTIVE);

        CreateMembershipRequest request = new CreateMembershipRequest(memberId, 10L, LocalDate.now());
        when(memberRepository.findByIdWithUser(memberId)).thenReturn(Optional.of(sampleMember));
        when(repository.findActiveByMemberId(memberId)).thenReturn(Optional.of(activeMembership));

        assertThrows(ConflictException.class, () -> membershipService.subscribeMember(request));
        verify(repository, never()).persist(any(Membership.class));
    }

    @Test
    void changePlan_WhenMembershipCancelled_ThrowsBadRequestException() {
        Membership cancelledMembership = new Membership();
        cancelledMembership.setId(1L);
        cancelledMembership.setStatus(MembershipStatus.CANCELLED);

        ChangePlanRequest request = new ChangePlanRequest(20L, "Upgrade");
        when(repository.findByIdWithDetails(1L)).thenReturn(Optional.of(cancelledMembership));

        assertThrows(BadRequestException.class, () -> membershipService.changePlan(1L, request));
        verify(planRepository, never()).findByIdOptional(any());
    }

    @Test
    void cancelMembership_WhenAlreadyCancelled_ThrowsBadRequestException() {
        Membership cancelledMembership = new Membership();
        cancelledMembership.setId(1L);
        cancelledMembership.setStatus(MembershipStatus.CANCELLED);

        CancelMembershipRequest request = new CancelMembershipRequest("Ya no asistirá");
        when(repository.findByIdWithDetails(1L)).thenReturn(Optional.of(cancelledMembership));

        assertThrows(BadRequestException.class, () -> membershipService.cancelMembership(1L, request));
        verify(historyRepository, never()).persist(any(MembershipHistory.class));
    }
}
