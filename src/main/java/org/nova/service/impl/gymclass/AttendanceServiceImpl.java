package org.nova.service.impl.gymclass;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.gymclass.Attendance;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.staff.Receptionist;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.NotFoundException;
import org.nova.repository.*;
import org.nova.service.gymclass.AttendanceService;
import org.nova.web.dto.request.gymclass.CreateAttendanceRequest;
import org.nova.web.dto.request.gymclass.UpdateAttendanceExitRequest;
import org.nova.web.dto.response.gymclass.AttendanceResponse;
import org.nova.web.mapper.AttendanceMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository repository;
    private final BranchRepository branchRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final ReceptionistRepository receptionistRepository;
    private final UserRepository userRepository;
    private final AttendanceMapper mapper;

    @Override
    public List<Attendance> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Attendance create(Attendance attendance) {
        repository.persist(attendance);
        return attendance;
    }

    @Override
    @Transactional
    public Attendance update(Long id, Attendance attendance) {
        Attendance existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdBranch(attendance.getIdBranch());
        existing.setIdReceptionist(attendance.getIdReceptionist());
        existing.setIdMember(attendance.getIdMember());
        existing.setDate(attendance.getDate());
        existing.setEntryTime(attendance.getEntryTime());
        existing.setExitTime(attendance.getExitTime());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public AttendanceResponse checkIn(CreateAttendanceRequest request, String authenticatedUsername) {
        Branch branch = branchRepository.findByIdOptional(request.branchId())
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
        if (!branch.isActive()) {
            throw new BadRequestException("La sucursal seleccionada no está activa");
        }

        Member member = memberRepository.findByIdOptional(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        membershipRepository.findActiveByMemberId(member.getId())
                .orElseThrow(() -> new BadRequestException("El socio no cuenta con una membresía activa para ingresar a las instalaciones"));

        Receptionist receptionist;
        if (request.receptionistId() != null) {
            receptionist = receptionistRepository.findByIdOptional(request.receptionistId())
                    .orElseThrow(() -> new NotFoundException("Recepcionista no encontrado con ID: " + request.receptionistId()));
        } else {
            User user = userRepository.findByUsername(authenticatedUsername)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + authenticatedUsername));
            receptionist = receptionistRepository.findByIdOptional(user.getId())
                    .orElseThrow(() -> new BadRequestException("Debe especificar un recepcionista válido para registrar la asistencia"));
        }

        Attendance attendance = new Attendance();
        attendance.setIdBranch(branch);
        attendance.setIdMember(member);
        attendance.setIdReceptionist(receptionist);
        attendance.setDate(request.date() != null ? request.date() : LocalDate.now());
        attendance.setEntryTime(request.entryTime() != null ? request.entryTime() : LocalTime.now());
        attendance.setExitTime(request.exitTime());

        repository.persist(attendance);
        return mapper.toResponse(attendance);
    }

    @Override
    @Transactional
    public AttendanceResponse checkOut(Long id, UpdateAttendanceExitRequest request) {
        Attendance attendance = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro de asistencia no encontrado con ID: " + id));

        LocalTime exitTime = (request != null && request.exitTime() != null) ? request.exitTime() : LocalTime.now();
        if (exitTime.isBefore(attendance.getEntryTime())) {
            throw new BadRequestException("La hora de salida no puede ser anterior a la hora de entrada");
        }

        attendance.setExitTime(exitTime);
        repository.persist(attendance);
        return mapper.toResponse(attendance);
    }

    @Override
    public PageResponse<AttendanceResponse> findPaged(
            int page,
            int size,
            Long branchId,
            UUID memberId,
            LocalDate date,
            String sortBy,
            String sortDirection
    ) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("asc"))
                ? Sort.Direction.Ascending
                : Sort.Direction.Descending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "date";
        if (!property.contains(".")) {
            property = "a." + property;
        }
        Sort sort = Sort.by(property, direction);

        PanacheQuery<Attendance> query = repository.findFiltered(branchId, memberId, date, sort)
                .page(Page.of(page, size));

        List<AttendanceResponse> content = mapper.toResponseList(query.list());
        long totalElements = query.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public List<AttendanceResponse> findByMemberId(UUID memberId) {
        memberRepository.findByIdOptional(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));
        return mapper.toResponseList(repository.findByMemberId(memberId));
    }

    @Override
    public AttendanceResponse findResponseById(Long id) {
        Attendance attendance = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro de asistencia no encontrado con ID: " + id));
        return mapper.toResponse(attendance);
    }
}