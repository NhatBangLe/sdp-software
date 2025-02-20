package io.github.nhatbangle.sdp.software.entity.composite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class UpdatePhaseHistoryId implements Serializable {
    @Serial
    private static final long serialVersionUID = 2142179798908059821L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "num_order", columnDefinition = "int UNSIGNED not null")
    private Long numOrder;

    @Size(max = 36)
    @NotNull
    @Column(name = "USER_id", nullable = false, length = 36)
    private String userId;

    @Size(max = 36)
    @NotNull
    @Column(name = "PHASE_id", nullable = false, length = 36)
    private String phaseId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UpdatePhaseHistoryId entity = (UpdatePhaseHistoryId) o;
        return Objects.equals(this.numOrder, entity.numOrder) &&
               Objects.equals(this.phaseId, entity.phaseId) &&
               Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numOrder, phaseId, userId);
    }

}