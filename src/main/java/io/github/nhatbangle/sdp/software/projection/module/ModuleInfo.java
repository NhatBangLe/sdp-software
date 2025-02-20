package io.github.nhatbangle.sdp.software.projection.module;

import jakarta.annotation.Nullable;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.module.Module}
 */
public interface ModuleInfo {
    String getId();

    String getName();

    @Nullable
    String getDescription();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();
}