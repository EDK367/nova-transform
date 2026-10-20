package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.enums.EnrollmentStatus;
import org.nova.domain.model.gymclass.ClassEnrollment;
import org.nova.domain.model.gymclass.ClassRating;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthorizationException;
import org.nova.exception.BadRequestException;
import org.nova.exception.ConflictException;
import org.nova.exception.NotFoundException;
import org.nova.repository.ClassEnrollmentRepository;
import org.nova.repository.ClassRatingRepository;
import org.nova.repository.ClassRepository;
import org.nova.repository.UserRepository;
import org.nova.service.gymclass.ClassRatingService;
import org.nova.web.dto.request.gymclass.CreateClassRatingRequest;
import org.nova.web.dto.response.gymclass.ClassRatingResponse;
import org.nova.web.mapper.ClassRatingMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassRatingServiceImpl implements ClassRatingService {

    private final ClassRatingRepository repository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;
    private final ClassRatingMapper mapper;

    @Override
    public List<ClassRating> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<ClassRating> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassRating create(ClassRating classRating) {
        repository.persist(classRating);
        return classRating;
    }

    @Override
    @Transactional
    public ClassRating update(Long id, ClassRating classRating) {
        ClassRating existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdEnrollment(classRating.getIdEnrollment());
        existing.setRating(classRating.getRating());
        existing.setComment(classRating.getComment());
        existing.setDateTime(classRating.getDateTime());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public ClassRatingResponse createRating(CreateClassRatingRequest request, String authenticatedUsername) {
        ClassEnrollment enrollment = enrollmentRepository.findByIdWithDetails(request.enrollmentId())
                .orElseThrow(() -> new NotFoundException("Inscripción no encontrada con ID: " + request.enrollmentId()));

        User user = userRepository.findByUsername(authenticatedUsername)
                .orElseThrow(() -> new NotFoundException("Usuario autenticado no encontrado: " + authenticatedUsername));

        if (!user.getId().equals(enrollment.getIdMember().getId())) {
            throw new AuthorizationException("No tiene permisos para calificar una inscripción ajena");
        }

        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new BadRequestException("No se puede calificar una inscripción cancelada");
        }

        if (repository.existsByEnrollmentId(enrollment.getId())) {
            throw new ConflictException("Esta inscripción ya cuenta con una calificación");
        }

        ClassRating rating = new ClassRating();
        rating.setIdEnrollment(enrollment);
        rating.setRating(request.rating());
        rating.setComment(request.comment());
        rating.setDateTime(Instant.now());

        repository.persist(rating);
        return mapper.toResponse(rating);
    }

    @Override
    public List<ClassRatingResponse> findByClassId(Long classId) {
        classRepository.findByIdOptional(classId)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + classId));
        return mapper.toResponseList(repository.findByClassId(classId));
    }

    @Override
    public ClassRatingResponse findResponseById(Long id) {
        ClassRating rating = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Calificación no encontrada con ID: " + id));
        return mapper.toResponse(rating);
    }

    @Override
    public Double getAverageRating(Long classId) {
        classRepository.findByIdOptional(classId)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + classId));
        return repository.calculateAverageRatingByClassId(classId);
    }
}