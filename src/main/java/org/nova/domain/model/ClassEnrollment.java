package org.nova.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.nova.domain.enums.EnrollmentStatus;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "class_enrollments", schema = "public", indexes = {@Index(name = "uq_class_enrollment",
        columnList = "id_class, id_member",
        unique = true)})
public class ClassEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_class", nullable = false)
    private Class idClass;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_member", nullable = false)
    private Member idMember;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'ENROLLED'")
    @Column(name = "status", nullable = false, columnDefinition = "enrollment_status_enum")
    private EnrollmentStatus status;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "enrollment_date", nullable = false)
    private Instant enrollmentDate;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}