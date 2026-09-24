package org.nova.service.impl.gymclass;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.nova.domain.model.gymclass.ClassRating;
import org.nova.repository.ClassRatingRepository;
import org.nova.service.gymclass.ClassRatingService;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class ClassRatingServiceImpl implements ClassRatingService {

    private final ClassRatingRepository repository;

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
}