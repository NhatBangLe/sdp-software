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
public class DeploymentProcessHasUserId implements Serializable {
    @Serial
    private static final long serialVersionUID = -1298588981317263555L;

    @Column(name = "PROCESS_id", columnDefinition = "int UNSIGNED not null")
    private Long processId;

    @UUID
    @NotNull
    @Column(name = "USER_id", nullable = false, length = 36)
    private String userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeploymentProcessHasUserId entity = (DeploymentProcessHasUserId) o;
        return Objects.equals(this.processId, entity.processId) &&
               Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(processId, userId);
    }

}