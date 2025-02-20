package io.github.nhatbangle.sdp.software.entity.deployment;

import io.github.nhatbangle.sdp.software.entity.User;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentProcessHasUserId;
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
@Table(name = "deployment_process_has_user")
@EntityListeners(AuditingEntityListener.class)
public class DeploymentProcessHasUser {
    @EmbeddedId
    private DeploymentProcessHasUserId id;

    @MapsId("processId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PROCESS_id", nullable = false)
    private DeploymentProcess process;

    @MapsId("userId")
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinColumn(name = "USER_id", nullable = false)
    private User user;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;
}