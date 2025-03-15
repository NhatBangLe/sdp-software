package io.github.nhatbangle.sdp.software.projection.deployment;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Value;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentPhase}
 */
public interface DeploymentPhaseInfo {
    String getId();

    Integer getNumOrder();

    @Nullable
    String getDescription();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();

    DeploymentPhaseTypeName getType();

    LocalDate getPlannedStartDate();

    LocalDate getPlannedEndDate();

    LocalDate getActualStartDate();

    LocalDate getActualEndDate();

    @Value("#{target.isDone}")
    Boolean getIsDone();

    UserInfo getUserLastUpdate();

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.User}
     */
    interface UserInfo {
        String getId();
    }
}