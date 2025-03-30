package io.github.nhatbangle.sdp.software.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.SoftwareLicense}
 */
public record SoftwareLicenseDetailResponse(
        String id,
        String description,
        long startTimeMs,
        long endTimeMs,
        int expireAlertIntervalDay,
        boolean isExpireAlertDone,
        long createdAtMs,
        @Nullable Long updatedAtMs,
        DeploymentProcessDto process,
        String licenseCreatorId
) implements Serializable {
    /**
     * DTO for {@link io.github.nhatbangle.sdp.software.entity.deployment.DeploymentProcess}
     */
    public record DeploymentProcessDto(
            long id,
            String softwareVersionId,
            String softwareVersionName,
            String customerId,
            String customerName,
            String customerEmail,
            String creatorId
    ) implements Serializable {
    }
}