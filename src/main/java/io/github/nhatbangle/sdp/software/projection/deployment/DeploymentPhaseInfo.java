package io.github.nhatbangle.sdp.software.projection.deployment;

import io.github.nhatbangle.sdp.software.entity.User;
import jakarta.annotation.Nullable;

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

    Boolean getIsDone();

    User getUserLastUpdate();
}