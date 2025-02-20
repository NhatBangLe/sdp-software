package io.github.nhatbangle.sdp.software.projection.deployment;

import jakarta.annotation.Nullable;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType}
 */
public interface DeploymentPhaseTypeInfo {
    String getId();

    String getName();

    @Nullable
    String getDescription();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();
}