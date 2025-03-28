package io.github.nhatbangle.sdp.software.projection.deployment;

import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.UpdatePhaseHistory}
 */
public interface UpdatePhaseHistoryInfo {
    String getDescription();

    @Value("#{target.isDone}")
    Boolean getIsDone();

    Instant getUpdatedAt();

    UpdatePhaseHistoryIdInfo getId();

    DeploymentPhaseInfo getPhase();

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.composite.UpdatePhaseHistoryId}
     */
    interface UpdatePhaseHistoryIdInfo {
        Long getNumOrder();

        String getUserId();

        String getPhaseId();
    }

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase}
     */
    interface DeploymentPhaseInfo {
        DeploymentPhaseTypeInfo getType();

        /**
         * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhaseType}
         */
        interface DeploymentPhaseTypeInfo {
            String getName();
        }
    }
}