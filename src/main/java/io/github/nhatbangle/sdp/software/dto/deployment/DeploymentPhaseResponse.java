package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase}
 */
public record DeploymentPhaseResponse(
        String id,
        int numOrder,
        @Nullable String description,
        long createdAtMs,
        @Nullable Long updatedAtMs,
        DeploymentPhaseType type,
        LocalDate plannedStartDate,
        LocalDate plannedEndDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,
        boolean isDone,
        @Nullable String userLastUpdatedId
) implements Serializable {
    public record DeploymentPhaseType(String name) implements Serializable {
    }

}