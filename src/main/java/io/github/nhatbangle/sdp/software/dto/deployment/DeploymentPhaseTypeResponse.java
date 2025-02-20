package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType}
 */
public record DeploymentPhaseTypeResponse(
        String id,
        String name,
        String description,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}