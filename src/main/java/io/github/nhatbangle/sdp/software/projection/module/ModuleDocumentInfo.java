package io.github.nhatbangle.sdp.software.projection.module;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.module.ModuleDocument}
 */
public interface ModuleDocumentInfo {
    String getId();

    String getName();

    String getDescription();

    Instant getCreatedAt();

    Instant getUpdatedAt();
}