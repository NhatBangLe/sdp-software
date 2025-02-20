package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase}
 */
public record DeploymentPhaseUpdateActualDatesRequest(
        @Nullable String description,
        @Nullable LocalDate actualStartDate,
        @Nullable LocalDate actualEndDate,
        @NotNull @UUID String updatedByUserId
) implements Serializable {
}