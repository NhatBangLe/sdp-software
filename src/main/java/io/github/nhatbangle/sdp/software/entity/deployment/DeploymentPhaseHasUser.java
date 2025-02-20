package io.github.nhatbangle.sdp.software.entity.deployment;

import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasUserId;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "deployment_phase_has_user")
@EntityListeners(AuditingEntityListener.class)
public class DeploymentPhaseHasUser {
    @EmbeddedId
    private DeploymentPhaseHasUserId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_id", nullable = false)
    private User user;

    @MapsId("phaseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PHASE_id", nullable = false)
    private DeploymentPhase phase;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}