package org.nova.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.repository.AttendanceRepository;
import org.nova.repository.BranchRepository;
import org.nova.repository.MemberRepository;
import org.nova.repository.MembershipRepository;
import org.nova.repository.ReceptionistRepository;
import org.nova.repository.UserRepository;
import org.nova.service.impl.gymclass.AttendanceServiceImpl;
import org.nova.web.dto.request.gymclass.CreateAttendanceRequest;
import org.nova.web.dto.request.gymclass.UpdateAttendanceExitRequest;
import org.nova.web.dto.response.gymclass.AttendanceResponse;
import org.nova.web.mapper.AttendanceMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceUnitTest {

    @Mock
    private AttendanceRepository repository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MembershipRepository membershipRepository;

    @Mock
    private ReceptionistRepository receptionistRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AttendanceMapper mapper;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    private Branch sampleBranch;
    private Member sampleMember;
    private Receptionist sampleReceptionist;
    private UUID memberId;
    private UUID receptionistId;

    @BeforeEach
    void setUp() {
        memberId = UUID.randomUUID();
        receptionistId = UUID.randomUUID();

        sampleBranch = new Branch();
        sampleBranch.setId(1L);
        sampleBranch.setName("Sede Central");
        sampleBranch.setActive(true);

        User memberUser = new User();
        memberUser.setId(UUID.randomUUID());
        memberUser.setEmail("member@test.com");

        sampleMember = new Member();
        sampleMember.setId(memberId);
        sampleMember.setUsers(memberUser);

        User recepUser = new User();
        recepUser.setId(receptionistId);
        recepUser.setUsername("recep1");

        sampleReceptionist = new Receptionist();
        sampleReceptionist.setId(receptionistId);
        sampleReceptionist.setUsers(recepUser);
    }

    @Test
    void checkIn_Success_PersistsAttendance() {
        CreateAttendanceRequest request = new CreateAttendanceRequest(
                1L, memberId, receptionistId, LocalDate.of(2026, 3, 1),
                LocalTime.of(8, 0), null
        );

        when(branchRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleBranch));
        when(memberRepository.findByIdOptional(memberId)).thenReturn(Optional.of(sampleMember));
        when(membershipRepository.findActiveByMemberId(memberId)).thenReturn(Optional.of(new Membership()));
        when(receptionistRepository.findByIdOptional(receptionistId)).thenReturn(Optional.of(sampleReceptionist));

        AttendanceResponse expected = new AttendanceResponse(
                10L, 1L, "Sede Central", receptionistId, "recep1",
                memberId, "Carlos", "member@test.com",
                LocalDate.of(2026, 3, 1), LocalTime.of(8, 0), null,
                Instant.now(), Instant.now()
        );
        when(mapper.toResponse(any(Attendance.class))).thenReturn(expected);

        AttendanceResponse response = attendanceService.checkIn(request, "recep1");

        assertNotNull(response);
        verify(repository, times(1)).persist(any(Attendance.class));
    }

    @Test
    void checkIn_WhenBranchInactive_ThrowsBadRequestException() {
        sampleBranch.setActive(false);
        CreateAttendanceRequest request = new CreateAttendanceRequest(
                1L, memberId, receptionistId, LocalDate.now(), LocalTime.now(), null
        );

        when(branchRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleBranch));

        assertThrows(BadRequestException.class, () -> attendanceService.checkIn(request, "recep1"));
        verify(repository, never()).persist(any(Attendance.class));
    }

    @Test
    void checkIn_WhenMemberHasNoActiveMembership_ThrowsBadRequestException() {
        CreateAttendanceRequest request = new CreateAttendanceRequest(
                1L, memberId, receptionistId, LocalDate.now(), LocalTime.now(), null
        );

        when(branchRepository.findByIdOptional(1L)).thenReturn(Optional.of(sampleBranch));
        when(memberRepository.findByIdOptional(memberId)).thenReturn(Optional.of(sampleMember));
        when(membershipRepository.findActiveByMemberId(memberId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> attendanceService.checkIn(request, "recep1"));
        verify(repository, never()).persist(any(Attendance.class));
    }

    @Test
    void checkOut_Success_UpdatesExitTimeAndPersists() {
        Attendance existing = new Attendance();
        existing.setId(5L);
        existing.setEntryTime(LocalTime.of(7, 30));

        UpdateAttendanceExitRequest request = new UpdateAttendanceExitRequest(LocalTime.of(9, 0));

        when(repository.findByIdWithDetails(5L)).thenReturn(Optional.of(existing));

        AttendanceResponse expected = new AttendanceResponse(
                5L, 1L, "Sede Central", receptionistId, "recep1",
                memberId, "Carlos", "member@test.com",
                LocalDate.now(), LocalTime.of(7, 30), LocalTime.of(9, 0),
                Instant.now(), Instant.now()
        );
        when(mapper.toResponse(existing)).thenReturn(expected);

        AttendanceResponse response = attendanceService.checkOut(5L, request);

        assertNotNull(response);
        assertEquals(LocalTime.of(9, 0), existing.getExitTime());
        verify(repository, times(1)).persist(existing);
    }

    @Test
    void checkOut_WhenExitBeforeEntry_ThrowsBadRequestException() {
        Attendance existing = new Attendance();
        existing.setId(5L);
        existing.setEntryTime(LocalTime.of(10, 0));

        UpdateAttendanceExitRequest request = new UpdateAttendanceExitRequest(LocalTime.of(9, 0));

        when(repository.findByIdWithDetails(5L)).thenReturn(Optional.of(existing));

        assertThrows(BadRequestException.class, () -> attendanceService.checkOut(5L, request));
        verify(repository, never()).persist(existing);
    }
}
