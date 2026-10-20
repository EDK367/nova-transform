package org.nova.service.impl.gymclass;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.common.PageResponse;
import org.nova.domain.enums.ClassStatus;
import org.nova.domain.model.branch.Branch;
import org.nova.domain.model.gymclass.Class;
import org.nova.domain.model.gymclass.ClassType;
import org.nova.domain.model.trainer.Trainer;
import org.nova.exception.BadRequestException;
import org.nova.exception.NotFoundException;
import org.nova.repository.*;
import org.nova.service.gymclass.ClassService;
import org.nova.web.dto.request.gymclass.CreateClassRequest;
import org.nova.web.dto.request.gymclass.UpdateClassRequest;
import org.nova.web.dto.response.gymclass.ClassResponse;
import org.nova.web.mapper.ClassMapper;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

    private final ClassRepository repository;
    private final BranchRepository branchRepository;
    private final ClassTypeRepository classTypeRepository;
    private final TrainerRepository trainerRepository;
    private final ClassEnrollmentRepository enrollmentRepository;
    private final WaitlistRepository waitlistRepository;
    private final ClassRatingRepository ratingRepository;
    private final ClassMapper mapper;

    @Override
    public List<Class> findAll() {
        return repository.listAll();
    }

    @Override
    public Optional<Class> findById(Long id) {
        return repository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public Class create(Class gymClass) {
        repository.persist(gymClass);
        return gymClass;
    }

    @Override
    @Transactional
    public Class update(Long id, Class gymClass) {
        Class existing = repository.findById(id);

        if (existing == null) {
            return null;
        }

        existing.setIdBranch(gymClass.getIdBranch());
        existing.setIdClassType(gymClass.getIdClassType());
        existing.setIdTrainer(gymClass.getIdTrainer());
        existing.setStatus(gymClass.getStatus());
        existing.setSchedule(gymClass.getSchedule());
        existing.setDuration(gymClass.getDuration());
        existing.setMaxCapacity(gymClass.getMaxCapacity());

        return existing;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public PageResponse<ClassResponse> findPaged(
            int page,
            int size,
            Long branchId,
            Long classTypeId,
            UUID trainerId,
            ClassStatus status,
            Instant startDate,
            Instant endDate,
            String sortBy,
            String sortDirection
    ) {
        Sort.Direction direction = (sortDirection != null && sortDirection.equalsIgnoreCase("desc"))
                ? Sort.Direction.Descending
                : Sort.Direction.Ascending;
        String property = (sortBy != null && !sortBy.isBlank()) ? sortBy : "schedule";
        if (!property.contains(".")) {
            property = "c." + property;
        }
        Sort sort = Sort.by(property, direction);

        PanacheQuery<Class> query = repository.findFiltered(
                branchId, classTypeId, trainerId, status, startDate, endDate, sort
        ).page(Page.of(page, size));

        List<ClassResponse> content = query.list().stream()
                .map(this::toResponseDto)
                .toList();

        long totalElements = query.count();
        int totalPages = size > 0 ? (int) Math.ceil((double) totalElements / size) : 0;
        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Override
    public ClassResponse findResponseById(Long id) {
        Class c = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + id));
        return toResponseDto(c);
    }

    @Override
    @Transactional
    public ClassResponse createClass(CreateClassRequest request) {
        Branch branch = branchRepository.findByIdOptional(request.branchId())
                .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
        if (!branch.isActive()) {
            throw new BadRequestException("La sucursal seleccionada no está activa");
        }

        ClassType classType = classTypeRepository.findByIdOptional(request.classTypeId())
                .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + request.classTypeId()));

        Trainer trainer = trainerRepository.findByIdOptional(request.trainerId())
                .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + request.trainerId()));

        Class c = new Class();
        c.setIdBranch(branch);
        c.setIdClassType(classType);
        c.setIdTrainer(trainer);
        c.setStatus(ClassStatus.SCHEDULED);
        c.setSchedule(request.schedule());
        c.setDuration(request.duration());
        c.setMaxCapacity(request.maxCapacity());

        repository.persist(c);
        return toResponseDto(c);
    }

    @Override
    @Transactional
    public ClassResponse updateClass(Long id, UpdateClassRequest request) {
        Class c = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + id));

        if (request.branchId() != null) {
            Branch branch = branchRepository.findByIdOptional(request.branchId())
                    .orElseThrow(() -> new NotFoundException("Sucursal no encontrada con ID: " + request.branchId()));
            if (!branch.isActive()) {
                throw new BadRequestException("La sucursal seleccionada no está activa");
            }
            c.setIdBranch(branch);
        }

        if (request.classTypeId() != null) {
            ClassType classType = classTypeRepository.findByIdOptional(request.classTypeId())
                    .orElseThrow(() -> new NotFoundException("Tipo de clase no encontrado con ID: " + request.classTypeId()));
            c.setIdClassType(classType);
        }

        if (request.trainerId() != null) {
            Trainer trainer = trainerRepository.findByIdOptional(request.trainerId())
                    .orElseThrow(() -> new NotFoundException("Entrenador no encontrado con ID: " + request.trainerId()));
            c.setIdTrainer(trainer);
        }

        if (request.schedule() != null) {
            c.setSchedule(request.schedule());
        }
        if (request.duration() != null) {
            c.setDuration(request.duration());
        }
        if (request.maxCapacity() != null) {
            c.setMaxCapacity(request.maxCapacity());
        }
        if (request.status() != null) {
            c.setStatus(request.status());
        }

        repository.persist(c);
        return toResponseDto(c);
    }

    @Override
    @Transactional
    public ClassResponse cancelClass(Long id) {
        Class c = repository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Clase no encontrada con ID: " + id));

        if (c.getStatus() == ClassStatus.CANCELLED) {
            throw new BadRequestException("La clase ya se encuentra cancelada");
        }

        c.setStatus(ClassStatus.CANCELLED);
        repository.persist(c);
        return toResponseDto(c);
    }

    private ClassResponse toResponseDto(Class c) {
        long enrolledCount = enrollmentRepository.countEnrolledByClassId(c.getId());
        long waitlistCount = waitlistRepository.countByClassId(c.getId());
        Double avgRating = ratingRepository.calculateAverageRatingByClassId(c.getId());
        return mapper.toResponse(c, enrolledCount, waitlistCount, avgRating);
    }
}