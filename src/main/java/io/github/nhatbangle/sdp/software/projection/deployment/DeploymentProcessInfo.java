package io.github.nhatbangle.sdp.software.projection.deployment;

import io.github.nhatbangle.sdp.software.constant.DeploymentProcessStatus;
import io.github.nhatbangle.sdp.software.projection.customer.CustomerName;
import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionName;
import jakarta.annotation.Nullable;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public interface DeploymentProcessInfo {
    Long getId();

    DeploymentProcessStatus getStatus();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();

    SoftwareVersionName getSoftwareVersion();

    CustomerName getCustomer();
}