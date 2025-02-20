package io.github.nhatbangle.sdp.software.projection.software;

import jakarta.annotation.Nullable;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareVersion}
 */
public interface SoftwareVersionInfo {
    String getId();

    String getName();

    @Nullable
    String getDescription();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();
}