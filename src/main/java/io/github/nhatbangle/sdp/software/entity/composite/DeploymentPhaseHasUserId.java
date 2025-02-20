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
public class DeploymentPhaseHasUserId implements Serializable {
    @Serial
    private static final long serialVersionUID = 7446620805056821558L;

    @UUID
    @NotNull
    @Column(name = "USER_id", nullable = false, length = 36)
    private String userId;

    @UUID
    @NotNull
    @Column(name = "PHASE_id", nullable = false, length = 36)
    private String phaseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeploymentPhaseHasUserId entity = (DeploymentPhaseHasUserId) o;
        return Objects.equals(this.phaseId, entity.phaseId) &&
               Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phaseId, userId);
    }

}