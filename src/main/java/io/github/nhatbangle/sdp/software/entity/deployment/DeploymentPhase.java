package io.github.nhatbangle.sdp.software.entity.deployment;

import io.github.nhatbangle.sdp.software.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deployment_phase")
@EntityListeners(AuditingEntityListener.class)
public class DeploymentPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Min(0)
    @ColumnDefault("'0'")
    @Column(name = "num_order", columnDefinition = "smallint UNSIGNED not null")
    private Integer numOrder;

    @Nullable
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Nullable
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "TYPE_id", nullable = false)
    private DeploymentPhaseType type;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROCESS_id", nullable = false)
    private DeploymentProcess process;

    @NotNull
    @Column(name = "planned_start_date", nullable = false)
    private LocalDate plannedStartDate;

    @NotNull
    @Column(name = "planned_end_date", nullable = false)
    private LocalDate plannedEndDate;

    @Nullable
    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Nullable
    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @NotNull
    @Builder.Default
    @ColumnDefault("b'0'")
    @Column(name = "is_done", nullable = false)
    private Boolean isDone = false;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_last_update_id")
    private User userLastUpdate;

    @Nullable
    @OneToMany(mappedBy = "phase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<DeploymentPhaseHasAttachment> attachments;

    @Nullable
    @OneToMany(mappedBy = "phase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<DeploymentPhaseHasUser> users;

    @Nullable
    @OneToMany(mappedBy = "phase", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<UpdatePhaseHistory> histories;
}