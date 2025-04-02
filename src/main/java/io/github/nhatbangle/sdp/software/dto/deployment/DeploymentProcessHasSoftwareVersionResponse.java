package io.github.nhatbangle.sdp.software.dto.deployment;

import io.github.nhatbangle.sdp.software.dto.software.SoftwareNameAndVersionResponse;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public record DeploymentProcessHasSoftwareVersionResponse(
        Long processId,
        String customerId,
        SoftwareNameAndVersionResponse softwareVersion
) implements Serializable {
}