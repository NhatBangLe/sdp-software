package io.github.nhatbangle.sdp.software.entity;

import io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "software_license")
@EntityListeners(AuditingEntityListener.class)
public class SoftwareLicense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Nullable
    @Size(max = 255)
    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @NotNull
    @Builder.Default
    @ColumnDefault("'15'")
    @Column(name = "expire_alert_interval_day", columnDefinition = "smallint UNSIGNED not null")
    private Integer expireAlertIntervalDay = 15;

    @NotNull
    @ColumnDefault("b'0'")
    @Column(name = "is_expire_alert_done", nullable = false)
    private Boolean isExpireAlertDone = false;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Nullable
    @LastModifiedDate
    @Column(name = "updated_at")
    private Instant updatedAt;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPLOYMENT_PROCESS_id", nullable = false)
    private DeploymentProcess process;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_CREATOR_id", nullable = false)
    private User creator;
}