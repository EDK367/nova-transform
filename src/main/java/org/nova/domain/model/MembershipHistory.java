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
@Table(name = "membership_histories", schema = "public")
public class MembershipHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_membership", nullable = false)
    private Membership idMembership;

    @Column(name = "previous_status", columnDefinition = "membership_status_enum not null")
    private Object previousStatus;

    @Column(name = "new_status", columnDefinition = "membership_status_enum not null")
    private Object newStatus;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "change_date", nullable = false)
    private Instant changeDate;

    @Column(name = "observation", length = Integer.MAX_VALUE)
    private String observation;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;


}