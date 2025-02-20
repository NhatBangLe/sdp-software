package io.github.nhatbangle.sdp.software.dto.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public record DeploymentProcessResponse(
        long id,
        DeploymentProcessStatus status,
        long createdAtMs,
        @Nullable Long updatedAtMs,
        Software software,
        Customer customer
) implements Serializable {
    public record Customer(String name) implements Serializable {
    }
    public record Software(String name, String versionName) implements Serializable {
    }
}