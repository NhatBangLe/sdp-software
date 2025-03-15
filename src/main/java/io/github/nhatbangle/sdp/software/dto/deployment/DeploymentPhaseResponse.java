package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.annotation.Nullable;

import java.io.Serializable;

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
        String plannedStartDate,
        String plannedEndDate,
        String actualStartDate,
        String actualEndDate,
        boolean isDone,
        @Nullable String userLastUpdatedId
) implements Serializable {
    public record DeploymentPhaseType(String name) implements Serializable {
    }

}