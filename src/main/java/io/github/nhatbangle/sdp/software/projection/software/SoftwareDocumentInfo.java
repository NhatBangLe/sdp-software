package io.github.nhatbangle.sdp.software.projection.software;

import io.github.nhatbangle.sdp.software.projection.DocumentTypeName;

import java.time.Instant;

/**
 * Projection for {@link io.github.nhatbangle.sdp.software.entity.software.SoftwareDocument}
 */
public interface SoftwareDocumentInfo {
    String getId();

    String getName();

    String getDescription();

    Instant getCreatedAt();

    Instant getUpdatedAt();

    DocumentTypeName getType();

    SoftwareVersionName getVersion();
}