package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.enums.ClassStatus;
import org.nova.domain.enums.EnrollmentStatus;
import org.nova.domain.model.gymclass.Class;
import org.nova.domain.model.gymclass.ClassEnrollment;
import org.nova.domain.model.gymclass.Waitlist;
import org.nova.domain.model.member.Member;
import org.nova.domain.model.user.User;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.*;
import org.nova.service.gymclass.ClassEnrollmentService;
import org.nova.web.dto.request.gymclass.CreateEnrollmentRequest;
import org.nova.web.dto.response.gymclass.ClassEnrollmentResponse;
import org.nova.web.mapper.ClassEnrollmentMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {

    private final ClassEnrollmentRepository repository;
    private final ClassRepository classRepository;
    private final MemberRepository memberRepository;
    private final MembershipRepository membershipRepository;
    private final WaitlistRepository waitlistRepository;
    private final UserRepository userRepository;
    private final ClassEnrollmentMapper mapper;

    @Override
    public List<ClassEnrollment> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<ClassEnrollment> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassEnrollment create(ClassEnrollment classEnrollment) {
        repository.persist(classEnrollment);
        return classEnrollment;
    }

    @Override
    @Transactional
    public ClassEnrollment update(Long id, ClassEnrollment classEnrollment) {
        ClassEnrollment existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdClass(classEnrollment.getIdClass());
        existing.setIdMember(classEnrollment.getIdMember());
        existing.setStatus(classEnrollment.getStatus());
        existing.setEnrollmentDate(classEnrollment.getEnrollmentDate());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public ClassEnrollmentResponse enroll(CreateEnrollmentRequest request, String authenticatedUsername) {
        Member member;
        if (request.memberId() != null) {
            member = memberRepository.findByIdOptional(request.memberId())
                    .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + request.memberId()));
        } else {
            User user = userRepository.findByUsername(authenticatedUsername)
                    .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + authenticatedUsername));
            member = memberRepository.findByIdOptional(user.getId())
                    .orElseThrow(() -> new BadRequestException("El usuario autenticado no tiene un perfil de socio"));
        }

        // Active membership check
        membershipRepository.findActiveByMemberId(member.getId())
                .orElseThrow(() -> new BadRequestException("El socio no cuenta con una membresía activa para inscribirse a clases"));

        Class c = classRepository.findByIdWithDetails(request.classId())
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + request.classId()));

        if (c.getStatus() != ClassStatus.SCHEDULED) {
            throw new BadRequestException("No se pueden realizar inscripciones en una clase con estado: " + c.getStatus());
        }

        // Double enrollment check
        Optional<ClassEnrollment> existingEnrollment = repository.findActiveEnrollment(c.getId(), member.getId());
        if (existingEnrollment.isPresent()) {
            throw new ConflictException("El socio ya se encuentra inscrito en esta clase");
        }
        if (waitlistRepository.existsByClassIdAndMemberId(c.getId(), member.getId())) {
            throw new ConflictException("El socio ya se encuentra en la lista de espera de esta clase");
        }

        // Capacity check
        long currentEnrolled = repository.countEnrolledByClassId(c.getId());
        if (currentEnrolled >= c.getMaxCapacity()) {
            // Full -> Add to waitlist and register enrollment with WAITLISTED
            Waitlist waitlist = new Waitlist();
            waitlist.setIdClass(c);
            waitlist.setIdMember(member);
            waitlist.setRegistrationDate(Instant.now());
            waitlist.setNotified(false);
            waitlistRepository.persist(waitlist);

            Optional<ClassEnrollment> previousEnrollment = repository.findByClassIdAndMemberId(c.getId(), member.getId());
            ClassEnrollment enrollment;
            if (previousEnrollment.isPresent()) {
                enrollment = previousEnrollment.get();
                enrollment.setStatus(EnrollmentStatus.WAITLISTED);
                enrollment.setEnrollmentDate(Instant.now());
            } else {
                enrollment = new ClassEnrollment();
                enrollment.setIdClass(c);
                enrollment.setIdMember(member);
                enrollment.setStatus(EnrollmentStatus.WAITLISTED);
                enrollment.setEnrollmentDate(Instant.now());
                repository.persist(enrollment);
            }
            return mapper.toResponse(enrollment);
        }

        // Available spot -> ENROLLED
        Optional<ClassEnrollment> previousEnrollment = repository.findByClassIdAndMemberId(c.getId(), member.getId());
        ClassEnrollment enrollment;
        if (previousEnrollment.isPresent()) {
            enrollment = previousEnrollment.get();
            enrollment.setStatus(EnrollmentStatus.ENROLLED);
            enrollment.setEnrollmentDate(Instant.now());
        } else {
            enrollment = new ClassEnrollment();
            enrollment.setIdClass(c);
            enrollment.setIdMember(member);
            enrollment.setStatus(EnrollmentStatus.ENROLLED);
            enrollment.setEnrollmentDate(Instant.now());
            repository.persist(enrollment);
        }

        return mapper.toResponse(enrollment);
    }

    @Override
    @Transactional
    public ClassEnrollmentResponse cancelEnrollment(Long id, String authenticatedUsername) {
        ClassEnrollment enrollment = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada con ID: " + id));

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new BadRequestException("La inscripción ya se encuentra cancelada");
        }

        boolean wasEnrolled = enrollment.getStatus() == EnrollmentStatus.ENROLLED;
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        repository.persist(enrollment);

        // Also clean up waitlist if member was on waitlist
        waitlistRepository.deleteByClassIdAndMemberId(enrollment.getIdClass().getId(), enrollment.getIdMember().getId());

        // If the cancelled spot was ENROLLED, promote first member in waitlist
        if (wasEnrolled) {
            Optional<Waitlist> firstWaiting = waitlistRepository.findFirstInWaitlist(enrollment.getIdClass().getId());
            if (firstWaiting.isPresent()) {
                Waitlist waiting = firstWaiting.get();
                waitlistRepository.delete(waiting);

                Optional<ClassEnrollment> waitingEnrollment = repository.findByClassIdAndMemberId(
                        enrollment.getIdClass().getId(), waiting.getIdMember().getId()
                );
                if (waitingEnrollment.isPresent()) {
                    waitingEnrollment.get().setStatus(EnrollmentStatus.ENROLLED);
                } else {
                    ClassEnrollment promoted = new ClassEnrollment();
                    promoted.setIdClass(enrollment.getIdClass());
                    promoted.setIdMember(waiting.getIdMember());
                    promoted.setStatus(EnrollmentStatus.ENROLLED);
                    promoted.setEnrollmentDate(Instant.now());
                    repository.persist(promoted);
                }
            }
        }

        return mapper.toResponse(enrollment);
    }

    @Override
    public List<ClassEnrollmentResponse> findByMemberId(UUID memberId) {
        memberRepository.findByIdOptional(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));
        return mapper.toResponseList(repository.findByMemberId(memberId));
    }

    @Override
    public List<ClassEnrollmentResponse> findByClassId(Long classId) {
        classRepository.findByIdOptional(classId)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + classId));
        return mapper.toResponseList(repository.findByClassId(classId));
    }

    @Override
    public ClassEnrollmentResponse findResponseById(Long id) {
        ClassEnrollment enrollment = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada con ID: " + id));
        return mapper.toResponse(enrollment);
    }
}