package org.nova.domain.model.gymclass;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "class_ratings", schema = "public", indexes = {@Index(name = "uq_class_rating",
        columnList = "id_enrollment",
        unique = true)})
public class ClassRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_enrollment", nullable = false)
    private ClassEnrollment idEnrollment;

    @NotNull
    @Column(name = "rating", nullable = false)
    private Short rating;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "date_time", nullable = false)
    private Instant dateTime;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}