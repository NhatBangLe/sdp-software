package io.github.nhatbangle.sdp.software.dto.deployment;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public record DeploymentProcessCreateRequest(
        @UUID @NotNull String softwareVersionId,
        @UUID @NotNull String customerId,
        @NotNull List<@NotNull @UUID String> moduleVersionIds
) implements Serializable {
}