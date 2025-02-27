package io.github.nhatbangle.sdp.software.projection.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public record DeploymentProcessInfo(
        long id,
        DeploymentProcessStatus status,
        Instant createdAt,
        @Nullable Instant updatedAt,
        String softwareName,
        String softwareVersionName,
        String customerName
) implements Serializable {
}