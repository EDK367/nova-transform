package org.nova.service.impl.membership;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.enums.MembershipStatus;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.membership.Membership;
import org.nova.domain.model.membership.MembershipFreeze;
import org.nova.domain.model.membership.MembershipHistory;
import org.nova.domain.model.membership.MembershipPlan;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.MemberRepository;
import org.nova.repository.MembershipFreezeRepository;
import org.nova.repository.MembershipHistoryRepository;
import org.nova.repository.MembershipPlanRepository;
import org.nova.repository.MembershipRepository;
import org.nova.service.membership.MembershipService;
import org.nova.web.dto.request.membership.CancelMembershipRequest;
import org.nova.web.dto.request.membership.ChangePlanRequest;
import org.nova.web.dto.request.membership.CreateMembershipRequest;
import org.nova.web.dto.request.membership.FreezeMembershipRequest;
import org.nova.web.dto.response.membership.MembershipFreezeResponse;
import org.nova.web.dto.response.membership.MembershipHistoryResponse;
import org.nova.web.dto.response.membership.MembershipResponse;
import org.nova.web.mapper.MembershipFreezeMapper;
import org.nova.web.mapper.MembershipHistoryMapper;
import org.nova.web.mapper.MembershipMapper;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class MembershipServiceImpl implements MembershipService {

    private final MembershipRepository repository;
    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final MembershipHistoryRepository historyRepository;
    private final MembershipFreezeRepository freezeRepository;
    private final MembershipMapper mapper;
    private final MembershipHistoryMapper historyMapper;
    private final MembershipFreezeMapper freezeMapper;

    @Override
    public List<Membership> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Membership> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Membership create(Membership membership) {
        repository.persist(membership);
        return membership;
    }

    @Override
    @Transactional
    public Membership update(Long id, Membership membership) {
        Membership existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdMember(membership.getIdMember());
        existing.setIdPlan(membership.getIdPlan());
        existing.setStatus(membership.getStatus());
        existing.setStartDate(membership.getStartDate());
        existing.setDueDate(membership.getDueDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public PageResponse<MembershipResponse> findPaged(
            int page,
            int size,
            MembershipStatus status,
            UUID memberId,
            String sortBy,
            String sortDirection
    ) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc"))
                ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "createdAt";
        Sort sort = Sort.by(property, direction);

        PanacheQuery<Membership> query = repository.findFiltered(status, memberId, sort)
                .page(Page.of(page, size));

        List<MembershipResponse> content = mapper.toResponseList(query.list());
        long totalElements = query.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public MembershipResponse findResponseById(Long id) {
        Membership membership = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + id));
        return mapper.toResponse(membership);
    }

    @Override
    public List<MembershipResponse> findByMemberId(UUID memberId) {
        memberRepository.findByIdOptional(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));
        return mapper.toResponseList(repository.findByMemberId(memberId));
    }

    @Override
    public Optional<MembershipResponse> findActiveByMemberId(UUID memberId) {
        return repository.findActiveByMemberId(memberId).map(mapper::toResponse);
    }

    @Override
    @Transactional
    public MembershipResponse subscribeMember(CreateMembershipRequest request) {
        Member member = memberRepository.findByIdWithUser(request.memberId())
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));

        if (member.getUsers() != null && !member.getUsers().isActive()) {
            throw new BadRequestException("El usuario asociado al socio se encuentra inactivo");
        }

        // Verificar si ya tiene membresía activa o congelada
        Optional<Membership> activeMembership = repository.findActiveByMemberId(request.memberId());
        if (activeMembership.isPresent()) {
            throw new ConflictException("El socio ya cuenta con una membresía activa (ID: " + activeMembership.get().getId() + ")");
        }

        MembershipPlan plan = planRepository.findByIdOptional(request.planId())
                .orElseThrow(() -> new NotFoundException("Plan de membresía no encontrado con ID: " + request.planId()));

        LocalDate startDate = request.startDate() != null ? request.startDate() : LocalDate.now();
        LocalDate dueDate = startDate.plusDays(plan.getDuration());

        Membership membership = new Membership();
        membership.setIdMember(member);
        membership.setIdPlan(plan);
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setStartDate(startDate);
        membership.setDueDate(dueDate);

        repository.persist(membership);

        // Registrar en historial
        MembershipHistory history = new MembershipHistory();
        history.setIdMembership(membership);
        history.setPreviousStatus(MembershipStatus.INACTIVE);
        history.setNewStatus(MembershipStatus.ACTIVE);
        history.setObservation("Alta de membresía - Plan: " + plan.getName() + " (" + plan.getDuration() + " días)");
        historyRepository.persist(history);

        return mapper.toResponse(membership);
    }

    @Override
    @Transactional
    public MembershipResponse changePlan(Long membershipId, ChangePlanRequest request) {
        Membership membership = repository.findByIdWithDetails(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));

        if (membership.getStatus() == MembershipStatus.CANCELLED || membership.getStatus() == MembershipStatus.EXPIRED) {
            throw new BadRequestException("No se puede cambiar el plan de una membresía en estado: " + membership.getStatus());
        }

        MembershipPlan newPlan = planRepository.findByIdOptional(request.newPlanId())
                .orElseThrow(() -> new NotFoundException("Nuevo plan no encontrado con ID: " + request.newPlanId()));

        if (membership.getIdPlan().getId().equals(newPlan.getId())) {
            throw new BadRequestException("La membresía ya tiene asignado el plan: " + newPlan.getName());
        }

        String oldPlanName = membership.getIdPlan().getName();
        membership.setIdPlan(newPlan);

        // Recalcular la fecha de vencimiento a partir de la fecha de inicio según la nueva duración
        membership.setDueDate(membership.getStartDate().plusDays(newPlan.getDuration()));

        // Registrar en historial
        MembershipHistory history = new MembershipHistory();
        history.setIdMembership(membership);
        history.setPreviousStatus(membership.getStatus());
        history.setNewStatus(membership.getStatus());
        String reasonStr = (request.reason() != null && !request.reason().isBlank()) ? " | Motivo: " + request.reason().trim() : "";
        history.setObservation("Cambio de plan de '" + oldPlanName + "' a '" + newPlan.getName() + "'" + reasonStr);
        historyRepository.persist(history);

        return mapper.toResponse(membership);
    }

    @Override
    @Transactional
    public MembershipResponse cancelMembership(Long membershipId, CancelMembershipRequest request) {
        Membership membership = repository.findByIdWithDetails(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));

        if (membership.getStatus() == MembershipStatus.CANCELLED) {
            throw new BadRequestException("La membresía ya se encuentra cancelada");
        }

        MembershipStatus previousStatus = membership.getStatus();
        membership.setStatus(MembershipStatus.CANCELLED);

        // Registrar en historial
        MembershipHistory history = new MembershipHistory();
        history.setIdMembership(membership);
        history.setPreviousStatus(previousStatus);
        history.setNewStatus(MembershipStatus.CANCELLED);
        String reasonStr = (request != null && request.reason() != null && !request.reason().isBlank())
                ? " | Motivo: " + request.reason().trim()
                : "";
        history.setObservation("Cancelación de suscripción por ADMIN" + reasonStr);
        historyRepository.persist(history);

        return mapper.toResponse(membership);
    }

    @Override
    @Transactional
    public MembershipResponse freezeMembership(Long membershipId, FreezeMembershipRequest request) {
        Membership membership = repository.findByIdWithDetails(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));

        if (membership.getStatus() != MembershipStatus.ACTIVE) {
            throw new BadRequestException("Solo se pueden congelar membresías con estado ACTIVE. Estado actual: " + membership.getStatus());
        }

        if (request.startDate().isAfter(request.endDate())) {
            throw new BadRequestException("La fecha de inicio de congelamiento no puede ser posterior a la fecha de fin");
        }

        if (request.startDate().isAfter(membership.getDueDate())) {
            throw new BadRequestException("La fecha de congelamiento no puede ser posterior a la fecha de vencimiento de la membresía (" + membership.getDueDate() + ")");
        }

        MembershipFreeze freeze = new MembershipFreeze();
        freeze.setIdMembership(membership);
        freeze.setStartDate(request.startDate());
        freeze.setEndDate(request.endDate());
        freeze.setReason(request.reason() != null ? request.reason().trim() : null);
        freezeRepository.persist(freeze);

        membership.setStatus(MembershipStatus.FROZEN);

        // Registrar en historial
        MembershipHistory history = new MembershipHistory();
        history.setIdMembership(membership);
        history.setPreviousStatus(MembershipStatus.ACTIVE);
        history.setNewStatus(MembershipStatus.FROZEN);
        String reasonStr = (request.reason() != null && !request.reason().isBlank()) ? " | Motivo: " + request.reason().trim() : "";
        history.setObservation("Congelamiento temporal de membresía del " + request.startDate() + " al " + request.endDate() + reasonStr);
        historyRepository.persist(history);

        return mapper.toResponse(membership);
    }

    @Override
    @Transactional
    public MembershipResponse unfreezeMembership(Long membershipId) {
        Membership membership = repository.findByIdWithDetails(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));

        if (membership.getStatus() != MembershipStatus.FROZEN) {
            throw new BadRequestException("Solo se pueden descongelar membresías con estado FROZEN. Estado actual: " + membership.getStatus());
        }

        // Buscar el último congelamiento para compensar los días
        Optional<MembershipFreeze> latestFreeze = freezeRepository.findLatestFreeze(membershipId);
        if (latestFreeze.isPresent()) {
            MembershipFreeze freeze = latestFreeze.get();
            LocalDate effectiveEnd = LocalDate.now().isBefore(freeze.getEndDate()) ? LocalDate.now() : freeze.getEndDate();
            long daysFrozen = ChronoUnit.DAYS.between(freeze.getStartDate(), effectiveEnd);
            if (daysFrozen > 0) {
                membership.setDueDate(membership.getDueDate().plusDays(daysFrozen));
            }
        }

        membership.setStatus(MembershipStatus.ACTIVE);

        // Registrar en historial
        MembershipHistory history = new MembershipHistory();
        history.setIdMembership(membership);
        history.setPreviousStatus(MembershipStatus.FROZEN);
        history.setNewStatus(MembershipStatus.ACTIVE);
        history.setObservation("Descongelamiento y reactivación de membresía");
        historyRepository.persist(history);

        return mapper.toResponse(membership);
    }

    @Override
    public List<MembershipHistoryResponse> getMembershipHistory(Long membershipId) {
        repository.findByIdOptional(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));
        return historyMapper.toResponseList(historyRepository.findByMembershipId(membershipId));
    }

    @Override
    public List<MembershipFreezeResponse> getMembershipFreezes(Long membershipId) {
        repository.findByIdOptional(membershipId)
                .orElseThrow(() -> new NotFoundException("Membresía no encontrada con ID: " + membershipId));
        return freezeMapper.toResponseList(freezeRepository.findByMembershipId(membershipId));
    }
}