package io.github.nhatbangle.sdp.software.dto.deployment;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.UpdatePhaseHistory}
 */
public record DeploymentPhaseHistoryResponse(
        PhaseHistoryId id,
        PhaseType phaseType,
        String description,
        boolean isDone,
        long updatedAt
) implements Serializable {
    /**
     * DTO for {@link io.github.nhatbangle.sdp.software.entity.composite.UpdatePhaseHistoryId}
     */
    public record PhaseHistoryId(
            Long numOrder,
            String userIdPerformed,
            String phaseId
    ) implements Serializable {
    }

    /**
     * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType}
     */
    public record PhaseType(
            String name
    ) implements Serializable {
    }
}