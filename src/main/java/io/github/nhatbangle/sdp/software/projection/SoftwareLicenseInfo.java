package io.github.nhatbangle.sdp.software.projection;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.SoftwareLicense}
 */
public interface SoftwareLicenseInfo {
    String getId();

    String getDescription();

    Instant getStartTime();

    Instant getEndTime();

    Integer getExpireAlertIntervalDay();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}