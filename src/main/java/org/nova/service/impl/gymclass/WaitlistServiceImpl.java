package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.enums.EnrollmentStatus;
import org.nova.domain.model.gymclass.ClassEnrollment;
import org.nova.domain.model.gymclass.Waitlist;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.NotFoundException;
import org.nova.repository.ClassEnrollmentRepository;
import org.nova.repository.ClassRepository;
import org.nova.repository.MemberRepository;
import org.nova.repository.UserRepository;
import org.nova.repository.WaitlistRepository;
import org.nova.service.gymclass.WaitlistService;
import org.nova.web.dto.response.gymclass.WaitlistResponse;
import org.nova.web.mapper.WaitlistMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class WaitlistServiceImpl implements WaitlistService {

    private final WaitlistRepository repository;
    private final ClassRepository classRepository;
    private final MemberRepository memberRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final WaitlistMapper mapper;

    @Override
    public List<Waitlist> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Waitlist> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Waitlist create(Waitlist waitlist) {
        repository.persist(waitlist);
        return waitlist;
    }

    @Override
    @Transactional
    public Waitlist update(Long id, Waitlist waitlist) {
        Waitlist existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdClass(waitlist.getIdClass());
        existing.setIdMember(waitlist.getIdMember());
        existing.setRegistrationDate(waitlist.getRegistrationDate());
        existing.setNotified(waitlist.getNotified());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<WaitlistResponse> findByMemberId(UUID memberId) {
        memberRepository.findByIdOptional(memberId)
                .orElseThrow(() -> new NotFoundException("Socio no encontrado con ID: " + memberId));
        return mapper.toResponseList(repository.findByMemberId(memberId));
    }

    @Override
    public List<WaitlistResponse> findByClassId(Long classId) {
        classRepository.findByIdOptional(classId)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + classId));
        return mapper.toResponseList(repository.findByClassId(classId));
    }

    @Override
    public WaitlistResponse findResponseById(Long id) {
        Waitlist waitlist = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro en lista de espera no encontrado con ID: " + id));
        return mapper.toResponse(waitlist);
    }

    @Override
    @Transactional
    public void removeFromWaitlist(Long id, String authenticatedUsername) {
        Waitlist waitlist = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Registro en lista de espera no encontrado con ID: " + id));

        // If client, ensure ownership
        User user = userRepository.findByUsername(authenticatedUsername).orElse(null);
        if (user != null && user.getRole() != null && "CLIENTE".equalsIgnoreCase(user.getRole().getName())) {
            if (!user.getId().equals(waitlist.getIdMember().getId())) {
                throw new AuthorizationException("No tiene permisos para cancelar este registro de lista de espera");
            }
        }

        // Also update enrollment status if exists
        Optional<ClassEnrollment> enrollment = enrollmentRepository.findByClassIdAndMemberId(
                waitlist.getIdClass().getId(), waitlist.getIdMember().getId()
        );
        enrollment.ifPresent(e -> {
            if (e.getStatus() == EnrollmentStatus.WAITLISTED) {
                e.setStatus(EnrollmentStatus.CANCELLED);
            }
        });

        repository.delete(waitlist);
    }
}