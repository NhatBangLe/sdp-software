package io.github.nhatbangle.sdp.software.projection;

import jakarta.annotation.Nullable;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.DocumentType}
 */
public interface DocumentTypeInfo {
    String getId();

    String getName();

    @Nullable
    String getDescription();

    Instant getCreatedAt();

    @Nullable
    Instant getUpdatedAt();
}