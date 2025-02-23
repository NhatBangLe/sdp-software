package io.github.nhatbangle.sdp.software.dto.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentMemberOperator;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcessHasUser}
 */
public record DeploymentPhaseMemberUpdateRequest(
        @NotNull @UUID String memberId,
        @NotNull DeploymentMemberOperator operator
) implements Serializable {
}