package io.github.nhatbangle.sdp.software.entity.deployment;

import io.github.nhatbangle.sdp.software.entity.Attachment;
import io.github.nhatbangle.sdp.software.entity.composite.DeploymentPhaseHasAttachmentId;
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
@Table(name = "deployment_phase_has_attachment")
@EntityListeners(AuditingEntityListener.class)
public class DeploymentPhaseHasAttachment {
    @EmbeddedId
    private DeploymentPhaseHasAttachmentId id;

    @MapsId("phaseId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DEPLOYMENT_PHASE_id", nullable = false)
    private DeploymentPhase phase;

    @MapsId("attachmentId")
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    )
    @JoinColumn(name = "ATTACHMENT_id", nullable = false)
    private Attachment attachment;

    @CreatedDate
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

}