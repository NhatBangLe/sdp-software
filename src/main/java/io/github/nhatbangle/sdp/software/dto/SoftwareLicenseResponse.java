package io.github.nhatbangle.sdp.software.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link io.github.nhatbangle.sdp.software.entity.SoftwareLicense}
 */
public record SoftwareLicenseResponse(
        String id,
        @Nullable String description,
        long startTimeMs,
        long endTimeMs,
        long expireAlertIntervalMs,
        long createdAtMs,
        @Nullable Long updatedAtMs
) implements Serializable {
}