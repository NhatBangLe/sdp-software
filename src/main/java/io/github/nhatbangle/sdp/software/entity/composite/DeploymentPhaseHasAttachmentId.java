package io.github.nhatbangle.sdp.software.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.UUID;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class DeploymentPhaseHasAttachmentId implements Serializable {
    @Serial
    private static final long serialVersionUID = 3060321132512634176L;

    @UUID
    @NotNull
    @Column(name = "DEPLOYMENT_PHASE_id", nullable = false, length = 36)
    private String phaseId;

    @UUID
    @NotNull
    @Column(name = "ATTACHMENT_id", nullable = false, length = 36)
    private String attachmentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeploymentPhaseHasAttachmentId entity = (DeploymentPhaseHasAttachmentId) o;
        return Objects.equals(this.attachmentId, entity.attachmentId) &&
               Objects.equals(this.phaseId, entity.phaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentId, phaseId);
    }
}