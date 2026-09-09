package org.nova.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "classes", schema = "public")
public class Class {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_branch", nullable = false)
    private Branch idBranch;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_class_type", nullable = false)
    private ClassType idClassType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_trainer", nullable = false)
    private Trainer idTrainer;

    @ColumnDefault("'SCHEDULED'")
    @Column(name = "status", columnDefinition = "class_status_enum not null")
    private Object status;

    @NotNull
    @Column(name = "schedule", nullable = false)
    private Instant schedule;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Long duration;

    @NotNull
    @Column(name = "max_capacity", nullable = false)
    private Long maxCapacity;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}