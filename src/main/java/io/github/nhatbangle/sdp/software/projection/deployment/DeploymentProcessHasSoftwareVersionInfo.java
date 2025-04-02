package io.github.nhatbangle.sdp.software.projection.deployment;

import io.github.nhatbangle.sdp.software.projection.software.SoftwareVersionIdName;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
 */
public interface DeploymentProcessHasSoftwareVersionInfo {
    Long getId();

    CustomerInfo getCustomer();

    SoftwareVersionIdName getSoftwareVersion();

    /**
     * Projection for {@link io.github.nhatbangle.sdp.software.entity.Customer}
     */
    interface CustomerInfo {
        String getId();
    }
}