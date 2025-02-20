package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase}
 */
public record DeploymentPhaseUpdateRequest(
        @NotNull @Min(0) Integer numOrder,
        @Nullable @Size(max = 255) String description,
        @NotNull LocalDate plannedStartDate,
        @NotNull LocalDate plannedEndDate
) implements Serializable {
}