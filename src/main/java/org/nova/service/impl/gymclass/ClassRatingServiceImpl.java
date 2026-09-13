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

    private final ClassRatingRepository classRatingRepository;

    @Override
    public List<ClassRating> findAll() {
        return classRatingRepository.listAll();
    }

    @Override
    public Optional<ClassRating> findById(Long id) {
        return classRatingRepository.findByIdOptional(id);
    }

    @Override
    @Transactional
    public ClassRating create(ClassRating classRating) {
        classRatingRepository.persist(classRating);
        return classRating;
    }

    @Override
    @Transactional
    public ClassRating update(Long id, ClassRating classRating) {
        ClassRating existing = classRatingRepository.findById(id);

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
        classRatingRepository.deleteById(id);
    }
}