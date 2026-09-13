package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.ClassEnrollment;
import org.nova.repository.ClassEnrollmentRepository;
import org.nova.service.gymclass.ClassEnrollmentService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassEnrollmentServiceImpl implements ClassEnrollmentService {

    private final ClassEnrollmentRepository classEnrollmentRepository;

    @Override
    public List<ClassEnrollment> findAll() {
        return classEnrollmentRepository.listAll();
    }

    @Override
    public Optional<ClassEnrollment> findById(Long id) {
        return classEnrollmentRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassEnrollment create(ClassEnrollment classEnrollment) {
        classEnrollmentRepository.persist(classEnrollment);
        return classEnrollment;
    }

    @Override
    @Transactional
    public ClassEnrollment update(Long id, ClassEnrollment classEnrollment) {
        ClassEnrollment existing = classEnrollmentRepository.findById(id);

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
        classEnrollmentRepository.deleteById(id);
    }
}