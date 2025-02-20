package io.github.nhatbangle.sdp.software.dto.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public record DeploymentProcessUpdateRequest(
        @NotNull DeploymentProcessStatus status
) implements Serializable {
}